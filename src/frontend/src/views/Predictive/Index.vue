<template>
  <div class="predictive-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>预测性维护</span>
        </div>
      </template>
      
      <el-empty description="预测性维护功能开发中" />
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-statistic title="设备健康度" value="92" suffix="%" />
        </el-col>
        <el-col :span="8">
          <el-statistic title="预警数量" value="3" />
        </el-col>
        <el-col :span="8">
          <el-statistic title="预计维护成本" value="15000" prefix="¥" />
        </el-col>
      </el-row>
      
      <el-table :data="predictions" style="margin-top: 20px">
        <el-table-column prop="deviceName" label="设备名称" />
        <el-table-column prop="healthScore" label="健康度" />
        <el-table-column prop="predictTime" label="预计维护时间" />
        <el-table-column prop="suggestion" label="建议" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const predictions = ref([
  { deviceName: '#1 主变压器', healthScore: '85%', predictTime: '2026-04-15', suggestion: '建议进行油色谱分析' },
  { deviceName: '#2 断路器', healthScore: '78%', predictTime: '2026-04-10', suggestion: '建议检查触头磨损情况' },
  { deviceName: '#3 隔离开关', healthScore: '90%', predictTime: '2026-04-20', suggestion: '建议进行润滑保养' }
])
</script>

<style scoped>
.predictive-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
