package com.aqutheseal.celestisynth.common.compat.bettercombat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.bettercombat.api.AttributesContainer;
import net.bettercombat.api.WeaponAttributes;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class BCDatagenUtil implements DataProvider {
    public final Map<String, Pair<RegistryObject<? extends ItemLike>, AttributesContainer>> data = new TreeMap<>();
    public final PackOutput output;
    public final String modid;

    public BCDatagenUtil(PackOutput output, String modid) {
        this.output = output;
        this.modid = modid;
    }

    public void addAttribute(RegistryObject<? extends ItemLike> item, AttributesContainer attribute) {
        data.put(item.getId().getPath(), Pair.of(item, attribute));
    }

    protected abstract void registerAttributes();

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        registerAttributes();
        CompletableFuture<?>[] futures = new CompletableFuture<?>[this.data.size()];
        int i = 0;
        for (Pair<RegistryObject<? extends ItemLike>, AttributesContainer> pairs : this.data.values()) {
            futures[i++] = save(pOutput, pairs.getFirst(), pairs.getSecond());
        }
        return CompletableFuture.allOf(futures);
    }

    protected CompletableFuture<?> save(CachedOutput pOutput, RegistryObject<? extends ItemLike> item, AttributesContainer attribute) {
        Path target = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve("weapon_attributes").resolve(item.getId().getPath() + ".json");
        return DataProvider.saveStable(pOutput, compileAttributeJson(attribute), target);
    }

    protected JsonObject compileAttributeJson(AttributesContainer attribute) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", attribute.parent());
        if (attribute.attributes() != null) {
            JsonObject attributes = new JsonObject();
            if (attribute.attributes().attackRange() != 0) {
                attributes.addProperty("attack_range", attribute.attributes().attackRange());
            }
            if (attribute.attributes().pose() != null) {
                attributes.addProperty("pose", attribute.attributes().pose());
            }
            if (attribute.attributes().offHandPose() != null) {
                attributes.addProperty("off_hand_pose", attribute.attributes().offHandPose());
            }
            if (attribute.attributes().two_handed() != null) {
                attributes.addProperty("two_handed", attribute.attributes().two_handed());
            }
            if (attribute.attributes().category() != null) {
                attributes.addProperty("category", attribute.attributes().category());
            }
            if (attribute.attributes().attacks() != null) {
                JsonArray attacksArray = new JsonArray();
                for (WeaponAttributes.Attack attack : attribute.attributes().attacks()) {
                    attacksArray.add(createAttackJsonArray(attack));
                }
                attributes.add("attacks", attacksArray);
            }
            json.add("attributes", attributes);
        }
        return json;
    }

    private static JsonObject createAttackJsonArray(WeaponAttributes.Attack attack) {
        JsonObject json = new JsonObject();
        JsonArray conditionsArray = new JsonArray();
        if (attack.conditions() != null) {
            for (WeaponAttributes.Condition condition : attack.conditions()) {
                conditionsArray.add(condition.name());
            }
            json.add("conditions", conditionsArray);
        }
        if (attack.hitbox() != null) {
            json.addProperty("hitbox", attack.hitbox().name());
        }
        if (attack.damageMultiplier() != 1.0) {
            json.addProperty("damage_multiplier", attack.damageMultiplier());
        }
        if (attack.damageMultiplier() != 0) {
            json.addProperty("angle", attack.angle());
        }
        if (attack.damageMultiplier() != 0) {
            json.addProperty("upswing", attack.upswing());
        }
        if (attack.animation() != null) {
            json.addProperty("animation", attack.animation());
        }
        if (attack.swingSound() != null) {
            JsonObject swingSound = new JsonObject();
            swingSound.addProperty("id", attack.swingSound().id());
            swingSound.addProperty("volume", attack.swingSound().volume());
            swingSound.addProperty("pitch", attack.swingSound().pitch());
            swingSound.addProperty("randomness", attack.swingSound().randomness());
            json.add("swing_sound", swingSound);
        }
        JsonObject impactSound = new JsonObject();
        if (attack.impactSound() != null) {
            impactSound.addProperty("id", attack.impactSound().id());
            impactSound.addProperty("volume", attack.impactSound().volume());
            impactSound.addProperty("pitch", attack.impactSound().pitch());
            impactSound.addProperty("randomness", attack.impactSound().randomness());
            json.add("impact_sound", impactSound);
        }
        return json;
    }

    @Override
    public String getName() {
        return StringUtils.capitalize(modid) + ": Better Combat Compatibility";
    }
}
