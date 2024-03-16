package com.aqutheseal.celestisynth.common.entity.projectile;

import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.item.weapons.CrescentiaItem;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class CrescentiaDragon extends ThrowableProjectile implements GeoEntity, CSWeaponUtil {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public LivingEntity chomped;
    public int lifespan = 100;

    public CrescentiaDragon(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CrescentiaDragon(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public CrescentiaDragon(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
        this.noCulling = true;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 offsetVector = this.getDeltaMovement().normalize().scale(6);
        for (int i = 0; i < 15; i++) {
            double xx = random.nextGaussian() * 0.15F;
            double yy = random.nextGaussian() * 0.15F;
            double zz = random.nextGaussian() * 0.15F;
            Vec3 direction = position();
            ParticleUtil.sendParticle(level(), ParticleTypes.END_ROD, direction.x() + offsetVector.x(), direction.y() + offsetVector.y() + 0.5, direction.z() + offsetVector.z(), xx, yy, zz);
        }
        List<Entity> entities = level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(4));
        ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);
        if (getOwner() instanceof Player playerOwner) {
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != getOwner() && target.isAlive()) {
                        initiateAbilityAttack(playerOwner, target, (float) (double) CSConfigManager.COMMON.crescentiaShiftSkillDmg.get(), AttackHurtTypes.RAPID_NO_KB);
                        target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                    }
                }
                if (entityBatch instanceof Projectile projectile && projectile.getOwner() != this.getOwner()) {
                    CrescentiaItem.createCrescentiaFirework(fireworkStack, level(), playerOwner, projectile.getX(), projectile.getY(), projectile.getZ(), true);
                    projectile.remove(RemovalReason.DISCARDED);
                }
            }

            if (chomped != null) {
                Vec3 vector = this.position().add(0, -1, 0).add(offsetVector).subtract(chomped.position());
                chomped.setDeltaMovement(vector.x(), vector.y(), vector.z());
                this.lifespan -= (int) chomped.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * 4;
            }

            if (tickCount % 5 == 0) {
                if (random.nextBoolean()) {
                    CSEffectEntity.createInstance(playerOwner, this, CSVisualTypes.CRESCENTIA_STRIKE.get(), 0, -1.5, 0);
                } else {
                    CSEffectEntity.createInstance(playerOwner, this, CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get(), 0, -1.5, 0);
                }
            }

            if (tickCount % 30 == 0) {
                this.playSound(CSSoundEvents.WHIRLWIND.get(), 0.2F, 0.5F + (float) (random.nextGaussian() * 0.25F));
            }
            CSEffectEntity.createInstance(playerOwner, this, CSVisualTypes.SOLARIS_AIR.get());

            if (tickCount % 10 == 0) {
                float offX = (random.nextFloat() * 20) - 10;
                float offY = (random.nextFloat() * 20) - 10;
                float offZ = (random.nextFloat() * 20) - 10;
                CrescentiaItem.createCrescentiaFirework(fireworkStack, level(), playerOwner, getX() + offX, getY() + offY,  getZ() + offZ, false);
            }
        }

        if (tickCount > lifespan) {
            level().explode(getOwner(), getX(), getY(), getZ(), 1.0F, Level.ExplosionInteraction.MOB);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        level().explode(getOwner(), getX(), getY(), getZ(), 1.0F, Level.ExplosionInteraction.MOB);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity target && pResult.getEntity() != this.getOwner()) {
            this.chomped = target;
            if (getOwner() instanceof LivingEntity owner) {
                initiateAbilityAttack(owner, target, 2F, AttackHurtTypes.RAPID_NO_KB);
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean isControlledByLocalInstance() {
        return true;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) -> {
            //
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.crescentia_dragon.idle"));
        }));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void defineSynchedData() {
    }
}
