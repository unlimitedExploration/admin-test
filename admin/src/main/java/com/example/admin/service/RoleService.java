package com.example.admin.service;

import com.example.admin.bean.Role;
import com.example.admin.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {
    @Autowired
    RoleMapper roleMapper;

    /**
     * 获取所有的角色
     * @return
     */
    public List<Role> roles() {
        return roleMapper.roles();
    }

    public List<Role> ordinaryRoles() {
        return roleMapper.getOrdinaryRole();
    }

    /**
     * 插入一个角色
     * @param role
     * @return
     */
    public int addNewRole(Role role){
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        return roleMapper.insert(role);
    }

    /**
     * 删除一个角色
     * @param rid
     * @return
     */
    public int deleteRoleById(Integer rid) {
        return roleMapper.deleteByPrimaryKey(rid);
    }
}
