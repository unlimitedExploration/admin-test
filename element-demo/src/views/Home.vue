<template>
  <div>
    <el-container class="home_container">
      <el-aside width="auto" style="background-color: rgb(238, 241, 246)">
        <el-menu router class="el-menu-vertical-demo" :collapse="isCollapse">
          <template v-for="(item,index) in routes">
            <el-submenu :index="index+''" v-if="!item.hidden" :key="index">
              <template slot="title">
                <i :class="item.iconCls"></i>
                <span slot="title">{{item.name}}</span>
              </template>
              <el-menu-item
                v-for="child in item.children"
                :index="child.path"
                :key="child.path"
              >{{child.name}}</el-menu-item>
            </el-submenu>
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <div class="home_title">
            <el-link style="color:#000;" :underline="false" :icon="changeIcon" @click="changeShow"></el-link>一个测试平台
          </div>
          <div class="home_userinfoContainer">
            <el-dropdown @command="handleCommand">
              <span class="home_userinfo">{{user.truename}}</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="changepwd">修改密码</el-dropdown-item>
                <el-dropdown-item command="logout">注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <el-breadcrumb v-if="this.$router.currentRoute.path!='/home'">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/home'">欢迎！</div>
          <router-view class="homeRouterView"></router-view>
        </el-main>
      </el-container>
    </el-container>
    <!-- 修改密码的dialog -->
    <el-dialog title="修改登录密码" :visible.sync="dialogVisible">
      <el-form :model="pwdForm" :rules="rules" ref="pwdForm">
        <el-form-item label="原密码" prop="oldpwd">
          <el-input type="password" v-model="pwdForm.oldpwd"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newpwd">
          <el-input type="password" v-model="pwdForm.newpwd"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="checkpwd">
          <el-input type="password" v-model="pwdForm.checkpwd"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('pwdForm')">提交</el-button>
          <el-button @click="resetForm('pwdForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    var validatePwd = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.pwdForm.checkpwd !== "") {
          this.$refs.pwdForm.validateField("checkpwd");
        }
        callback();
      }
    };
    var validatePwd2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.pwdForm.newpwd) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      isCollapse: false, //是否折叠
      changeIcon: "el-icon-d-arrow-left",
      dialogVisible: false,
      pwdForm: {
        oldpwd: "",
        newpwd: "",
        checkpwd: ""
      },
      rules: {
        oldpwd: [{ validator: validatePwd, trigger: "blur" }],
        newpwd: [{ validator: validatePwd, trigger: "blur" }],
        checkpwd: [{ validator: validatePwd2, trigger: "blur" }]
      }
    };
  },
  computed: {
    user() {
      return this.$store.state.currentUser;
    },
    routes() {
      return this.$store.state.routes;
    }
  },
  methods: {
    changeShow: function() {
      if (this.isCollapse) {
        this.isCollapse = false;
        this.changeIcon = "el-icon-d-arrow-left";
      } else {
        this.isCollapse = true;
        this.changeIcon = "el-icon-d-arrow-right";
      }
    },
    handleCommand(command) {
      if (command == "logout") {
        this.$confirm("此操作将注销登录,是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.getRequest("/logout");
            window.sessionStorage.removeItem("user");
            this.$store.commit("initRoutes", []);
            this.$router.replace("/");
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消操作"
            });
          });
      } else if (command == "changepwd") {
        this.dialogVisible = true;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.pwdForm.uid = this.user.id;
          this.putRequest("/sys/user/changepwd", this.pwdForm).then(resp => {
            if (resp) {
              this.getRequest("/logout");
              window.sessionStorage.removeItem("user");
              this.$store.commit("initRoutes", []);
              this.$router.replace("/");
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
};
</script>

<style>
.home_container {
  height: 100%;
  width: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
}
.el-header {
  background-color: #5db2ff;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-aside {
  color: #333;
}
.home_title {
  color: #fff;
  font-size: 22px;
  display: inline;
}

.home_userinfo {
  color: #fff;
  cursor: pointer;
}

.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文行楷;
  color: #409eff;
  padding-top: 50px;
}

.homeRouterView {
  margin-top: 10px;
}
</style>