package org.thecelestialworkshop.celestisynth.common.entity.helper;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSTags;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.util.TriConsumer;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public enum MonolithRunes {
    NO_RUNE((ResourceLocation) null, 0, 0, 0, 0, MonolithRunes::noRuneTick, MonolithRunes::noRuneConsume),
    BLOOD_RUNE("blood", 400, 1, 10, 3, MonolithRunes::bloodRuneTick, MonolithRunes::bloodRuneConsume),
    AQUA_RUNE("aqua", 0, 0, 0, 0, MonolithRunes::noRuneTick, MonolithRunes::noRuneConsume),
    APOCALYPTIC_RUNE("apocalyptic", 500, 20, 10, Integer.MAX_VALUE, MonolithRunes::noRuneTick, MonolithRunes::apocalypticRuneConsume);

    public static final HashMap<TagKey<Item>, MonolithRunes> ACTIVATORS_LIST = new HashMap<>();

    public final @Nullable ResourceLocation runeTexture;
    public final int summonInterval;
    public final int summonClusterSize;
    public final int summonRange;
    public final int summonLimit;
    public final BiConsumer<StarMonolith, Level> ambientTick;
    public final TriConsumer<StarMonolith, BlockPos, ServerLevel> summonAction;

    MonolithRunes(@Nullable ResourceLocation runeTexture, int summonInterval, int summonClusterSize, int summonRange, int summonLimit, BiConsumer<StarMonolith, Level> ambientTick, TriConsumer<StarMonolith, BlockPos, ServerLevel> summonAction) {
        this.runeTexture = runeTexture;
        this.summonInterval = summonInterval;
        this.summonClusterSize = summonClusterSize;
        this.summonRange = summonRange;
        this.summonLimit = summonLimit;
        this.ambientTick = ambientTick;
        this.summonAction = summonAction;
    }

    static {
        ACTIVATORS_LIST.put(CSTags.Items.BLOOD_RUNE_ACTIVATOR, BLOOD_RUNE);
    }

    MonolithRunes(String runeId, int summonInterval, int summonClusterSize, int summonRange, int summonLimit, BiConsumer<StarMonolith, Level> ambientTick, TriConsumer<StarMonolith, BlockPos, ServerLevel> summonAction) {
        this(Celestisynth.prefix("textures/entity/mob/star_monolith/runes/star_monolith_rune_" + runeId + ".png"), summonInterval, summonClusterSize, summonRange, summonLimit, ambientTick, summonAction);
    }

    public static void noRuneTick(StarMonolith monolith, Level level) {
    }

    public static void noRuneConsume(StarMonolith monolith, BlockPos summonPosition, ServerLevel level) {
    }

    public static void bloodRuneTick(StarMonolith monolith, Level level) {
        Vec3 rot = new Vec3(monolith.getRandom().nextGaussian(), monolith.getRandom().nextGaussian(), monolith.getRandom().nextGaussian());
        ParticleUtil.sendParticle(level, CSParticleTypes.KERES_OMEN.get(), monolith.position().add(rot).add(0, 1, 0), rot.scale(0.1));
    }

    public static void bloodRuneConsume(StarMonolith monolith, BlockPos summonPosition, ServerLevel level) {
        Traverser traverser = CSEntityTypes.TRAVERSER.get().create(level);
        traverser.moveTo(summonPosition, 0, 0);
        traverser.setMonolith(monolith);
        traverser.setAction(Traverser.ACTION_SPAWNED);
        level.addFreshEntity(traverser);

        Vec3 from = monolith.position().add(0, 1.25, 0);
        Vec3 to = traverser.position();
        double distance = from.distanceTo(to);
        Vec3 direction = to.subtract(from).normalize();
        for (double i = 0; i <= distance; i += 0.2) {
            Vec3 particlePos = from.add(direction.scale(i));
            ParticleUtil.sendParticles(level, CSParticleTypes.KERES_OMEN.get(), particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0);
        }
    }

    public static void apocalypticRuneConsume(StarMonolith monolith, BlockPos summonPosition, ServerLevel level) {
        List<EntityType<? extends Mob>> hostilesList = List.of(
                EntityType.ZOMBIE, EntityType.RAVAGER, EntityType.VINDICATOR, EntityType.SKELETON, EntityType.WITHER_SKELETON
        );
        Mob chosenMob = hostilesList.get(level.random.nextInt(hostilesList.size())).create(level);
        chosenMob.moveTo(summonPosition, 0, 0);
        chosenMob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1024);
        chosenMob.setHealth(chosenMob.getMaxHealth());

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, monolith.getBoundingBox().inflate(128)).stream().filter(i -> i != monolith && !(i instanceof Player) && !hostilesList.contains(i.getType())).toList();
        if (!entities.isEmpty()) {
            chosenMob.setTarget(entities.get(monolith.getRandom().nextInt(entities.size())));
        }
        level.addFreshEntity(chosenMob);
    }
}
