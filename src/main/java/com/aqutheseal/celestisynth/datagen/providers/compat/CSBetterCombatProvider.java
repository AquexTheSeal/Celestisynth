package com.aqutheseal.celestisynth.datagen.providers.compat;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.bettercombat.api.AttributesContainer;
import net.bettercombat.api.WeaponAttributes;
import net.minecraft.data.PackOutput;

public class CSBetterCombatProvider extends BaseBetterCombatProvider {
    public CSBetterCombatProvider(PackOutput output) {
        super(output, Celestisynth.MODID);
    }

    @Override
    protected void registerAttributes() {
        this.solarisInit();
        this.crescentiaInit();
        this.breezebreakerInit();
        this.poltergeistInit();
        this.aquafloraInit();
        this.frostboundInit();
    }

    public void frostboundInit() {
        addAttribute(CSItems.FROSTBOUND, new AttributesContainer(
                "bettercombat:claymore",
                new WeaponAttributes(
                        5.0,
                        "bettercombat:pose_two_handed_heavy",
                        null,
                        true,
                        null,
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.1, 150.0, 0.5,
                                        "bettercombat:one_handed_slash_horizontal_right",
                                        new WeaponAttributes.Sound("bettercombat:scythe_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 150.0, 0.5,
                                        "bettercombat:one_handed_slash_horizontal_left",
                                        new WeaponAttributes.Sound("bettercombat:scythe_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.FORWARD_BOX,
                                        1.2, 0, 0.5,
                                        "bettercombat:one_handed_slam",
                                        new WeaponAttributes.Sound("bettercombat:claymore_slam"),
                                        null
                                )
                        }
                )
        ));
    }

    public void aquafloraInit() {
        addAttribute(CSItems.AQUAFLORA, new AttributesContainer(
                "bettercombat:sword",
                new WeaponAttributes(
                        4.0, null, null, false, null,
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.2, 80.0, 0.5,
                                        "bettercombat:two_handed_slash_horizontal_left",
                                        new WeaponAttributes.Sound("bettercombat:katana_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.1, 80.0, 0.5,
                                        "bettercombat:dual_handed_slash_cross",
                                        new WeaponAttributes.Sound("bettercombat:scythe_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.FORWARD_BOX,
                                        1.2, 0.0, 0.5,
                                        "bettercombat:two_handed_stab_left",
                                        new WeaponAttributes.Sound("bettercombat:rapier_stab"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.1, 80.0, 0.5,
                                        "bettercombat:two_handed_slash_horizontal_right",
                                        new WeaponAttributes.Sound("bettercombat:katana_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.FORWARD_BOX,
                                        1.2, 0.0, 0.5,
                                        "bettercombat:two_handed_stab_right",
                                        new WeaponAttributes.Sound("bettercombat:rapier_stab"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 80.0, 0.5,
                                        "bettercombat:dual_handed_slash_cross",
                                        new WeaponAttributes.Sound("bettercombat:scythe_slash"),
                                        null
                                )
                        }
                )
        ));
    }

    public void poltergeistInit() {
        addAttribute(CSItems.POLTERGEIST, new AttributesContainer(
                "bettercombat:sword",
                new WeaponAttributes(
                        3.5, null, null, false, null,
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.FORWARD_BOX,
                                        1.2, 90.0, 0.75,
                                        "bettercombat:two_handed_slam_heavy",
                                        new WeaponAttributes.Sound("bettercombat:mace_slam"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 90.0, 0.6,
                                        "bettercombat:two_handed_slash_horizontal_right",
                                        new WeaponAttributes.Sound("bettercombat:mace_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 90.0, 0.6,
                                        "bettercombat:two_handed_slash_horizontal_left",
                                        new WeaponAttributes.Sound("bettercombat:mace_slash"),
                                        null
                                )
                        }
                )
        ));
    }

    public void breezebreakerInit() {
        addAttribute(CSItems.BREEZEBREAKER, new AttributesContainer(
                "bettercombat:sword",
                new WeaponAttributes(
                        3.5, null, null, false, null,
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 80.0, 0.2,
                                        "bettercombat:two_handed_slash_horizontal_left",
                                        new WeaponAttributes.Sound("bettercombat:katana_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.1, 80.0, 0.2,
                                        "bettercombat:two_handed_slash_horizontal_right",
                                        new WeaponAttributes.Sound("bettercombat:katana_slash"),
                                        null
                                )
                        }
                )
        ));
    }

    public void crescentiaInit() {
        addAttribute(CSItems.CRESCENTIA, new AttributesContainer(
                "bettercombat:sword",
                new WeaponAttributes(
                        4.0, null, null, false, null,
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 90.0, 0.5,
                                        "bettercombat:two_handed_slash_horizontal_left",
                                        new WeaponAttributes.Sound("bettercombat:scythe_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null,
                                        WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.1, 90.0, 0.5,
                                        "bettercombat:two_handed_slash_horizontal_right",
                                        new WeaponAttributes.Sound("bettercombat:scythe_slash"),
                                        null
                                )
                        }
                )
        ));
    }

    public void solarisInit() {
        addAttribute(CSItems.SOLARIS, new AttributesContainer(
                "bettercombat:sword",
                new WeaponAttributes(
                        4.0, null, null, false, null,
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(
                                        null, WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 90.0, 0.5,
                                        "bettercombat:two_handed_slash_horizontal_left",
                                        new WeaponAttributes.Sound("bettercombat:rapier_slash"),
                                        null
                                ),
                                new  WeaponAttributes.Attack(
                                        null, WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 90.0, 0.5,
                                        "bettercombat:two_handed_slash_horizontal_right",
                                        new WeaponAttributes.Sound("bettercombat:rapier_slash"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null, WeaponAttributes.HitBoxShape.FORWARD_BOX,
                                        1.1, 0.0, 0.5,
                                        "bettercombat:two_handed_stab_left",
                                        new WeaponAttributes.Sound("bettercombat:rapier_stab"),
                                        null
                                ),
                                new WeaponAttributes.Attack(
                                        null, WeaponAttributes.HitBoxShape.FORWARD_BOX,
                                        1.1, 0.0, 0.5,
                                        "bettercombat:two_handed_stab_right",
                                        new WeaponAttributes.Sound("bettercombat:rapier_stab"),
                                        null
                                )
                        }
                )
        ));
    }
}
