package com.westerhoud.osrs.taskman.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SheetProgress {
    final Map<String, Progress> progressByTier;

    public String getCurrentTier() {
        if (progressByTier.get(Tier.EASY.getName()).isIncomplete()) {
            return Tier.EASY.getName();
        } else if (progressByTier.get(Tier.MEDIUM.getName()).isIncomplete()) {
            return Tier.MEDIUM.getName();
        } else if (progressByTier.get(Tier.HARD.getName()).isIncomplete()) {
            return Tier.HARD.getName();
        } else if (progressByTier.get(Tier.ELITE.getName()).isIncomplete()) {
            return Tier.ELITE.getName();
        }
        return Tier.EXTRA.getName();
    }
}
