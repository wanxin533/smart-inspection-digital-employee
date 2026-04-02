<template>
  <div class="scene-detail-container">
    <el-card class="scene-card">
      <template #header>
        <div class="card-header">
          <div class="scene-title">
            <span class="scene-icon">{{ currentSceneConfig?.icon }}</span>
            <h2>{{ currentSceneConfig?.name || '3D 场景展示' }}</h2>
          </div>
          <el-select v-model="currentSceneType" @change="switchScene" placeholder="选择场景">
            <el-option
              v-for="scene in allScenes"
              :key="scene.component"
              :label="`${scene.icon} ${scene.name}`"
              :value="scene.component.toLowerCase().replace('scene', '')"
            />
          </el-select>
        </div>
      </template>

      <!-- 3D 场景容器 -->
      <div class="3d-scene-wrapper">
        <component
          :is="currentSceneComponent"
          :scenario-data="sceneData"
          :key="currentSceneType"
        />
      </div>

      <!-- 场景信息面板 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="16">
          <el-card shadow="hover">
            <template #header>
              <span>📊 实时监测数据</span>
            </template>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="场景类型">
                {{ currentSceneConfig?.name }}
              </el-descriptions-item>
              <el-descriptions-item label="设备总数">
                {{ sceneData.deviceCount || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="运行状态">
                <el-tag type="success">正常运行</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="最后更新">
                {{ lastUpdateTime }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <span>🎮 操作控制</span>
            </template>
            <div class="control-panel">
              <el-button type="primary" @click="resetView" :icon="RefreshRight">
                重置视角
              </el-button>
              <el-button @click="toggleAutoRotate" :icon="Loading">
                {{ autoRotate ? '停止旋转' : '自动旋转' }}
              </el-button>
              <el-button @click="exportScreenshot" :icon="Camera">
                导出截图
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 告警信息 -->
      <el-card shadow="hover" style="margin-top: 20px">
        <template #header>
          <span>🚨 实时告警</span>
        </template>
        <el-table :data="alarmList" style="width: 100%" max-height="300">
          <el-table-column prop="type" label="告警类型" width="150" />
          <el-table-column prop="level" label="级别" width="80">
            <template #default="{ row }">
              <el-tag :type="getLevelType(row.level)">
                {{ row.level }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="device" label="设备位置" />
          <el-table-column prop="time" label="发生时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" @click="handleAlarm(row)">处置</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RefreshRight, Loading, Camera } from '@element-plus/icons-vue'
import {
  SCENE_CONFIGS,
  getAllScenes,
  getSceneComponent
} from '@/components/Scenes'
import {
  SubstationScene,
  TransmissionLineScene,
  PowerRoomScene,
  CableTunnelScene,
  RenewableScene,
  DataCenterScene,
  ChemicalPlantScene,
  UrbanGalleryScene,
  RailTransitScene
} from '@/components/Scenes'

// 场景组件映射
const sceneComponents = {
  substation: SubstationScene,
  transmissionline: TransmissionLineScene,
  powerroom: PowerRoomScene,
  cabletunnel: CableTunnelScene,
  renewable: RenewableScene,
  datacenter: DataCenterScene,
  chemicalplant: ChemicalPlantScene,
  urbancorridor: UrbanGalleryScene,
  railtransit: RailTransitScene
}

// 状态
const currentSceneType = ref('substation')
const autoRotate = ref(true)
const lastUpdateTime = ref(new Date().toLocaleTimeString())

// 计算属性
const allScenes = computed(() => getAllScenes())
const currentSceneConfig = computed(() => SCENE_CONFIGS[currentSceneType.value])
const currentSceneComponent = computed(() => {
  return sceneComponents[currentSceneType.value] || SubstationScene
})

// 场景数据
const sceneData = ref({
  deviceCount: 156,
  voltageLevel: '220kV',
  temperature: '22°C',
  status: 'normal'
})

// 告警列表
const alarmList = ref([
  {
    id: 1,
    type: '温度异常',
    level: '警告',
    device: '#1 变压器 A 相',
    time: '2026-04-02 08:15:23'
  },
  {
    id: 2,
    type: '电压越限',
    level: '严重',
    device: '10kV 母线',
    time: '2026-04-02 08:12:45'
  },
  {
    id: 3,
    type: '油位偏低',
    level: '提示',
    device: '#2 断路器',
    time: '2026-04-02 08:08:12'
  }
])

// 方法
const switchScene = (type) => {
  console.log('Switching to scene:', type)
  // 更新场景数据
  const config = SCENE_CONFIGS[type]
  if (config) {
    sceneData.value = {
      ...config.defaultData,
      status: 'normal'
    }
    lastUpdateTime.value = new Date().toLocaleTimeString()
  }
}

const resetView = () => {
  // 重置视角逻辑（通过事件通知子组件）
  window.dispatchEvent(new CustomEvent('reset-scene-view'))
}

const toggleAutoRotate = () => {
  autoRotate.value = !autoRotate.value
  window.dispatchEvent(new CustomEvent('toggle-auto-rotate', { 
    detail: { rotate: autoRotate.value } 
  }))
}

const exportScreenshot = () => {
  // 导出截图逻辑
  const canvas = document.querySelector('canvas')
  if (canvas) {
    const link = document.createElement('a')
    link.download = `scene-${currentSceneType.value}-${Date.now()}.png`
    link.href = canvas.toDataURL('image/png')
    link.click()
  }
}

const handleAlarm = (alarm) => {
  console.log('Handling alarm:', alarm)
  // TODO: 打开告警处置对话框
}

const getLevelType = (level) => {
  const typeMap = {
    '提示': 'info',
    '警告': 'warning',
    '严重': 'danger'
  }
  return typeMap[level] || 'info'
}

// 生命周期
onMounted(() => {
  switchScene(currentSceneType.value)
  
  // 定时更新
  setInterval(() => {
    lastUpdateTime.value = new Date().toLocaleTimeString()
  }, 1000)
})
</script>

<style scoped>
.scene-detail-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.scene-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.scene-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.scene-title h2 {
  margin: 0;
  font-size: 20px;
  color: #303133;
}

.scene-icon {
  font-size: 24px;
}

.3d-scene-wrapper {
  width: 100%;
  height: 500px;
  border-radius: 8px;
  overflow: hidden;
  background: #000;
}

.control-panel {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

:deep(.el-descriptions__label) {
  width: 120px;
  font-weight: 600;
}
</style>
