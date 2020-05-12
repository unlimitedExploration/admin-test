package com.example.admin.service;

import com.example.admin.bean.Menu;
import com.example.admin.bean.RespPageBean;
import com.example.admin.common.UserUtils;
import com.example.admin.mapper.MenuMapper;
import com.example.admin.mapper.MenuRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    MenuRoleMapper menuRoleMapper;

//    public final static Logger logger = LoggerFactory.getLogger(MenuService.class);

    /**
     * 获取所有的菜单和关联的角色
     * @return
     */
    public List<Menu> getAllMenus(){
        return menuMapper.getAllMenus();
    }

    /**
     * 获取当前用户的菜单
     * @return
     */
    public List<Menu> getMenusByUserId(){
        return menuMapper.getMenusByUserId(UserUtils.getCurrentUser().getId());
    }

    /**
     * 获取菜单的上下级关系
     * @return
     */
    public List<Menu> menuTree() {
        return menuMapper.menuTree();
    }

    /**
     * 获取和指定角色关联的菜单id
     * @param rid
     * @return
     */
    public List<Integer> getMenusByRid(Integer rid) {
        return menuMapper.getMenusByRid(rid);
    }

    /**
     * 更新指定角色的菜单
     * @param rid
     * @param mids
     * @return
     */
    public boolean updateMenuRole(Integer rid,Integer[] mids){
        menuRoleMapper.deleteMenuByRid(rid);
        if (mids == null || mids.length == 0){
            return true;
        }
        Integer result = menuRoleMapper.addMenusToRole(rid,mids);
        return result == mids.length;
    }

    /**
     * 获取指定菜单的子菜单
     * @param mid
     * @return
     */
    public List<Menu> getSubmenu(Integer mid){
        return menuMapper.selectSubmenu(mid);
    }

    /**
     * 获取所有一级菜单(带分页）
     * @return
     */
    public RespPageBean getMenusByPage(Integer page, Integer size){
        if (page != null && size != null){
            page = (page-1)*size;
        }
        List<Menu> menus = menuMapper.selectMenuWithChildren(page,size);
        Long total = menuMapper.getTotal();
        RespPageBean res = new RespPageBean();
        res.setData(menus);
        res.setTotal(total);
        return res;
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    public Integer addMenu(Menu menu){
        return menuMapper.insertSelective(menu);
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    public Integer updateMenu(Menu menu){
        return menuMapper.updateByPrimaryKeySelective(menu);
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    public Integer deleteMenu(Integer id){
        return menuMapper.deleteByPrimaryKey(id);
    }

}
