package com.example.admin.mapper;

import com.example.admin.bean.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {

    int deleteByRid(Integer rid);

    int deleteByUid(Integer uid);

    Integer addRolesToUser(@Param("uid") Integer uid,@Param("rids") Integer[] rids);

    List<UserRole> selectByRid(Integer rid);
}
