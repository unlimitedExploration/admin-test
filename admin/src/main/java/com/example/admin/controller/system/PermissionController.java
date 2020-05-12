package com.example.admin.controller.system;

import com.example.admin.bean.Menu;
import com.example.admin.bean.RespBean;
import com.example.admin.bean.Role;
import com.example.admin.service.MenuService;
import com.example.admin.service.RoleService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理
 */
@RestController
@RequestMapping("/sys/permission")
public class PermissionController {
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    //获取所有的角色
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.roles();
    }

    //获取所有菜单分级信息
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.menuTree();
    }

    //根据角色id获取对应的菜单id
    @GetMapping("/mids/{rid}")
    public List<Integer> getMidsByRid(@PathVariable Integer rid){
        return menuService.getMenusByRid(rid);
    }

    //更新角色对应的菜单信息
    @PutMapping("/")
    public RespBean updateRoleMenus(Integer rid,Integer[] mids){
        if(menuService.updateMenuRole(rid,mids)){
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败");
    }

    //添加角色
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        if (roleService.addNewRole(role) == 1){
            return RespBean.ok("添加成功");
        }
        return RespBean.error("添加失败");
    }
    //删除角色
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid){
        if (roleService.deleteRoleById(rid) == 1) {
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }

}
