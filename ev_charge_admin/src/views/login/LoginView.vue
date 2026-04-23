<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.js'
import { login } from '@/api/admin.js'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const form = ref({ username: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form.value.username, form.value.password)
    if (res.success) {
      userStore.setAdminInfo(res.data)
      userStore.setToken(res.data.id + '')
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="charging-station">
        <div class="charger-body"></div>
        <div class="charger-cable"></div>
        <div class="charger-gun"></div>
      </div>
      <div class="bubbles">
        <span v-for="i in 8" :key="i" :style="{ animationDelay: i * 0.3 + 's' }"></span>
      </div>
    </div>
    <div class="login-box">
      <div class="login-header">
        <div class="logo-icon">
          <el-icon size="48" color="#00c853"><Lightning /></el-icon>
        </div>
        <h1>新能源汽车连锁充电站管理平台</h1>
        <p>EV Charging Station Management System</p>
      </div>
      <el-form :model="form" class="login-form">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="请输入管理员账号"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            <el-icon class="btn-icon"><Lightning /></el-icon>
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <span class="eco-tag">
          <el-icon><FirstAidKit /></el-icon>
          绿色能源 · 智慧充电
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0a1f1a 0%, #0d2b1d 50%, #0a1f14 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.charging-station {
  position: absolute;
  bottom: 10%;
  left: 8%;
  width: 120px;
  height: 200px;
}

.charger-body {
  position: absolute;
  bottom: 0;
  left: 20px;
  width: 80px;
  height: 140px;
  background: linear-gradient(180deg, #1a3a2a 0%, #0d2b1d 100%);
  border-radius: 12px;
  border: 2px solid #00c853;
  box-shadow: 0 0 20px rgba(0, 200, 83, 0.3);
}

.charger-body::before {
  content: '';
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 40px;
  background: #00c853;
  border-radius: 50%;
  box-shadow: 0 0 15px rgba(0, 200, 83, 0.6);
  animation: pulse 2s ease-in-out infinite;
}

.charger-body::after {
  content: '';
  position: absolute;
  top: 75px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 4px;
  background: #00c853;
  border-radius: 2px;
  box-shadow: 0 0 10px rgba(0, 200, 83, 0.5);
}

.charger-cable {
  position: absolute;
  bottom: 40px;
  right: -30px;
  width: 60px;
  height: 8px;
  background: #00c853;
  border-radius: 4px;
  transform: rotate(-30deg);
  box-shadow: 0 0 10px rgba(0, 200, 83, 0.4);
}

.charger-gun {
  position: absolute;
  bottom: 55px;
  right: -50px;
  width: 30px;
  height: 20px;
  background: linear-gradient(90deg, #00c853, #00e676);
  border-radius: 6px;
  box-shadow: 0 0 15px rgba(0, 200, 83, 0.5);
}

@keyframes pulse {
  0%, 100% { transform: translateX(-50%) scale(1); opacity: 1; }
  50% { transform: translateX(-50%) scale(1.1); opacity: 0.8; }
}

.bubbles {
  position: absolute;
  width: 100%;
  height: 100%;
}

.bubbles span {
  position: absolute;
  bottom: -20px;
  width: 8px;
  height: 8px;
  background: rgba(0, 200, 83, 0.4);
  border-radius: 50%;
  animation: rise 6s infinite ease-in;
}

.bubbles span:nth-child(1) { left: 10%; width: 6px; height: 6px; }
.bubbles span:nth-child(2) { left: 20%; width: 10px; height: 10px; animation-duration: 8s; }
.bubbles span:nth-child(3) { left: 35%; width: 5px; height: 5px; animation-duration: 5s; }
.bubbles span:nth-child(4) { left: 50%; width: 12px; height: 12px; animation-duration: 7s; }
.bubbles span:nth-child(5) { left: 65%; width: 6px; height: 6px; animation-duration: 9s; }
.bubbles span:nth-child(6) { left: 80%; width: 8px; height: 8px; animation-duration: 6s; }
.bubbles span:nth-child(7) { left: 90%; width: 4px; height: 4px; animation-duration: 10s; }
.bubbles span:nth-child(8) { left: 45%; width: 7px; height: 7px; animation-duration: 7.5s; }

@keyframes rise {
  0% { bottom: -20px; transform: translateX(0); opacity: 0; }
  10% { opacity: 1; }
  90% { opacity: 0.6; }
  100% { bottom: 100%; transform: translateX(30px); opacity: 0; }
}

.login-box {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  z-index: 10;
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  margin-bottom: 16px;
}

.login-header h1 {
  font-size: 22px;
  color: #1a3a2a;
  margin-bottom: 8px;
  font-weight: 600;
}

.login-header p {
  font-size: 13px;
  color: #888;
}

.login-form .el-input {
  --el-input-focus-border-color: #00c853;
}

.login-btn {
  width: 100%;
  background: linear-gradient(135deg, #00c853, #00e676);
  border: none;
  font-size: 16px;
  height: 44px;
}

.login-btn:hover {
  background: linear-gradient(135deg, #00b248, #00c853);
}

.btn-icon {
  margin-right: 6px;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.eco-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #00c853;
  padding: 6px 16px;
  border: 1px solid rgba(0, 200, 83, 0.3);
  border-radius: 20px;
  background: rgba(0, 200, 83, 0.05);
}
</style>
