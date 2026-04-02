<template>
  <div class="scenarios-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>多场景管理</span>
        </div>
      </template>
      
      <el-empty description="多场景管理功能开发中" />
      
      <el-table :data="scenarios" style="margin-top: 20px">
        <el-table-column prop="name" label="场景名称" />
        <el-table-column prop="type" label="场景类型" />
        <el-table-column prop="deviceCount" label="设备数量" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '运行中' : '未启用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const scenarios = ref([
  { name: '变电站智能巡检', type: '输电', deviceCount: 156, status: 'active' },
  { name: '配电房监控', type: '配电', deviceCount: 89, status: 'active' },
  { name: '电缆隧道巡检', type: '配电', deviceCount: 45, status: 'inactive' },
  { name: '新能源场站', type: '发电', deviceCount: 234, status: 'active' }
])
</script>

<style scoped>
.scenarios-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
