<template>
  <div class="defect-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>缺陷管理</span>
          <el-button type="primary" @click="handleCreate">上报缺陷</el-button>
        </div>
      </template>
      
      <el-table :data="defectList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="缺陷 ID" width="120" />
        <el-table-column prop="type" label="缺陷类型" width="120" />
        <el-table-column prop="level" label="缺陷等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deviceName" label="设备名称" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发现时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleAudit(row)" v-if="row.status === '待审核'">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDefectList } from '@/api/defect'

const loading = ref(false)
const defectList = ref([])

const getLevelType = (level) => {
  const map = { '一般': '', '严重': 'warning', '危急': 'danger' }
  return map[level] || ''
}

const getStatusType = (status) => {
  const map = { '待审核': '', '处理中': 'warning', '已处理': 'success' }
  return map[status] || ''
}

const loadDefectList = async () => {
  loading.value = true
  try {
    const data = await getDefectList({ page: 1, size: 20 })
    defectList.value = data.list
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  ElMessage.info('上报缺陷功能开发中')
}

const handleView = (row) => {
  ElMessage.info(`查看缺陷：${row.id}`)
}

const handleAudit = (row) => {
  ElMessage.info(`审核缺陷：${row.id}`)
}

onMounted(() => {
  loadDefectList()
})
</script>

<style scoped>
.defect-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
