package org.thecelestialworkshop.celestisynth.common.item.misc;

import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.entity.helper.MonolithRunes;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.RainfallTurret;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CelestialDebuggerItem extends Item implements CSWeaponUtil {
    public CelestialDebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        DoubleArrayList angles = new DoubleArrayList();
        angles.add(0);
        if (!pPlayer.isShiftKeyDown()) {
            if (!pLevel.isClientSide) {
                StarMonolith monolith = CSEntityTypes.STAR_MONOLITH.get().create(pLevel);
                monolith.setRune(MonolithRunes.APOCALYPTIC_RUNE);
                monolith.moveTo(pPlayer.position());
                pLevel.addFreshEntity(monolith);
            }
        } else {
            if (!pLevel.isClientSide()) {
                for (int i = 0; i < 360; i = i + 40) {
                    RainfallTurret turret = new RainfallTurret(CSEntityTypes.RAINFALL_TURRET.get(), pLevel);
                    turret.moveTo(pPlayer.getX() + (Mth.sin(i) * 30), pPlayer.getY(), pPlayer.getZ() + (Mth.cos(i) * 30));
                    turret.setOwnerUUID(pPlayer.getUUID());
                    pLevel.addFreshEntity(turret);
                }
            }
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pTarget instanceof RainfallTurret turret) {
            turret.setTarget(pAttacker);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
