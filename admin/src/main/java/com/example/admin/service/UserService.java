package com.example.admin.service;

import com.example.admin.bean.*;
import com.example.admin.mapper.UserMapper;
import com.example.admin.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User u = userMapper.loadUserByUsername(s);
        if (u == null){
            throw new UsernameNotFoundException("用户名不对");
        }
        u.setRoles(userMapper.getRolesByUserId(u.getId()));
        return u;
    }

    /**
     * 修改登录密码
     * @param oldpwd
     * @param newpwd
     * @param uid
     * @return
     */
    public boolean updateUserPwd(String oldpwd,String newpwd,Integer uid){
        User user = userMapper.selectByPrimaryKey(uid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpwd,user.getPassword())){
            String encodePwd = encoder.encode(newpwd);
            Integer r = userMapper.updatePassword(encodePwd,uid);
            if (r == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取用户
     * @param page
     * @param size
     * @param user
     * @return
     */
    public RespPageBean getUserByPage(Integer page,Integer size,User user){
        if (page != null && size != null){
            page = (page-1)*size;
        }
        RespPageBean res = new RespPageBean();
        List<User> users = userMapper.selectUserByPage(page,size,user);
        Long total = userMapper.getTotal();
        res.setData(users);
        res.setTotal(total);
        return res;
    }

    /**
     * 新增用户
     * @param user
     * @param rids
     * @return
     */
    public int addUser(User user,Integer[] rids){
        //设置默认的登录密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("123456"));
        //设置创建时间
        user.setCreateTime(new Date());
        if (userMapper.insertSelective(user) == 1){
            //添加用户后，在 user_role 表中添加用户和角色的关联
            Integer uid = user.getId();
            int r = userMapper.addRolesToUser(uid,rids);
            if (r == rids.length){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 修改用户信息
     * @param user
     * @param rids
     * @return
     */
    public int updateUser(User user,Integer[] rids){
        Integer uid = user.getId();
        user.setUpdateTime(new Date());
        if (userMapper.updateSelective(user) == 1){
            int deleted = userMapper.deleteRoleByUserId(uid);
            int r = userMapper.addRolesToUser(uid,rids);
            if (r == rids.length){
                return 1;
            }
        }
        return 0;
    }

    public int deleteUser(Integer uid){
        int deleted = userMapper.deleteRoleByUserId(uid);
        return userMapper.deleteUserById(uid);
    }

    public List<User> getAllUser(){
        return userMapper.getAllUser(null);
    }

    /**
     * 判断手机号在系统中是否已经存在
     * @param phone
     * @return
     */
    public boolean phoneExisted(String phone) {
        List<User> u = userMapper.selectUserByPhone(phone);
        if (u.size() > 0){
            return true;
        }
        return false;
    }

    public Integer addUsers(List<User> list){
        List<String> existedPhone = new ArrayList<>();
        for (User user:list){
            String phone = user.getPhone();
            if (phoneExisted(phone)){
                existedPhone.add(phone);
            }
        }
        if (existedPhone.size() == 0){
            return userMapper.insertUsers(list);
        } else {
            return -1;
        }
    }

}
