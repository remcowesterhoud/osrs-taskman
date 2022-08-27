package com.westerhoud.osrs.taskman.dto.sheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressDto {
    final int maxValue;
    final int value;

    @JsonIgnore
    public boolean isIncomplete() {
        return value != maxValue;
    }
}
