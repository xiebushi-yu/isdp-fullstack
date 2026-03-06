package edu.scau.mis.web.controller;

import edu.scau.mis.common.web.domain.ApiResult;
import edu.scau.mis.pos.controller.dto.LoginUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    public static final ConcurrentHashMap<String, String> USERS = new ConcurrentHashMap<>();
    static {
        USERS.put("admin", "123456");
        USERS.put("user", "123456");
    }

    @PostMapping("/login")
    public ApiResult<LoginUser> login(@RequestBody LoginUser user){
        String password = USERS.get(user.getUserName());
        if(user.getPassword().equals(password)){
            user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            user.setToken(user.getUserName()+"_token");
            return ApiResult.success(user);
        } else {
            return new ApiResult<>(401, "认证失败，无法访问系统资源");
        }
    }
}