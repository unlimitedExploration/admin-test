package com.example.admin.controller;

import com.example.admin.bean.Menu;
import com.example.admin.bean.User;
import com.example.admin.common.UserUtils;
import com.example.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    MenuService menuService;

    @RequestMapping("/sysmenu")
    public List<Menu> sysmenu() {
        return menuService.getMenusByUserId();
    }

    @RequestMapping("/user")
    public User currentUser(){
        return UserUtils.getCurrentUser();
    }
}
