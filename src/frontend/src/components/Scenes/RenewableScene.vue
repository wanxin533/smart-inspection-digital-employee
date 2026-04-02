<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🌞 新能源场景</h3>
      <p>光伏板数量：{{ scenarioData.panelCount || 0 }}</p>
      <p>装机容量：{{ scenarioData.capacity || '0MW' }}</p>
      <p>今日发电：{{ scenarioData.todayPower || '0kWh' }}</p>
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

let scene = null
let camera = null
let renderer = null
let animationId = null
let solarPanels = []

const initScene = () => {
  if (!sceneContainer.value) return

  scene = new THREE.Scene()
  scene.background = new THREE.Color(0x87ceeb)

  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(15, 10, 15)
  camera.lookAt(0, 2, 0)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
  scene.add(ambientLight)

  const sunLight = new THREE.DirectionalLight(0xffddaa, 1.2)
  sunLight.position.set(20, 20, 10)
  scene.add(sunLight)

  createSolarFarm()
  animate()
}

const createSolarFarm = () => {
  // 地面
  const ground = new THREE.Mesh(
    new THREE.PlaneGeometry(50, 50),
    new THREE.MeshStandardMaterial({ color: 0x5d8a3d })
  )
  ground.rotation.x = -Math.PI / 2
  ground.position.y = 0
  scene.add(ground)

  // 光伏板阵列
  const rows = 5
  const cols = 6
  const rowSpacing = 4
  const colSpacing = 3

  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      const panelGroup = new THREE.Group()

      // 光伏板
      const panel = new THREE.Mesh(
        new THREE.BoxGeometry(2.5, 0.1, 1.5),
        new THREE.MeshStandardMaterial({ 
          color: 0x1a3a5c,
          roughness: 0.3,
          metalness: 0.7
        })
      )
      panel.rotation.x = -Math.PI / 6 // 倾斜 30 度
      panelGroup.add(panel)

      // 支架
      const stand = new THREE.Mesh(
        new THREE.CylinderGeometry(0.1, 0.1, 1.5, 8),
        new THREE.MeshStandardMaterial({ color: 0x888888 })
      )
      stand.position.y = -0.5
      panelGroup.add(stand)

      // 底座
      const base = new THREE.Mesh(
        new THREE.BoxGeometry(0.8, 0.2, 0.8),
        new THREE.MeshStandardMaterial({ color: 0x666666 })
      )
      base.position.y = -1.3
      panelGroup.add(base)

      const x = (col - cols / 2) * colSpacing
      const z = (row - rows / 2) * rowSpacing
      panelGroup.position.set(x, 1, z)
      
      scene.add(panelGroup)
      solarPanels.push(panelGroup)
    }
  }

  // 添加太阳
  const sunGeometry = new THREE.SphereGeometry(3, 32, 32)
  const sunMaterial = new THREE.MeshBasicMaterial({ color: 0xffdd00 })
  const sun = new THREE.Mesh(sunGeometry, sunMaterial)
  sun.position.set(30, 20, 10)
  scene.add(sun)

  // 添加光晕
  const glowMaterial = new THREE.MeshBasicMaterial({ 
    color: 0xffaa00, 
    transparent: true, 
    opacity: 0.3 
  })
  const glow = new THREE.Mesh(
    new THREE.SphereGeometry(4, 32, 32),
    glowMaterial
  )
  glow.position.set(30, 20, 10)
  scene.add(glow)
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  const time = Date.now() * 0.0001

  // 光伏板跟随太阳转动（简化版）
  solarPanels.forEach((panel, index) => {
    panel.rotation.y = Math.sin(time + index * 0.1) * 0.2
  })

  // 摄像机环绕
  camera.position.x = Math.cos(time * 0.5) * 20
  camera.position.z = Math.sin(time * 0.5) * 20
  camera.lookAt(0, 2, 0)

  if (renderer && scene && camera) {
    renderer.render(scene, camera)
  }
}

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
  background: linear-gradient(135deg, #87ceeb 0%, #b0e0f5 100%);
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
  text-shadow: 1px 1px 2px rgba(0,0,0,0.8);
}

.scene-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
}

.scene-info p {
  margin: 5px 0;
  font-size: 14px;
  opacity: 0.95;
}
</style>
