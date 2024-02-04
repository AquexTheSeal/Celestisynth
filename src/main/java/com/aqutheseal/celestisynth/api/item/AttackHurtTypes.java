package com.aqutheseal.celestisynth.api.item;

public enum AttackHurtTypes {
    REGULAR(true, true, false),
    NO_KB(false, true, false),
    NO_KB_PIERCE(false, true, false),
    RAPID(false, true, true),
    RAPID_PIERCE(false, false, true);

    public final boolean doKnockback;
    public final boolean isBlockable;
    public final boolean isRapid;

    AttackHurtTypes(boolean doKnockback, boolean isBlockable, boolean isRapid) {
        this.doKnockback = doKnockback;
        this.isBlockable = isBlockable;
        this.isRapid = isRapid;
    }

    public boolean doKnockback() {
        return doKnockback;
    }

    public boolean isBlockable() {
        return isBlockable;
    }

    public boolean isRapid() {
        return isRapid;
    }
}
