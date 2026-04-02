<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🚇 城市管廊场景</h3>
      <p>管廊长度：{{ scenarioData.galleryLength || '0km' }}</p>
      <p>管线类型：{{ scenarioData.utilityTypes || '电力/通信/给水' }}</p>
      <p>监测设备：{{ scenarioData.monitorDevices || 0 }}</p>
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

const initScene = () => {
  if (!sceneContainer.value) return

  scene = new THREE.Scene()
  scene.background = new THREE.Color(0x1a1a2e)

  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(0, 4, 3)
  camera.lookAt(0, 2, -20)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.4)
  scene.add(ambientLight)

  // 管廊灯带
  for (let z = 0; z > -60; z -= 8) {
    const corridorLight = new THREE.PointLight(0x00aaff, 0.8, 12)
    corridorLight.position.set(0, 3.5, z)
    scene.add(corridorLight)
  }

  createUtilityGallery()
  animate()
}

const createUtilityGallery = () => {
  // 管廊主体结构（矩形隧道）
  const galleryGroup = new THREE.Group()

  // 地板
  const floor = new THREE.Mesh(
    new THREE.BoxGeometry(6, 0.3, 60),
    new THREE.MeshStandardMaterial({ color: 0x3a3a4a })
  )
  floor.position.set(0, 0.15, -30)
  galleryGroup.add(floor)

  // 天花板
  const ceiling = new THREE.Mesh(
    new THREE.BoxGeometry(6, 0.3, 60),
    new THREE.MeshStandardMaterial({ color: 0x4a4a5a })
  )
  ceiling.position.set(0, 4.15, -30)
  galleryGroup.add(ceiling)

  // 左墙
  const leftWall = new THREE.Mesh(
    new THREE.BoxGeometry(0.3, 4, 60),
    new THREE.MeshStandardMaterial({ color: 0x5a5a6a })
  )
  leftWall.position.set(-3, 2, -30)
  galleryGroup.add(leftWall)

  // 右墙
  const rightWall = new THREE.Mesh(
    new THREE.BoxGeometry(0.3, 4, 60),
    new THREE.MeshStandardMaterial({ color: 0x5a5a6a })
  )
  rightWall.position.set(3, 2, -30)
  galleryGroup.add(rightWall)

  scene.add(galleryGroup)

  // 管线支架（多层）
  const pipeColors = [0xff4444, 0x44ff44, 0x4444ff, 0xffff44] // 电力/通信/给水/燃气
  const pipeTypes = ['电力', '通信', '给水', '燃气']

  for (let layer = 0; layer < 3; layer++) {
    const y = 1.5 + layer * 0.8
    
    // 左侧支架
    const leftBracket = new THREE.Mesh(
      new THREE.BoxGeometry(0.4, 0.2, 60),
      new THREE.MeshStandardMaterial({ color: 0x666666 })
    )
    leftBracket.position.set(-2.3, y, -30)
    scene.add(leftBracket)

    // 右侧支架
    const rightBracket = new THREE.Mesh(
      new THREE.BoxGeometry(0.4, 0.2, 60),
      new THREE.MeshStandardMaterial({ color: 0x666666 })
    )
    rightBracket.position.set(2.3, y, -30)
    scene.add(rightBracket)

    // 左侧管道
    const leftPipe = new THREE.Mesh(
      new THREE.CylinderGeometry(0.15, 0.15, 60, 12),
      new THREE.MeshStandardMaterial({ 
        color: pipeColors[layer],
        roughness: 0.4,
        metalness: 0.5
      })
    )
    leftPipe.rotation.z = Math.PI / 2
    leftPipe.position.set(-2.3, y + 0.25, -30)
    scene.add(leftPipe)

    // 右侧管道
    const rightPipe = new THREE.Mesh(
      new THREE.CylinderGeometry(0.15, 0.15, 60, 12),
      new THREE.MeshStandardMaterial({ 
        color: pipeColors[layer],
        roughness: 0.4,
        metalness: 0.5
      })
    )
    rightPipe.rotation.z = Math.PI / 2
    rightPipe.position.set(2.3, y + 0.25, -30)
    scene.add(rightPipe)
  }

  // 检修通道标识线
  const lineGeometry = new THREE.PlaneGeometry(0.2, 60)
  const lineMaterial = new THREE.MeshBasicMaterial({ color: 0xffff00 })
  
  const leftLine = new THREE.Mesh(lineGeometry, lineMaterial)
  leftLine.rotation.x = -Math.PI / 2
  leftLine.position.set(-1, 0.31, -30)
  scene.add(leftLine)

  const rightLine = new THREE.Mesh(lineGeometry, lineMaterial)
  rightLine.rotation.x = -Math.PI / 2
  rightLine.position.set(1, 0.31, -30)
  scene.add(rightLine)

  // 监测设备箱
  for (let z = -10; z > -50; z -= 15) {
    const monitorBox = new THREE.Mesh(
      new THREE.BoxGeometry(0.5, 0.6, 0.3),
      new THREE.MeshStandardMaterial({ color: 0x4a90d9 })
    )
    monitorBox.position.set(-2.7, 1, z)
    scene.add(monitorBox)

    // 指示灯
    const led = new THREE.Mesh(
      new THREE.SphereGeometry(0.08, 8, 8),
      new THREE.MeshBasicMaterial({ color: 0x00ff00 })
    )
    led.position.set(-2.45, 1.2, z)
    scene.add(led)
  }
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  // 摄像机向前推进（模拟巡检机器人视角）
  const time = Date.now() * 0.00005
  camera.position.z = 3 - (time % 55)
  camera.lookAt(0, 2, camera.position.z - 25)

  // 灯光闪烁效果
  scene.traverse((child) => {
    if (child.isPointLight) {
      child.intensity = 0.6 + Math.sin(Date.now() * 0.002 + child.position.z) * 0.2
    }
  })

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
  background: linear-gradient(135deg, #1a1a2e 0%, #0f0f1a 100%);
  border-radius: 8px;
  overflow: hidden;
}

.scene-info {
  position: absolute;
  top: 20px;
  left: 20px;
  color: #00aaff;
  z-index: 10;
  background: rgba(0, 0, 0, 0.7);
  padding: 15px;
  border-radius: 8px;
  backdrop-filter: blur(10px);
  border-left: 3px solid #00aaff;
}

.scene-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #00aaff;
}

.scene-info p {
  margin: 5px 0;
  font-size: 14px;
  opacity: 0.9;
}
</style>
