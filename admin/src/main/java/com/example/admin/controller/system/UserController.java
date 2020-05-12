package com.example.admin.controller.system;

import com.example.admin.bean.*;
import com.example.admin.common.POIUtils;
import com.example.admin.service.DepartmentService;
import com.example.admin.service.RoleService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    DepartmentService departmentService;

    @PutMapping("/changepwd")
    public RespBean updatePwd(@RequestBody Map<String,Object> info){
        String oldpwd = (String) info.get("oldpwd");
        String newpwd = (String) info.get("newpwd");
        Integer uid = (Integer) info.get("uid");

        if (userService.updateUserPwd(oldpwd,newpwd,uid)){
            return RespBean.ok("更新成功");
        }else {
            return RespBean.error("更新失败");
        }
    }

    @GetMapping("/")
    public RespPageBean getUserByPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer size,User user){
        return userService.getUserByPage(page,size,user);
    }

    @GetMapping("/roles")
    public List<Role> getRoles(){
        return roleService.ordinaryRoles();
    }

    @GetMapping("/deps")
    public List<Department> getDepartments(){
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    public RespBean addUser(@RequestBody User user,@RequestParam("rids") Integer[] rids){
        if (!userService.phoneExisted(user.getPhone())){
            if (userService.addUser(user, rids) == 1) {
                return RespBean.ok("添加成功");
            }
            return RespBean.error("添加失败");
        } else {
            return RespBean.error("系统中已存在相同的手机号");
        }
    }

    @PutMapping("/")
    public RespBean updateUser(@RequestBody User user,@RequestParam("rids") Integer[] rids){
        if (userService.updateUser(user,rids) == 1){
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id) == 1){
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @PostMapping("/import")
    public RespBean importUser(MultipartFile file){
        List<User> users = POIUtils.excelToUsers(file);
        if (userService.addUsers(users) == users.size()){
            return RespBean.ok("导入成功");
        }
        return RespBean.error("导入失败");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportUser(){
        List<User> users = (List<User>) userService.getUserByPage(null,null,new User()).getData();
        return POIUtils.generateUserExcel(users);
    }
}
