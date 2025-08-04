package org.thecelestialworkshop.celestisynth.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.material.Fluids;

public class EntityUtil {
    public static final SpawnPlacements.Type MONOLITH_SPAWNING_CONDITION = SpawnPlacements.Type.create("any_with_solid_under", (levelReader, blockPos, entityType) ->
            SpawnPlacements.Type.ON_GROUND.canSpawnAt(levelReader, blockPos, entityType) || levelReader.getFluidState(blockPos).is(Fluids.WATER)
    );

    public static boolean isNotAPetOf(Entity owner, LivingEntity target) {
        if (target instanceof OwnableEntity ownable) {
            if (ownable.getOwner() == owner) {
                return false;
            }
        }
        return !target.isDeadOrDying() && !target.isRemoved();
    }

    private static <T extends LivingEntity & OwnableEntity> boolean isValidTargetForOwnableBase(T pet, LivingEntity potentialTarget) {
        if (potentialTarget == pet) {
            return false;
        }
        if (pet.getOwner() != null) {
            if (pet.getOwner() == potentialTarget) {
                return false;
            }
            if (potentialTarget instanceof OwnableEntity ownable) {
                if (pet.getOwner() == ownable.getOwner()) {
                    return false;
                }
            }
        }
        return !potentialTarget.isDeadOrDying() && !potentialTarget.isRemoved();
    }

    public static <T extends LivingEntity & OwnableEntity> boolean isValidTargetForOwnable(T pet, LivingEntity potentialTarget) {
        return isValidTargetForOwnableBase(pet, potentialTarget) && TargetingConditions.forCombat().test(pet, potentialTarget);
    }

    public static <T extends LivingEntity & OwnableEntity> TargetingConditions isValidTargetForOwnableCondition(T pet) {
        return TargetingConditions.forCombat().selector(target -> isValidTargetForOwnableBase(pet, target));
    }
}
