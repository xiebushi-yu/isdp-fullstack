package edu.scau.mis.pos.controller.dto;

import lombok.Data;

@Data
public class LoginUser {
    private String avatar;
    private String userName;
    private String password;
    private String token;
}