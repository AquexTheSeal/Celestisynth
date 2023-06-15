package com.aqutheseal.celestisynth.item.helpers;

import com.aqutheseal.celestisynth.network.CSNetwork;
import com.aqutheseal.celestisynth.network.CSSpawnParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CSUtilityFunctions {

    public static <T extends ParticleType<?>> int sendParticles(ServerLevel serverWorld, T pType, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXOffset, double pYOffset, double pZOffset, double pXSpeed, double pYSpeed, double pZSpeed) {
        CSSpawnParticlePacket sspawnparticlepacket = new CSSpawnParticlePacket(pType, false, pPosX, pPosY, pPosZ, (float)pXOffset, (float)pYOffset, (float)pZOffset, (float)pXSpeed, (float)pYSpeed, (float)pZSpeed, pParticleCount);
        int i = 0;

        for(int j = 0; j < serverWorld.players().size(); ++j) {
            ServerPlayer serverplayerentity = serverWorld.players().get(j);
            if (sendParticles(serverplayerentity, false, pPosX, pPosY, pPosZ, sspawnparticlepacket)) {
                ++i;
            }
        }

        return i;
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

    private static boolean sendParticles(ServerPlayer pPlayer, boolean pLongDistance, double pPosX, double pPosY, double pPosZ, CSSpawnParticlePacket packet) {
        if (pPlayer.getLevel().isClientSide()) {
            return false;
        } else {
            BlockPos blockpos = pPlayer.blockPosition();
            if (blockpos.closerThan(new Vec3i((int) pPosX, (int) pPosY, (int) pPosZ), pLongDistance ? 512.0D : 32.0D)) {
                CSNetwork.sendToAll(packet);
                return true;
            } else {
                return false;
            }
        }
    }
}
