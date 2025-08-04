package org.thecelestialworkshop.celestisynth.common.item.misc;

import org.thecelestialworkshop.celestisynth.common.entity.helper.MonolithRunes;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class StarMonolithItem extends Item {
    public final MonolithRunes rune;

    public StarMonolithItem(Properties pProperties, MonolithRunes rune) {
        super(pProperties);
        this.rune = rune;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            StarMonolith monolith = CSEntityTypes.STAR_MONOLITH.get().create(pContext.getLevel());
            monolith.setRune(rune);
            monolith.moveTo(pContext.getClickedPos().above(), 0, 0);
            pContext.getLevel().addFreshEntity(monolith);
        }
        if (!pContext.getPlayer().isCreative()) {
            pContext.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
