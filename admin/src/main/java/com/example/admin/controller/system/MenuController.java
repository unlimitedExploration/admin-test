package com.example.admin.controller.system;

import com.example.admin.bean.Menu;
import com.example.admin.bean.RespBean;
import com.example.admin.bean.RespPageBean;
import com.example.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("/")
    public RespPageBean getMenusByPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer size){
        return menuService.getMenusByPage(page,size);
    }

    @GetMapping("/submenu/{mid}")
    public List<Menu> getSubmenu(@PathVariable Integer mid){
        return menuService.getSubmenu(mid);
    }

    @PostMapping("/")
    public RespBean addMenu(@RequestBody Menu menu){
        if (menuService.addMenu(menu) == 1){
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateMenu(@RequestBody Menu menu){
        if (menuService.updateMenu(menu) == 1){
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteMenu(@PathVariable Integer id){
        if (menuService.deleteMenu(id) == 1){
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
