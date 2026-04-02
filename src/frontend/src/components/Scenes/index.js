/**
 * 智能巡视系统 - 3D 场景组件索引
 * 
 * 包含 9 大电力巡视场景的 3D 可视化组件
 * 基于 Three.js 实现，支持实时渲染和交互
 */

// 核心场景组件
export { default as SubstationScene } from './SubstationScene.vue'       // 变电站场景
export { default as TransmissionLineScene } from './TransmissionLineScene.vue'  // 输电线路场景
export { default as PowerRoomScene } from './PowerRoomScene.vue'          // 配电房场景
export { default as CableTunnelScene } from './CableTunnelScene.vue'      // 电缆隧道场景
export { default as RenewableScene } from './RenewableScene.vue'          // 新能源场景
export { default as DataCenterScene } from './DataCenterScene.vue'        // 数据中心场景
export { default as ChemicalPlantScene } from './ChemicalPlantScene.vue'  // 石油化工场景
export { default as UrbanGalleryScene } from './UrbanGalleryScene.vue'    // 城市管廊场景
export { default as RailTransitScene } from './RailTransitScene.vue'      // 轨道交通场景

// 场景配置映射
export const SCENE_CONFIGS = {
  substation: {
    name: '变电站场景',
    component: 'SubstationScene',
    icon: '🏭',
    defaultData: {
      voltageLevel: '220kV',
      deviceCount: 156,
      area: '5000㎡'
    }
  },
  transmission: {
    name: '输电线路场景',
    component: 'TransmissionLineScene',
    icon: '⚡',
    defaultData: {
      voltageLevel: '500kV',
      lineLength: '120km',
      towerCount: 285
    }
  },
  powerRoom: {
    name: '配电房场景',
    component: 'PowerRoomScene',
    icon: '🏢',
    defaultData: {
      roomCount: 3,
      cabinetCount: 24,
      transformerCount: 2
    }
  },
  cableTunnel: {
    name: '电缆隧道场景',
    component: 'CableTunnelScene',
    icon: '🚇',
    defaultData: {
      tunnelLength: '8.5km',
      cableLayers: 3,
      monitorPoints: 156
    }
  },
  renewable: {
    name: '新能源场景',
    component: 'RenewableScene',
    icon: '🌞',
    defaultData: {
      panelCount: 2400,
      capacity: '50MW',
      todayPower: '18500kWh'
    }
  },
  dataCenter: {
    name: '数据中心场景',
    component: 'DataCenterScene',
    icon: '🖥️',
    defaultData: {
      rackCount: 128,
      pue: '1.35',
      temperature: '22°C'
    }
  },
  chemicalPlant: {
    name: '石油化工场景',
    component: 'ChemicalPlantScene',
    icon: '🏭',
    defaultData: {
      tankCount: 12,
      pipelineLength: '3.2km',
      pressurePoints: 86
    }
  },
  urbanGallery: {
    name: '城市管廊场景',
    component: 'UrbanGalleryScene',
    icon: '🚇',
    defaultData: {
      galleryLength: '15km',
      utilityTypes: '电力/通信/给水/燃气',
      monitorDevices: 245
    }
  },
  railTransit: {
    name: '轨道交通场景',
    component: 'RailTransitScene',
    icon: '🚄',
    defaultData: {
      lineLength: '45km',
      stationCount: 28,
      powerSupply: 'DC1500V'
    }
  }
}

/**
 * 获取场景组件
 * @param {string} sceneType - 场景类型
 * @returns {Component} Vue 组件
 */
export function getSceneComponent(sceneType) {
  const config = SCENE_CONFIGS[sceneType]
  if (!config) {
    console.warn(`Unknown scene type: ${sceneType}`)
    return null
  }
  return config.component
}

/**
 * 获取所有场景列表
 * @returns {Array} 场景配置数组
 */
export function getAllScenes() {
  return Object.values(SCENE_CONFIGS)
}
