<template>
  <div class="inspection-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>巡视任务</span>
          <el-button type="primary" @click="handleCreate">新建任务</el-button>
        </div>
      </template>
      
      <el-table :data="taskList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="任务 ID" width="120" />
        <el-table-column prop="name" label="任务名称" />
        <el-table-column prop="type" label="巡视类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assigneeName" label="执行人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleExecute(row)" v-if="row.status === '待执行'">执行</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadTaskList"
        @size-change="loadTaskList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getTaskList } from '@/api/inspection'

const loading = ref(false)
const taskList = ref([])

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const getStatusType = (status) => {
  const map = {
    '待执行': '',
    '进行中': 'warning',
    '已完成': 'success'
  }
  return map[status] || ''
}

const loadTaskList = async () => {
  loading.value = true
  try {
    const data = await getTaskList({
      page: pagination.page,
      size: pagination.size
    })
    taskList.value = data.list
    pagination.total = data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  ElMessage.info('新建任务功能开发中')
}

const handleView = (row) => {
  ElMessage.info(`查看任务：${row.id}`)
}

const handleExecute = (row) => {
  ElMessage.info(`执行任务：${row.id}`)
}

onMounted(() => {
  loadTaskList()
})
</script>

<style scoped>
.inspection-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
