package com.example.admin.mapper;

import com.example.admin.bean.Role;
import com.example.admin.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface UserMapper {
    User loadUserByUsername(String username);

    List<Role> getRolesByUserId(Integer uid);

    User selectByPrimaryKey(Integer id);

    int updatePassword(@Param("encodePwd") String encodePwd,@Param("uid") Integer uid);

    List<User> selectUserByPage(@Param("page") Integer page, @Param("size") Integer size,@Param("user") User user);

    Long getTotal();

    int deleteRoleByUserId(Integer uid);

    int addRolesToUser(@Param("uid") Integer uid,@Param("rids") Integer[] rids);

    int deleteUserById(Integer uid);

    List<User> getAllUser(@Param("currentId") Integer currentId);

    int insertSelective(User user);

    int updateSelective(User user);

    List<User> selectUserByPhone(String phone);

    Integer insertUsers(@Param("list") List<User> list);
}
