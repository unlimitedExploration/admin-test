<template>
  <el-form
    :model="loginForm"
    :rules="rules"
    ref="loginForm"
    class="login-container"
    label-position="left"
    label-width="0px"
    v-loading="loading"
  >
    <h3 class="login_title">系统登录</h3>
    <el-form-item prop="username">
      <el-input type="text" v-model="loginForm.username" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="loginForm.password" placeholder="登录密码"></el-input>
    </el-form-item>
    <el-form-item prop="code">
      <el-input
        size="normal"
        type="text"
        v-model="loginForm.code"
        auto-complete="off"
        placeholder="点击图片更换验证码"
        @keydown.enter.native="submitLogin"
        style="width: 250px"
      ></el-input>
      <img :src="vcUrl" @click="updateVerifyCode" alt />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.native.prevent="submitLogin">登录</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  data() {
    return {
      vcUrl: "/verifyCode?time=" + new Date(),
      loginForm: {
        username: "",
        password: "",
        code: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }]
      },
      loading: false
    };
  },
  methods: {
    updateVerifyCode() {
      this.vcUrl = "/verifyCode?time=" + new Date();
    },
    submitLogin: function() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.postKeyValueRequest("/doLogin", this.loginForm).then(resp => {
            this.loading = false;
            if (resp) {
              this.$store.commit("initCurrentUser", resp.obj);
              window.sessionStorage.setItem("user", JSON.stringify(resp.obj));
              let path = this.$route.query.redirect;
              this.$router.replace(
                path == "/" || path == undefined ? "/home" : path
              );
            } else {
              this.vcUrl = "/verifyCode?time=" + new Date();
            }
          });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style>
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}

.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>