<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🏭 变电站场景</h3>
      <p>电压等级：{{ scenarioData.voltageLevel || '220kV' }}</p>
      <p>设备总数：{{ scenarioData.deviceCount || 0 }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as THREE from 'three'

const props = defineProps({
  scenarioData: {
    type: Object,
    default: () => ({})
  }
})

const sceneContainer = ref(null)

// Three.js 核心变量
let scene = null
let camera = null
let renderer = null
let animationId = null

// 初始化场景
const initScene = () => {
  if (!sceneContainer.value) return

  // 创建场景
  scene = new THREE.Scene()
  scene.background = new THREE.Color(0x1a1a2e)

  // 创建相机
  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(10, 10, 10)
  camera.lookAt(0, 0, 0)

  // 创建渲染器
  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 添加光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
  directionalLight.position.set(10, 10, 10)
  scene.add(directionalLight)

  // 创建变电站设备模型（简化版）
  createSubstationModels()

  // 开始动画
  animate()
}

// 创建变电站设备模型
const createSubstationModels = () => {
  // 变压器
  const transformerGroup = new THREE.Group()
  
  const transformerBody = new THREE.Mesh(
    new THREE.BoxGeometry(2, 1.5, 1.5),
    new THREE.MeshStandardMaterial({ color: 0x4a90d9 })
  )
  transformerBody.position.y = 0.75
  transformerGroup.add(transformerBody)

  // 变压器底座
  const base = new THREE.Mesh(
    new THREE.BoxGeometry(2.5, 0.3, 2),
    new THREE.MeshStandardMaterial({ color: 0x666666 })
  )
  base.position.y = 0.15
  transformerGroup.add(base)

  transformerGroup.position.set(-3, 0, 0)
  scene.add(transformerGroup)

  // 断路器
  const breakerGroup = new THREE.Group()
  
  const breakerBody = new THREE.Mesh(
    new THREE.CylinderGeometry(0.3, 0.3, 2, 8),
    new THREE.MeshStandardMaterial({ color: 0xd94a4a })
  )
  breakerBody.position.y = 1
  breakerGroup.add(breakerBody)

  breakerGroup.position.set(3, 0, 0)
  scene.add(breakerGroup)

  // 地面
  const ground = new THREE.Mesh(
    new THREE.PlaneGeometry(20, 20),
    new THREE.MeshStandardMaterial({ color: 0x2d2d44 })
  )
  ground.rotation.x = -Math.PI / 2
  ground.position.y = 0
  scene.add(ground)
}

// 动画循环
const animate = () => {
  animationId = requestAnimationFrame(animate)

  // 缓慢旋转场景
  scene.rotation.y += 0.002

  if (renderer && scene && camera) {
    renderer.render(scene, camera)
  }
}

// 窗口大小调整
const handleResize = () => {
  if (!sceneContainer.value || !camera || !renderer) return

  camera.aspect = sceneContainer.value.clientWidth / sceneContainer.value.clientHeight
  camera.updateProjectionMatrix()
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
}

onMounted(() => {
  initScene()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  if (renderer && sceneContainer.value) {
    sceneContainer.value.removeChild(renderer.domElement)
    renderer.dispose()
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.scene-container {
  width: 100%;
  height: 500px;
  position: relative;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  border-radius: 8px;
  overflow: hidden;
}

.scene-info {
  position: absolute;
  top: 20px;
  left: 20px;
  color: white;
  z-index: 10;
  background: rgba(0, 0, 0, 0.6);
  padding: 15px;
  border-radius: 8px;
  backdrop-filter: blur(10px);
}

.scene-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
}

.scene-info p {
  margin: 5px 0;
  font-size: 14px;
  opacity: 0.9;
}
</style>
