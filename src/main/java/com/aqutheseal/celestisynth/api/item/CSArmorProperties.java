package com.aqutheseal.celestisynth.api.item;

public class CSArmorProperties {
    private final String identifier;
    private boolean stunImmune;
    private double damageReflectionPercent;
    private double damageReflectionAddition;
    private double mobEffectDurationMultiplier;
    private double skillDamageMultiplier;

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

    public String getIdentifier() {
        return identifier;
    }

    public boolean isStunImmune() {
        return stunImmune;
    }

    public double getDamageReflectionPercent() {
        return damageReflectionPercent;
    }

    public double getDamageReflectionAddition() {
        return damageReflectionAddition;
    }

    public double getMobEffectDurationMultiplier() {
        return mobEffectDurationMultiplier;
    }

    public double getSkillDamageMultiplier() {
        return skillDamageMultiplier;
    }

    public String toString() {
        return "CSArmorProperties{" +
                "identifier='" + identifier + '\'' +
                ", stunImmune=" + stunImmune +
                ", damageReflectionPercent=" + damageReflectionPercent +
                ", damageReflectionAddition=" + damageReflectionAddition +
                ", mobEffectDurationMultiplier=" + mobEffectDurationMultiplier +
                ", skillDamageMultiplier=" + skillDamageMultiplier +
                '}';
    }
}
