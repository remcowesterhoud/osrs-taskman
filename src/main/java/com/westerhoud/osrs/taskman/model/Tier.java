package com.westerhoud.osrs.taskman.model;

import lombok.Getter;

public enum Tier {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    ELITE("Elite"),
    PETS("Pets"),
    PASSIVE("Passive"),
    EXTRA("Extra");

    @Getter
    private final String name;
    Tier(final String name) {
        this.name = name;
    }

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
