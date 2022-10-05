package com.westerhoud.osrs.taskman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TierProgress {
  final int maxValue;
  final int value;

  @JsonIgnore
  public boolean isIncomplete() {
    return value != maxValue;
  }
}
