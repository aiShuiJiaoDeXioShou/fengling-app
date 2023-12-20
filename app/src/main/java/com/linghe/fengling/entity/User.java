package com.linghe.fengling.entity;

import lombok.EqualsAndHashCode;
import org.litepal.crud.LitePalSupport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends LitePalSupport {
    private String userName;
    private String userNumber;
    private String userPassword;
    private String userSex;
    private String userStatus;
    private String userEmail;
    private long id;
    private int isLogin;
}
