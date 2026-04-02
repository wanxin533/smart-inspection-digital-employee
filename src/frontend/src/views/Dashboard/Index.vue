<template>
  <div class="dashboard-container">
    <el-container>
      <el-aside width="200px">
        <div class="logo">智能巡视系统</div>
        <el-menu :default-active="activeMenu" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
          <el-menu-item index="/">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/inspection">
            <el-icon><Document /></el-icon>
            <span>巡视管理</span>
          </el-menu-item>
          <el-menu-item index="/defect">
            <el-icon><Warning /></el-icon>
            <span>缺陷管理</span>
          </el-menu-item>
          <el-menu-item index="/work-order">
            <el-icon><List /></el-icon>
            <span>工单管理</span>
          </el-menu-item>
          <el-menu-item index="/predictive">
            <el-icon><TrendCharts /></el-icon>
            <span>预测性维护</span>
          </el-menu-item>
          <el-menu-item index="/knowledge">
            <el-icon><Reading /></el-icon>
            <span>知识图谱</span>
          </el-menu-item>
          <el-menu-item index="/scenarios">
            <el-icon><Grid /></el-icon>
            <span>多场景</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header>
          <div class="header-content">
            <breadcrumb />
            <div class="user-info">
              <el-dropdown>
                <span class="user-name">
                  <el-avatar :size="32">{{ userName[0] }}</el-avatar>
                  {{ userName }}
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { HomeFilled, Document, Warning, List, TrendCharts, Reading, Grid } from '@element-plus/icons-vue'
import { logout } from '@/api/auth'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const userName = computed(() => localStorage.getItem('username') || '管理员')

const handleLogout = async () => {
  await logout()
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
}

.el-aside {
  background: #304156;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: bold;
  background: #2b3a4b;
}

.el-header {
  background: white;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.el-main {
  background: #f5f7fa;
  padding: 20px;
}
</style>
