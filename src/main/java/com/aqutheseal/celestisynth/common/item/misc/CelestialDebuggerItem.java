package com.aqutheseal.celestisynth.common.item.misc;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class CelestialDebuggerItem extends Item {
    public CelestialDebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {

        int tip = 13 + new Random().nextInt(7);
        int topX = -10 + (new Random().nextInt(10));
        int topZ = -10 + (new Random().nextInt(10));
        int radius = 1 + new Random().nextInt(3);

        Vec3 to = new Vec3(pTarget.getX() + topX, pTarget.getY() + tip, pTarget.getZ() + topZ);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double fromCenter = Math.sqrt(x * x + z * z);
                if (fromCenter <= radius) {
                    Vec3 from = new Vec3(pTarget.getX() + x, pTarget.getY(), pTarget.getZ() + z);

                    Vec3 per = to.subtract(from).normalize();
                    Vec3 current = from.add(0, 0, 0);
                    double distance = from.distanceTo(to);

                    for (double i = 0; i < distance; i++) {
                        BlockPos targetPos = posFromVec(current);
                        if (i >= 0 && i < distance / (3 - (new Random().nextDouble()))) {
                            pTarget.level().setBlock(targetPos, Blocks.DEEPSLATE.defaultBlockState(), 3);
                        } else {
                            pTarget.level().setBlock(targetPos, CSBlocks.WINTEREIS.get().defaultBlockState(), 3);
                        }
                        current = current.add(per);

                        if (i <= 0) {
                            BlockPos getFromTarget = targetPos;
                            while (pTarget.level().isEmptyBlock(getFromTarget.below())) {
                                pTarget.level().setBlock(getFromTarget, Blocks.DEEPSLATE.defaultBlockState(), 3);
                                getFromTarget = getFromTarget.below();
                            }
                        }
                    }
                }
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }
}
