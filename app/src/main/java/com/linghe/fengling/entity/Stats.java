package com.linghe.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    private long userId; // 用户id
    private long playerNumber;// 播放次数
    private long musicPlayer; // 播放音乐
    private LocalDateTime time; // 时间

}
