package org.m.common.entity.dto;


import lombok.Data;

@Data
public class UserDto {
    private String userName;
    private String password;
    private String code;
    private String newPassword;
    private String oldPassword;
}
