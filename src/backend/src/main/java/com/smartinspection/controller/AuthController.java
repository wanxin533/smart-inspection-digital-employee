package com.smartinspection.controller;

import com.smartinspection.entity.SysUser;
import com.smartinspection.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        boolean success = userService.login(username, password);
        
        Map<String, Object> result = new HashMap<>();
        if (success) {
            SysUser user = userService.getByUsername(username);
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", Map.of(
                "token", "mock-jwt-token-" + System.currentTimeMillis(),
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "nickname", user.getNickname()
                )
            ));
        } else {
            result.put("code", 401);
            result.put("message", "用户名或密码错误");
        }
        
        return result;
    }

    @GetMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "退出成功");
        return result;
    }
}
