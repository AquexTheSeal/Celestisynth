package com.aqutheseal.celestisynth.item.helpers;

public class CSArmorProperties {

    public final String identifier;
    public boolean stunImmune;
    public double damageReflectionPercent;
    public double damageReflectionAddition;
    public double mobEffectDurationMultiplier;
    public double skillDamageMultiplier;

    public CSArmorProperties(String identifier) {
        this.identifier = identifier;
    }

    public CSArmorProperties setStunImmunity(boolean stunImmune) {
        this.stunImmune = stunImmune;
        return this;
    }

    public CSArmorProperties setDamageReflectionPercent(double damageReflectionPercent) {
        this.damageReflectionPercent = damageReflectionPercent;
        return this;
    }

    public CSArmorProperties setDamageReflectionAddition(double damageReflectionAddition) {
        this.damageReflectionAddition = damageReflectionAddition;
        return this;
    }

    public CSArmorProperties setMobEffectDurationMultiplier(double mobEffectDurationMultiplier) {
        this.mobEffectDurationMultiplier = mobEffectDurationMultiplier;
        return this;
    }

    public CSArmorProperties setSkillDamageMultiplier(double skillDamageAddMultiplier) {
        this.skillDamageMultiplier = skillDamageAddMultiplier;
        return this;
    }
}
