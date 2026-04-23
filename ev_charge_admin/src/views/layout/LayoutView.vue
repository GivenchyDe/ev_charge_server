<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isCollapse = ref(false)

const menuList = [
  { path: '/', icon: 'Odometer', title: '首页概览' },
  { path: '/user', icon: 'User', title: '用户管理' },
  { path: '/station', icon: 'OfficeBuilding', title: '网点管理' },
  { path: '/charger', icon: 'SetUp', title: '充电桩管理' },
  { path: '/gun', icon: 'Connection', title: '充电枪管理' },
  { path: '/billing', icon: 'Money', title: '计费规则' },
  { path: '/order', icon: 'Document', title: '充电订单' },
  { path: '/recharge', icon: 'Wallet', title: '充值订单' },
  { path: '/review', icon: 'Star', title: '订单评价' },
  { path: '/feedback', icon: 'Warning', title: '故障反馈' },
  { path: '/announcement', icon: 'Bell', title: '公告管理' },
  { path: '/message', icon: 'Message', title: '消息通知' },
  { path: '/chat', icon: 'ChatDotRound', title: '客服聊天' },
  { path: '/admin', icon: 'UserFilled', title: '管理员' },
]

const handleLogout = () => {
  ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' }).then(() => {
    userStore.logout()
    ElMessage.success('已退出')
    router.push('/login')
  })
}
</script>

<template>
  <el-container class="layout">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="aside">
      <div class="logo">
        <el-icon size="28" color="#00c853"><Lightning /></el-icon>
        <span v-if="!isCollapse" class="logo-text">EV充电管理</span>
      </div>
      <el-menu
        :default-active="route.path"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#0d2b1d"
        text-color="#b0c4b1"
        active-text-color="#00e676"
      >
        <el-menu-item v-for="item in menuList" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="breadcrumb">{{ route.meta?.title || '首页概览' }}</span>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-icon size="18"><UserFilled /></el-icon>
              {{ userStore.adminInfo?.name || userStore.adminInfo?.username || '管理员' }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  height: 100vh;
}

.aside {
  background: #0d2b1d;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid rgba(0, 200, 83, 0.15);
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #00e676;
  letter-spacing: 2px;
}

.aside .el-menu {
  border-right: none;
}

.aside .el-menu-item:hover {
  background: rgba(0, 200, 83, 0.1) !important;
}

.aside .el-menu-item.is-active {
  background: rgba(0, 200, 83, 0.15) !important;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 5;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.collapse-btn:hover {
  color: #00c853;
}

.breadcrumb {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #333;
  font-size: 14px;
}

.main {
  background: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}
</style>
