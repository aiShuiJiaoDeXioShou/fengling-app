package com.linghe.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.litepal.crud.LitePalSupport;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting extends LitePalSupport {
    private long id;
    // 主题
    private int theme;
    // 语言
    private String lang;
    private long userId;
}
