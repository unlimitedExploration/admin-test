<template>
  <div>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="用户管理" name="users">
        <Users></Users>
      </el-tab-pane>
      <el-tab-pane label="部门管理" name="department">
        <Department></Department>
      </el-tab-pane>
      <el-tab-pane label="菜单管理" name="menus">
        <Menus></Menus>
      </el-tab-pane>
      <el-tab-pane label="角色管理" name="permission">
        <Permission></Permission>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import Permission from "../../components/sys/Permission"
import Menus from "../../components/sys/Menus"
import Department from "../../components/sys/Department"
import Users from "../../components/sys/Users"
export default {
  name: "SysBasic",
  data() {
    return {
      activeName: "users"
    };
  },
  components: {
      Permission,
      Menus,
      Department,
      Users
  }
};
</script>