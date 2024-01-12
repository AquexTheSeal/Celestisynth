package com.aqutheseal.celestisynth.util;

import com.aqutheseal.celestisynth.common.network.util.CSSpawnParticlePacket;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public final class ParticleUtil {

    private ParticleUtil() {
        throw new IllegalAccessError("Attempted to access a Utility Class!");
    }

    public static <T extends ParticleType<?>> int sendParticles(ServerLevel serverWorld, T pType, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXOffset, double pYOffset, double pZOffset, double pXSpeed, double pYSpeed, double pZSpeed) {
        CSSpawnParticlePacket sspawnparticlepacket = new CSSpawnParticlePacket(pType, false, pPosX, pPosY, pPosZ, (float)pXOffset, (float)pYOffset, (float)pZOffset, (float)pXSpeed, (float)pYSpeed, (float)pZSpeed, pParticleCount);
        int i = 0;

        for(int j = 0; j < serverWorld.players().size(); ++j) {
            ServerPlayer serverplayerentity = serverWorld.players().get(j);
            if (sendParticles(serverplayerentity, pPosX, pPosY, pPosZ, sspawnparticlepacket)) {
                ++i;
            }
        }

        return i;
    }

    public static <T extends ParticleType<?>> int sendParticle(Level world, T pType, double pPosX, double pPosY, double pPosZ) {
        return sendParticles(world, pType, pPosX, pPosY, pPosZ, 0, 0, 0, 0);
    }

    public static <T extends ParticleType<?>> int sendParticles(Level world, T pType, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXSpeed, double pYSpeed, double pZSpeed) {
        if (!world.isClientSide()) {
            return sendParticles((ServerLevel) world, pType, pPosX, pPosY, pPosZ, pParticleCount, 0, 0, 0, pXSpeed, pYSpeed, pZSpeed);
        } else {
            return 0;
        }
    }

    public static <T extends ParticleType<?>> int sendParticles(ServerLevel serverWorld, T pType, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXSpeed, double pYSpeed, double pZSpeed) {
        return sendParticles(serverWorld, pType, pPosX, pPosY, pPosZ, pParticleCount, 0, 0, 0, pXSpeed, pYSpeed, pZSpeed);
    }

    private static boolean sendParticles(ServerPlayer pPlayer, double pPosX, double pPosY, double pPosZ, CSSpawnParticlePacket packet) {
        if (pPlayer.getLevel().isClientSide()) {
            return false;
        } else {
            BlockPos blockpos = pPlayer.blockPosition();
            if (blockpos.closerThan(new Vec3i((int) pPosX, (int) pPosY, (int) pPosZ), 1024)) {
                CSNetworkManager.sendToAll(packet);
                return true;
            } else {
                return false;
            }
        }
    }
}
