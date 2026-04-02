package com.smartinspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartinspection.entity.SysUser;
import com.smartinspection.mapper.SysUserMapper;
import com.smartinspection.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public boolean login(String username, String password) {
        SysUser user = getByUsername(username);
        if (user == null) {
            return false;
        }
        // TODO: 密码验证（实际应使用 BCrypt 加密）
        return user.getPassword().equals(password);
    }
}
