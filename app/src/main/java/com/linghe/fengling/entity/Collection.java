package com.linghe.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.litepal.crud.LitePalSupport;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection extends LitePalSupport {
    private long id;
    private long songId;
    private int songIndex;
    private long userId;
}
