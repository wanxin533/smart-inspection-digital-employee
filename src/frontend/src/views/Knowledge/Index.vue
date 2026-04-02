<template>
  <div class="knowledge-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>知识图谱</span>
        </div>
      </template>
      
      <el-empty description="知识图谱功能开发中" />
      
      <div style="text-align: center; padding: 40px">
        <el-descriptions title="知识图谱统计" :column="3" border>
          <el-descriptions-item label="实体数量">1,234</el-descriptions-item>
          <el-descriptions-item label="关系数量">5,678</el-descriptions-item>
          <el-descriptions-item label="规则数量">234</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script setup>
// 知识图谱页面
</script>

<style scoped>
.knowledge-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
