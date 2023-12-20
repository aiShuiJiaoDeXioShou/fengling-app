package com.linghe.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.litepal.crud.LitePalSupport;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theme extends LitePalSupport {
    private long id;
    private String themeName;
    private int themeValue;
}
