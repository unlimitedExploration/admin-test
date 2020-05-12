package com.example.admin.mapper;

import com.example.admin.bean.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> roles();

    List<Role> getOrdinaryRole();

    int insert(Role role);

    int deleteByPrimaryKey(Integer rid);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
