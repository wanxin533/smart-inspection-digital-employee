<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>⚡ 输电线路场景</h3>
      <p>电压等级：{{ scenarioData.voltageLevel || '500kV' }}</p>
      <p>线路长度：{{ scenarioData.lineLength || '0km' }}</p>
      <p>杆塔数量：{{ scenarioData.towerCount || 0 }}</p>
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
let towers = []

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
  camera.position.set(0, 15, 30)
  camera.lookAt(0, 5, 0)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.7)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.9)
  directionalLight.position.set(50, 50, 50)
  scene.add(directionalLight)

  createTransmissionLine()
  animate()
}

const createTransmissionLine = () => {
  // 创建地面
  const ground = new THREE.Mesh(
    new THREE.PlaneGeometry(100, 40),
    new THREE.MeshStandardMaterial({ color: 0x3d8c40 })
  )
  ground.rotation.x = -Math.PI / 2
  ground.position.y = 0
  scene.add(ground)

  // 创建多个杆塔
  const towerCount = 5
  const spacing = 20

  for (let i = 0; i < towerCount; i++) {
    const towerGroup = new THREE.Group()
    
    // 塔身
    const towerBody = new THREE.Mesh(
      new THREE.CylinderGeometry(0.5, 1, 8, 4),
      new THREE.MeshStandardMaterial({ color: 0x8b4513 })
    )
    towerBody.position.y = 4
    towerGroup.add(towerBody)

    // 横担
    const crossArm = new THREE.Mesh(
      new THREE.BoxGeometry(6, 0.3, 0.5),
      new THREE.MeshStandardMaterial({ color: 0x654321 })
    )
    crossArm.position.y = 7
    towerGroup.add(crossArm)

    towerGroup.position.set((i - Math.floor(towerCount / 2)) * spacing, 0, 0)
    scene.add(towerGroup)
    towers.push(towerGroup)

    // 创建电线（使用曲线）
    if (i < towerCount - 1) {
      const points = []
      const startX = (i - Math.floor(towerCount / 2)) * spacing
      const endX = ((i + 1) - Math.floor(towerCount / 2)) * spacing
      
      points.push(new THREE.Vector3(startX, 7, 0))
      points.push(new THREE.Vector3(startX + spacing * 0.25, 5, 0))
      points.push(new THREE.Vector3(startX + spacing * 0.5, 4.5, 0))
      points.push(new THREE.Vector3(startX + spacing * 0.75, 5, 0))
      points.push(new THREE.Vector3(endX, 7, 0))

      const curve = new THREE.CatmullRomCurve3(points)
      const tubeGeometry = new THREE.TubeGeometry(curve, 20, 0.08, 8, false)
      const tubeMaterial = new THREE.MeshStandardMaterial({ color: 0x333333 })
      const tube = new THREE.Mesh(tubeGeometry, tubeMaterial)
      scene.add(tube)
    }
  }

  // 添加天空背景
  const skyGeometry = new THREE.SphereGeometry(80, 32, 32)
  const skyMaterial = new THREE.MeshBasicMaterial({ 
    color: 0x87ceeb, 
    side: THREE.BackSide 
  })
  const sky = new THREE.Mesh(skyGeometry, skyMaterial)
  scene.add(sky)
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  // 摄像机缓慢移动
  const time = Date.now() * 0.0001
  camera.position.x = Math.sin(time) * 10
  camera.lookAt(0, 5, 0)

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
