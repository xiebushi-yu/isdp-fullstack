<template>
  <div class="loginWrap">
    <el-form ref="formRef" :model="loginForm" :rules="loginRules" class="login-form">
      <img src="@/assets/logo.svg" class="logo" /><span class="title">ISDP-Vue3</span>
      <el-form-item prop="userName">
        <el-input
          v-model="loginForm.userName"
          type="text"
          auto-complete="off"
          placeholder="用户名"
          :prefix-icon="Iphone"
        >
        </el-input>
      </el-form-item>
      <div>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            :prefix-icon="Lock"
            maxlength="16"
            show-password
          >
          </el-input>
        </el-form-item>
        <div>
          <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
        </div>
      </div>
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="large"
          type="success"
          style="width: 100%; margin-top: 20px"
          @click="handleLoginByStore"
        >
          <span>登 录</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Iphone, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/user'
import { ElNotification, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()

const router = useRouter()
const formRef = ref()

onMounted(() => {})

// 登录数据
const loginForm = reactive({
  userName: 'admin',
  password: '123456',
  rememberMe: true,
})

// 登录表单校验
const loginRules = reactive({
  userName: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
  password: [{ required: true, trigger: 'blur', min: 6, message: '请输入您的密码' }],
})

// 是否正在登录
const loading = ref(false)

// 登录按钮
const handleLogin = () => {
  loading.value = true
  let rules: any = []
  rules = ['userName', 'password']
  formRef.value.validateField(rules, (valid: any) => {
    if (valid) {
      const loginUser = {
        userName: loginForm.userName,
        password: loginForm.password,
      }
      login(loginUser).then((response: any) => {
        if (response.code === 200) {
          router.push('/')
          ElMessage({
            type: 'success',
            message: response.msg,
          })
        } else {
          loading.value = false
          ElNotification({
            type: 'error',
            message: response.msg,
          })
        }
      })
    } else {
      loading.value = false
    }
  })
}

const handleLoginByStore = () => {
  loading.value = true
  let rules: any = []
  rules = ['userName', 'password']
  formRef.value.validateField(rules, (valid: any) => {
    if (valid) {
      const loginUser = {
        userName: loginForm.userName,
        password: loginForm.password,
      }
      userStore
        .userLogin(loginUser)
        .then(() => {
          router.push('/')
          ElMessage({
            type: 'success',
            message: '登录成功',
          })
        })
        .catch(() => {
          loading.value = false
        })
    } else {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.loginWrap {
  display: flex;
  width: 100%;
  height: 100vh;
  justify-content: center;
  align-items: center;
  background-size: cover;
  background-image: linear-gradient(to right top, #4c7cc3, #00a1ba, #00bf98, #36c486);

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;
    box-sizing: border-box;
    margin-bottom: 150px;

    :deep(.el-form-item__content) {
      justify-content: space-between !important;
    }

    .el-input {
      height: 38px;

      input {
        height: 38px;
      }
    }

    .loginType {
      display: flex;
      justify-content: end;
      font-size: 14px;
      color: #606266;
      margin: 0 0 20px 0;
    }

    .forgot {
      font-size: 14px;
      vertical-align: top;
      margin-top: 6px;
      color: #606266;
    }

    .logo {
      height: 40px;
      margin-left: 50px;
    }

    .title {
      /* 上-右-下-左 */
      margin: 0px 20px 60px 20px;
      text-align: center;
      color: #36c486;
      font-size: 30px;
    }
  }
}
</style>
