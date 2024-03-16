package com.aqutheseal.celestisynth.api.item;

public enum AttackHurtTypes {
    REGULAR(true, false),
    NO_KB(false, false),
    RAPID(true, true),
    RAPID_NO_KB(false, true);

    public final boolean doKnockback;
    public final boolean isRapid;

    AttackHurtTypes(boolean doKnockback, boolean isRapid) {
        this.doKnockback = doKnockback;
        this.isRapid = isRapid;
    }

    public boolean doKnockback() {
        return doKnockback;
    }

    public boolean isRapid() {
        return isRapid;
    }
}
