package com.westerhoud.osrs.taskman.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandData {
  private Task task;
  private String tier;
  private int progressPercentage;
}
