package com.niklasekman.tutorialmod.thirst;

import net.minecraft.nbt.CompoundTag;

public class PlayerThirst {
    private int thirst;
    private final int MIN_THIRST = 0;
    private final int MAX_THIRST = 10;

    public int getThirst() {
        return thirst;
    }

    public void addThirst(int thirstAmount) {
        this.thirst = Math.min(thirst + thirstAmount, MAX_THIRST);
    }

    public void removeThirst(int thirstAmount) {
        this.thirst = Math.max(thirst - thirstAmount, MIN_THIRST);
    }

    public void copyFrom(PlayerThirst source) {
        this.thirst = source.thirst;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("thirst", thirst);
    }
    public void loadNBTData(CompoundTag nbt) {
        thirst = nbt.getInt("thirst");
    }
}
