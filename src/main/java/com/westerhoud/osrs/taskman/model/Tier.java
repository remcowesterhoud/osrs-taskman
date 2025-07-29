package com.westerhoud.osrs.taskman.model;

import java.util.Arrays;
import lombok.Getter;

public enum Tier {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    ELITE("Elite"),
    MASTER("Master"),
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
            case ELITE -> MASTER;
            case MASTER -> PETS;
            case PETS -> PASSIVE;
            case PASSIVE, EXTRA -> EXTRA;
        };
    }

    public static boolean exists(String name) {
        return Arrays.stream(Tier.values())
            .anyMatch(tier -> tier.getName().equalsIgnoreCase(name));
    }
}
