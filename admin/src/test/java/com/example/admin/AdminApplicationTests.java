package com.example.admin;

import com.example.admin.bean.Menu;
import com.example.admin.bean.User;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.RoleService;
import com.example.admin.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AdminApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
    RoleService roleService;

	@Autowired
	UserService userService;

	@Test
    public void testRoles(){
//	    String name = "ROLE_test3";
//	    String namezh = "测试角色3";
//	    int r = roleService.addNewRole(name,namezh);
    }

    @Test
    public void testUser(){
//    	List<String> phone = userService.getAllPhone();
//    	System.out.println("get:"+phone.size()+" phones");
	}

}
