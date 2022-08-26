package com.westerhoud.osrs.taskman.dto.sheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SheetDto {
    private String key;
    private String passphrase;
}
