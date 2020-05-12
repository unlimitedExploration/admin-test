package com.example.admin.mapper;

import org.apache.ibatis.annotations.Param;

public interface MenuRoleMapper {
    int deleteMenuByRid(Integer rid);

    int addMenusToRole(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
