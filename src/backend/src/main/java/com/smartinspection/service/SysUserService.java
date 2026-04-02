package com.smartinspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartinspection.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    
    SysUser getByUsername(String username);
    
    boolean login(String username, String password);
}
