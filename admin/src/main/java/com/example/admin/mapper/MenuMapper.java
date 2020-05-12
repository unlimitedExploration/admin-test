package com.example.admin.mapper;

import com.example.admin.bean.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper {
    List<Menu> getAllMenus();

    List<Menu> getMenusByUserId(Integer uid);

    List<Menu> menuTree();

    List<Integer> getMenusByRid(Integer rid);

    List<Menu> selectSubmenu(Integer mid);

    List<Menu> selectMenuWithChildren(@Param("page") Integer page,@Param("size") Integer size);

    Long getTotal();

    int insertSelective(Menu menu);

    int updateByPrimaryKeySelective(Menu menu);

    int deleteByPrimaryKey(Integer id);
}
