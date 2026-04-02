<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🏢 配电房场景</h3>
      <p>配电室数量：{{ scenarioData.roomCount || 1 }}</p>
      <p>配电柜数量：{{ scenarioData.cabinetCount || 0 }}</p>
      <p>变压器台数：{{ scenarioData.transformerCount || 0 }}</p>
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
  scene.background = new THREE.Color(0x2c3e50)

  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(8, 5, 8)
  camera.lookAt(0, 2, 0)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.5)
  scene.add(ambientLight)

  const pointLight = new THREE.PointLight(0xffffff, 1, 20)
  pointLight.position.set(0, 8, 0)
  scene.add(pointLight)

  createPowerRoom()
  animate()
}

const createPowerRoom = () => {
  // 房间墙壁
  const roomGroup = new THREE.Group()

  // 地板
  const floor = new THREE.Mesh(
    new THREE.BoxGeometry(12, 0.2, 8),
    new THREE.MeshStandardMaterial({ color: 0x555555 })
  )
  floor.position.y = 0.1
  roomGroup.add(floor)

  // 后墙
  const backWall = new THREE.Mesh(
    new THREE.BoxGeometry(12, 6, 0.3),
    new THREE.MeshStandardMaterial({ color: 0x8b9dc3 })
  )
  backWall.position.set(0, 3, -4)
  roomGroup.add(backWall)

  // 左墙
  const leftWall = new THREE.Mesh(
    new THREE.BoxGeometry(0.3, 6, 8),
    new THREE.MeshStandardMaterial({ color: 0x8b9dc3 })
  )
  leftWall.position.set(-6, 3, 0)
  roomGroup.add(leftWall)

  // 右墙
  const rightWall = new THREE.Mesh(
    new THREE.BoxGeometry(0.3, 6, 8),
    new THREE.MeshStandardMaterial({ color: 0x8b9dc3 })
  )
  rightWall.position.set(6, 3, 0)
  roomGroup.add(rightWall)

  scene.add(roomGroup)

  // 配电柜（左侧）
  for (let i = 0; i < 4; i++) {
    const cabinet = new THREE.Mesh(
      new THREE.BoxGeometry(1.2, 2.5, 0.8),
      new THREE.MeshStandardMaterial({ color: 0x4a90d9 })
    )
    cabinet.position.set(-4, 1.25, -2 + i * 1.5)
    scene.add(cabinet)

    // 指示灯
    const indicator = new THREE.Mesh(
      new THREE.SphereGeometry(0.08, 8, 8),
      new THREE.MeshBasicMaterial({ color: i % 2 === 0 ? 0x00ff00 : 0xff0000 })
    )
    indicator.position.set(-3.4, 2, -2 + i * 1.5)
    scene.add(indicator)
  }

  // 配电柜（右侧）
  for (let i = 0; i < 4; i++) {
    const cabinet = new THREE.Mesh(
      new THREE.BoxGeometry(1.2, 2.5, 0.8),
      new THREE.MeshStandardMaterial({ color: 0x4a90d9 })
    )
    cabinet.position.set(4, 1.25, -2 + i * 1.5)
    scene.add(cabinet)
  }

  // 中央变压器
  const transformer = new THREE.Mesh(
    new THREE.CylinderGeometry(0.8, 0.8, 1.5, 16),
    new THREE.MeshStandardMaterial({ color: 0x2ecc71 })
  )
  transformer.position.set(0, 0.75, 2)
  scene.add(transformer)

  // 天花板灯
  for (let x = -4; x <= 4; x += 4) {
    for (let z = -2; z <= 2; z += 4) {
      const lightFixture = new THREE.Mesh(
        new THREE.BoxGeometry(1, 0.1, 1),
        new THREE.MeshBasicMaterial({ color: 0xffffcc })
      )
      lightFixture.position.set(x, 5.9, z)
      scene.add(lightFixture)
    }
  }
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  // 指示灯闪烁
  const time = Date.now() * 0.003
  scene.children.forEach(child => {
    if (child.material && child.material.emissive) {
      child.material.emissive.setHSL(0, 1, 0.5 + Math.sin(time) * 0.5)
    }
  })

  // 摄像机轻微移动
  camera.position.x = Math.sin(Date.now() * 0.0002) * 1
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
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  border-radius: 8px;
  overflow: hidden;
}

.scene-info {
  position: absolute;
  top: 20px;
  left: 20px;
  color: white;
  z-index: 10;
  background: rgba(0, 0, 0, 0.7);
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
