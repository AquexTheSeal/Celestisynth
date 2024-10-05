package org.thecelestialworkshop.celestisynth.common.entity.mob.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import org.thecelestialworkshop.celestisynth.common.entity.base.MonolithSummonedEntity;
import org.thecelestialworkshop.celestisynth.common.entity.goals.star_monolith.StarMonolithSpikeGoal;
import org.thecelestialworkshop.celestisynth.common.entity.helper.MonolithRunes;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.common.registry.CSTags;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class StarMonolith extends Mob implements GeoEntity, FixedMovesetEntity, CSWeaponUtil {
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(StarMonolith.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(StarMonolith.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(StarMonolith.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RUNE = SynchedEntityData.defineId(StarMonolith.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final int ACTION_SPIKE = 1;

    public static final int VARIANT_OVERWORLD = 0;
    public static final int VARIANT_NETHER = 1;
    public static final int VARIANT_END = 2;

    public StarMonolith(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FLYING_SPEED, 0.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new StarMonolithSpikeGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.getRune().ambientTick.accept(this, this.level());
        if (!this.level().isClientSide()) {
            if (level().getDifficulty() != Difficulty.PEACEFUL && this.hasNearbyAlivePlayerWithFilter(getX(), getY(), getZ(), 128)) {
                if (getRune().summonInterval > 0) {
                    if (tickCount % getRune().summonInterval == 0) {
                        int summoned = StreamSupport.stream(((ServerLevel) level()).getAllEntities().spliterator(), false).filter(e -> e instanceof MonolithSummonedEntity summonedEntity && summonedEntity.getMonolith() == this).toList().size();
                        if (summoned < getRune().summonLimit) {
                            List<BlockPos> validSpawns = getValidSpawnPoints(getRune().summonRange);
                            if (!validSpawns.isEmpty()) {
                                for (BlockPos targetPos : getRandomBlockPositionsWithinList(validSpawns, this.getRune().summonClusterSize)) {
                                    this.getRune().summonAction.accept(this, targetPos, (ServerLevel) this.level());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public List<BlockPos> getRandomBlockPositionsWithinList(List<BlockPos> posList, int listSize) {
        ArrayList<BlockPos> poses = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            poses.add(posList.get(random.nextInt(posList.size())));
        }
        return poses.stream().toList();
    }

    public boolean hasNearbyAlivePlayerWithFilter(double pX, double pY, double pZ, double pDistance) {
        for (Player player : this.level().players()) {
            if (EntitySelector.NO_SPECTATORS.test(player) && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player) && this.hasLineOfSight(player)) {
                double distanceOfPlayer = player.distanceToSqr(pX, pY, pZ);
                if (pDistance < 0.0D || distanceOfPlayer < pDistance * pDistance) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<BlockPos> getValidSpawnPoints(int range) {
        ArrayList<BlockPos> blockPositions = new ArrayList<>();
        for (int xx = -range; xx <= range; xx++) {
            for (int yy = -range; yy <= range; yy++) {
                for (int zz = -range; zz <= range; zz++) {
                    BlockPos toAddPos = this.blockPosition().offset(xx, yy, zz);
                    if (level().getBlockState(toAddPos.below()).isFaceSturdy(level(), toAddPos.below(), Direction.UP)) {
                        if (level().getBlockState(toAddPos).isAir()) {
                            if (hasLineOfSight(toAddPos)) {
                                blockPositions.add(toAddPos);
                            }
                        }
                    }
                }
            }
        }
        return blockPositions;
    }

    public boolean hasLineOfSight(BlockPos pos) {
        Vec3 vec3 = this.position();
        Vec3 vec31 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        return this.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) -> {
            if (getAction() == ACTION_SPIKE) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.star_monolith.spike"));
            }
            if (isDeadOrDying()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.star_monolith.death"));
            }
            if (getRune() != MonolithRunes.NO_RUNE) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.star_monolith.idle"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.star_monolith.off"));
        }));
    }

    public static boolean canSpawn(EntityType<StarMonolith> pEntityType, ServerLevelAccessor pServerLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return checkMobSpawnRules(pEntityType, pServerLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.moveTo(this.getFloorPositionUnderPlayer(level(), this.blockPosition()).above(), 0, 0);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        for (Map.Entry<TagKey<Item>, MonolithRunes> activatorEntry : MonolithRunes.ACTIVATORS_LIST.entrySet()) {
            if (stack.is(activatorEntry.getKey())) {
                this.setRune(activatorEntry.getValue());
                if (!pPlayer.isCreative()) {
                    stack.shrink(1);
                }
                this.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
                this.playSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        if (stack.is(CSItems.CELESTIAL_DEBUGGER.get())) {
            this.setRune(MonolithRunes.APOCALYPTIC_RUNE);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1024);
            this.setHealth(this.getMaxHealth());
            this.getAttribute(Attributes.ARMOR).setBaseValue(1024);
            this.playSound(SoundEvents.WITHER_BREAK_BLOCK, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(pPlayer, pHand);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.NATURAL || pReason == MobSpawnType.STRUCTURE || pReason == MobSpawnType.CHUNK_GENERATION) {
            if (level().dimension().equals(Level.OVERWORLD)) {
                setVariant(VARIANT_OVERWORLD);

                if (pLevel.getBiome(blockPosition()).is(BiomeTags.IS_OCEAN) || pLevel.getBiome(blockPosition()).is(BiomeTags.IS_DEEP_OCEAN)) {
                    setRune(MonolithRunes.AQUA_RUNE);
                }
            }

            if (level().dimension().equals(Level.NETHER)) {
                setVariant(VARIANT_NETHER);

                if (isInCurrentStructure(CSTags.Structures.NETHER_MONOLITH_SPAWN)) setRune(MonolithRunes.BLOOD_RUNE);
            }
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public boolean isInCurrentStructure(ResourceKey<Structure> structure) {
        return this.level() instanceof ServerLevel server && server.structureManager().getStructureWithPieceAt(this.blockPosition(), structure).isValid();
    }

    public boolean isInCurrentStructure(TagKey<Structure> structureTag) {
        return this.level() instanceof ServerLevel server && server.structureManager().getStructureWithPieceAt(blockPosition(), structureTag).isValid();
    }

    @Override
    public boolean isPersistenceRequired() {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.BLAZE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEACON_DEACTIVATE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        this.resetAction();
        if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("variant"));
        this.setRune(MonolithRunes.values()[pCompound.getInt("rune")]);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("variant", this.getVariant());
        pCompound.putInt("rune", this.getRune().ordinal());
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setRune(MonolithRunes rune) {
        this.entityData.set(RUNE, rune.ordinal());
    }

    public MonolithRunes getRune() {
        return MonolithRunes.values()[this.entityData.get(RUNE)];
    }

    @Override
    public void setAction(int action) {
        this.entityData.set(ACTION, action);
    }

    @Override
    public int getAction() {
        return this.entityData.get(ACTION);
    }

    @Override
    public int getAnimationTick() {
        return this.entityData.get(ANIMATION_TICK);
    }

    @Override
    public void setAnimationTick(int tick) {
        this.entityData.set(ANIMATION_TICK, tick);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTION, 0);
        this.entityData.define(ANIMATION_TICK, 0);
        this.entityData.define(RUNE, 0);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public int getMaxAirSupply() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public void setDeltaMovement(Vec3 motionIn) {
        super.setDeltaMovement(Vec3.ZERO.add(0, -0.3, 0));
    }

    public void knockback(double strength, double x, double z) {
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    protected void markHurt() {
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

//    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
//        return false;
//    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
    }
}
