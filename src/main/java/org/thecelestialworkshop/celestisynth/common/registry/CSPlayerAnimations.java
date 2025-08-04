package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSPlayerAnimations {
    public static final ResourceKey<Registry<PlayerAnimationContainer>> ANIMATIONS_KEY = ResourceKey.createRegistryKey(Celestisynth.prefix("cs_player_animations"));

    public static final DeferredRegister<PlayerAnimationContainer> ANIMATIONS = DeferredRegister.create(ANIMATIONS_KEY, Celestisynth.MODID);

    public static final RegistryObject<PlayerAnimationContainer> CLEAR = addAnimation("cs_clear");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_SOLARIS_SPIN = addAnimation("cs_solaris_spin");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_CRESCENTIA_STRIKE = addAnimation("cs_crescentia_strike");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_CRESCENTIA_THROW = addAnimation("cs_crescentia_throw");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_NORMAL_SINGLE = addAnimation("cs_breezebreaker_normal_single");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_NORMAL_DOUBLE = addAnimation("cs_breezebreaker_normal_double");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_SHIFT_RIGHT = addAnimation("cs_breezebreaker_shift_right");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_SHIFT_LEFT = addAnimation("cs_breezebreaker_shift_left");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_JUMP = addAnimation("cs_breezebreaker_jump");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_JUMP_ATTACK = addAnimation("cs_breezebreaker_jump_attack");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_BREEZEBREAKER_SPRINT_ATTACK = addAnimation("cs_breezebreaker_sprint_attack");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_POLTERGEIST_SMASH = addAnimation("cs_poltergeist_smash");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_POLTERGEIST_RETREAT = addAnimation("cs_poltergeist_retreat");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_AQUAFLORA_PIERCE_RIGHT = addAnimation("cs_aquaflora_pierce_right");
    //public static final RegistryObject<PlayerAnimationContainer> ANIM_AQUAFLORA_PIERCE_LEFT = addAnimation("cs_aquaflora_pierce_left");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_AQUAFLORA_BASH = addAnimation("cs_aquaflora_bash");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_AQUAFLORA_ASSASSINATE = addAnimation("cs_aquaflora_assassinate");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_RAINFALL_AIM_LEFT = addAnimation("cs_rainfall_aim_left");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_RAINFALL_AIM_RIGHT = addAnimation("cs_rainfall_aim_right");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_FROSTBOUND_TRIPLE_SLASH = addAnimation("cs_frostbound_triple_slash");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_FROSTBOUND_CRYOGENESIS = addAnimation("cs_frostbound_cryogenesis");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_KERES_CHARGE = addAnimation("cs_keres_charge");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_KERES_SMASH = addAnimation("cs_keres_smash");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_KERES_SLASH = addAnimation("cs_keres_slash");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_KERES_SLASH_1 = addAnimation("cs_keres_slash_1");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_KERES_SLASH_2 = addAnimation("cs_keres_slash_2");
    public static final RegistryObject<PlayerAnimationContainer> ANIM_KERES_REND = addAnimation("cs_keres_rend");

    public static RegistryObject<PlayerAnimationContainer> addAnimation(String id) {
        return addAnimation(Celestisynth.prefix(id));
    }

    public static RegistryObject<PlayerAnimationContainer> addAnimation(ResourceLocation id) {
        return ANIMATIONS.register(id.getPath(), () -> new PlayerAnimationContainer(id));
    }
}
