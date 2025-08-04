package org.thecelestialworkshop.celestisynth.common.entity.base;

public interface FixedMovesetEntity {

    void setAction(int action);

    int getAction();

    default void resetAction() {
        this.setAction(0);
    }

    default int getDefaultAction() {
        return 0;
    }

    default boolean hasNoAction() {
        return this.getAction() == this.getDefaultAction();
    }

    int getAnimationTick();

    void setAnimationTick(int tick);

    default void incrementAnimationTick() {
        setAnimationTick(getAnimationTick() + 1);
    }

    default void resetAnimationTick() {
        setAnimationTick(0);
    }
}
