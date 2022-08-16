package com.westerhoud.osrs.taskman.model;

public enum Tier {
    EASY,
    MEDIUM,
    HARD,
    ELITE,
    PETS,
    PASSIVE,
    EXTRA;

    public static Tier nextTier(final Tier currentTier) {
        return switch (currentTier) {
            case EASY -> MEDIUM;
            case MEDIUM -> HARD;
            case HARD -> ELITE;
            case ELITE -> PETS;
            case PETS -> PASSIVE;
            case PASSIVE, EXTRA -> EXTRA;
        };
    }
}
