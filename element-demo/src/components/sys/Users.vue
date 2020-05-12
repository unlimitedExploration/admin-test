<template>
  <div>
    <div style="display: flex;justify-content: space-between;">
      <div>
        <el-input
          style="width:300px;margin-right:10px;"
          size="small"
          placeholder="请输入手机号，可以直接回车搜索"
          prefix-icon="el-icon-search"
          clearable
          @clear="initUsers"
          v-model="keyword"
          @keydown.enter.native="initUsers"
        ></el-input>
        <el-button icon="el-icon-search" size="small" type="primary" @click="initUsers">搜索</el-button>
      </div>
      <div>
        <el-upload
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-success="onSuccess"
          :on-error="onError"
          :disabled="importDataDisabled"
          action="/sys/user/import"
          style="display: inline-flex;margin-right: 8px"
        >
          <el-button
            :disabled="importDataDisabled"
            type="success"
            size="small"
            :icon="importDataBtnIcon"
          >{{importDataBtnText}}</el-button>
        </el-upload>
        <el-button type="success" icon="el-icon-download" size="small" @click="exportData">导出数据</el-button>
        <el-button type="primary" icon="el-icon-plus" size="small" @click="showAddUserView">添加用户</el-button>
      </div>
    </div>
    <div>
      <el-table :data="users" style="margin-top:20px;width: 100%;" border>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="120"></el-table-column>
        <el-table-column prop="truename" label="真实姓名" width="100"></el-table-column>
        <el-table-column prop="department.name" label="所属部门" width="100"></el-table-column>
        <el-table-column label="用户角色" width="200">
          <template slot-scope="scope">
            <el-tag v-for="(role,index) in scope.row.roles" :key="index" size="mini">{{role.namezh}}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button @click="showEditUserView(scope.row)" size="mini" type="text">编辑</el-button>
            <el-button @click="deleteUser(scope.row)" size="mini" type="text">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
        <el-pagination
          background
          @current-change="currentChange"
          @size-change="sizeChange"
          layout="sizes, prev, pager, next, jumper, ->, total, slot"
          :total="total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog :title="title" :close-on-click-modal="closeOnClickModal" :close-on-press-escape="closeOnPressEscape" :visible.sync="dialogFormVisible">
      <el-form :label-position="labelPosition" :model="user" :rules="rules" ref="userForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="user.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="truename">
          <el-input v-model="user.truename" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="用户角色" prop="roles">
          <el-select v-model="roleids" multiple :multiple-limit="roleLimit" placeholder="请选择用户角色">
            <el-option
              v-for="item in userRoles"
              :key="item.id"
              :label="item.namezh"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门" prop="departmentId">
          <el-cascader
            v-model="user.departmentId"
            :options="allDeps"
            :show-all-levels="false"
            :props="cascaderProps"
            clearable
          ></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "Users",
  data() {
    return {
      loading: false,
      users: [],
      page: 1,
      size: 10,
      total: 0,
      dialogFormVisible: false,
      keyword: "",
      title: "",
      labelPosition: "right",
      user: {
        username: "",
        phone: "",
        truename: "",
        roles: [],
        departmentId: null
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            pattern: "^[a-zA-Z0-9]{2,24}$",
            message: "请使用字母和数字",
            trigger: "blur"
          }
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          {
            pattern: "^1[34578][0-9]{9}$",
            message: "请检查手机号的格式",
            trigger: "blur"
          }
        ],
        truename: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
          {
            pattern: "^[\u4e00-\u9fa5]{2,5}$",
            message: "请使用中文",
            trigger: "blur"
          }
        ],
        departmentId: [
          { required: true, message: "请选择一个部门", trigger: "blur" }
        ]
      },
      userRoles: [],
      roleids: [],
      roleLimit: 2,
      allDeps: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      cascaderProps: {
        //是否严格的遵守父子节点不互相关联
        checkStrictly: true,
        //只返回该节点的值
        emitPath: false,
        value: "id",
        label: "name",
        children: "children"
      },
      importDataBtnText: "批量导入",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      closeOnClickModal: false,
      closeOnPressEscape: false
    };
  },
  mounted() {
    this.initUsers();
    this.initData();
  },
  methods: {
    initUsers() {
      this.loading = true;
      let url =
        "/sys/user/?page=" +
        this.page +
        "&size=" +
        this.size +
        "&phone=" +
        this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.users = resp.data;
          this.total = resp.total;
        }
      });
    },
    initData() {
      if (!window.sessionStorage.getItem("roles")) {
        this.getRequest("/sys/user/roles").then(resp => {
          if (resp) {
            this.userRoles = resp;
            window.sessionStorage.setItem("roles", JSON.stringify(resp));
          }
        });
      } else {
        this.userRoles = JSON.parse(window.sessionStorage.getItem("roles"));
      }
      if (!window.sessionStorage.getItem("deps")) {
        this.getRequest("/sys/user/deps").then(res => {
          if (res) {
            this.allDeps = res;
            window.sessionStorage.setItem("deps", JSON.stringify(res));
          }
        });
      } else {
        this.allDeps = JSON.parse(window.sessionStorage.getItem("deps"));
      }
    },
    resetUser() {
      this.user = {
        username: "",
        phone: "",
        truename: "",
        roles: [],
        departmentId: null
      };
    },
    //页数改变时触发
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initUsers();
    },
    //page改变时触发
    currentChange(val) {
      this.page = val;
      this.initUsers();
    },
    showAddUserView() {
      this.title = "新增用户";
      this.resetUser();
      this.roleids = [];
      this.dialogFormVisible = true;
    },
    showEditUserView(data) {
      this.roleids = [];
      let roles = data.roles;
      for (let index = 0; index < roles.length; index++) {
        this.roleids.push(roles[index].id);
      }
      this.title = "编辑用户";
      this.user = data;
      this.dialogFormVisible = true;
    },
    doAddUser() {
      if (this.user.id) {
        this.$refs["userForm"].validate(valid => {
          if (valid) {
            this.putRequest("/sys/user/?rids=" + this.roleids, this.user).then(
              resp => {
                if (resp) {
                  this.dialogFormVisible = false;
                  this.initUsers();
                }
              }
            );
          }
        });
      } else {
        this.$refs["userForm"].validate(valid => {
          if (valid) {
            this.postRequest("/sys/user/?rids=" + this.roleids, this.user).then(
              resp => {
                if (resp) {
                  this.dialogFormVisible = false;
                  this.initUsers();
                }
              }
            );
          }
        });
      }
    },
    deleteUser(row) {
      this.$confirm("此操作将永久删除该用户信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.deleteRequest("/sys/user/" + row.id).then(resp => {
            if (resp) {
              this.initUsers();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    beforeUpload(file) {
      const extendName = file.name.substring(file.name.lastIndexOf('.')+1);
      const isExcel1 = extendName === 'xls';
      const isExcel2 = extendName === 'xlsx';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isExcel1 && !isExcel2) {
        this.$message.error('请使用excel格式');
      }
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过2M');
      }
      if ((isExcel1 || isExcel2) && isLt2M) {
        this.importDataBtnText = "正在导入";
        this.importDataBtnIcon = "el-icon-loading";
        this.importDataDisabled = true;
      }
      return (isExcel1 || isExcel2) && isLt2M;
    },
    onSuccess() {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.initUsers();
    },
    onError() {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
    },
    exportData() {
      window.open('/sys/user/export','_parent')
    }
  }
};
</script>
<style scoped>
</style>