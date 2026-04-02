<template>
  <div class="work-order-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>工单管理</span>
          <el-button type="primary" @click="handleCreate">创建工单</el-button>
        </div>
      </template>
      
      <el-table :data="workOrderList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="工单 ID" width="140" />
        <el-table-column prop="type" label="工单类型" width="100" />
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assigneeName" label="处理人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleProcess(row)" v-if="row.status === '待处理'">处理</el-button>
            <el-button size="small" type="success" @click="handleAccept(row)" v-if="row.status === '待验收'">验收</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const workOrderList = ref([
  { id: 'WO20260331001', type: '维修', priority: '高', status: '待处理', assigneeName: '张三', createTime: '2026-03-31 14:00:00' },
  { id: 'WO20260331002', type: '巡检', priority: '中', status: '处理中', assigneeName: '李四', createTime: '2026-03-31 13:00:00' },
  { id: 'WO20260331003', type: '维修', priority: '低', status: '待验收', assigneeName: '王五', createTime: '2026-03-31 12:00:00' }
])

const getPriorityType = (priority) => {
  const map = { '高': 'danger', '中': 'warning', '低': '' }
  return map[priority] || ''
}

const getStatusType = (status) => {
  const map = { '待处理': '', '处理中': 'warning', '待验收': 'success', '已完成': 'success' }
  return map[status] || ''
}

const handleCreate = () => {
  ElMessage.info('创建工单功能开发中')
}

const handleView = (row) => {
  ElMessage.info(`查看工单：${row.id}`)
}

const handleProcess = (row) => {
  ElMessage.info(`处理工单：${row.id}`)
}

const handleAccept = (row) => {
  ElMessage.info(`验收工单：${row.id}`)
}

onMounted(() => {
  // 加载工单列表
})
</script>

<style scoped>
.work-order-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
