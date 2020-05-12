package com.example.admin.common;

import com.example.admin.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
