package com.westerhoud.osrs.taskman.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Deprecated(forRemoval = true)
public class SheetCredentials {
  private String key;
  private String passphrase;

  public Credentials toCredentials() {
    return new Credentials(key, passphrase);
  }
}
