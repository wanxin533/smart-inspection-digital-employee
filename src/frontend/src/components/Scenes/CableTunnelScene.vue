<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🚇 电缆隧道场景</h3>
      <p>隧道长度：{{ scenarioData.tunnelLength || '0km' }}</p>
      <p>电缆层数：{{ scenarioData.cableLayers || 0 }}</p>
      <p>监测点数量：{{ scenarioData.monitorPoints || 0 }}</p>
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
  scene.background = new THREE.Color(0x1a1a1a)

  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(0, 3, 5)
  camera.lookAt(0, 2, -10)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.4)
  scene.add(ambientLight)

  // 隧道灯
  for (let z = 0; z > -50; z -= 10) {
    const tunnelLight = new THREE.PointLight(0xffaa00, 1, 15)
    tunnelLight.position.set(0, 4, z)
    scene.add(tunnelLight)
  }

  createCableTunnel()
  animate()
}

const createCableTunnel = () => {
  // 隧道拱形结构
  const tunnelGroup = new THREE.Group()
  
  // 隧道壁（使用圆环的一部分）
  const tunnelShape = new THREE.Shape()
  const radius = 4
  const segments = 32
  
  for (let i = 0; i <= segments; i++) {
    const angle = (i / segments) * Math.PI + Math.PI
    const x = Math.cos(angle) * radius
    const y = Math.sin(angle) * radius
    if (i === 0) {
      tunnelShape.moveTo(x, y)
    } else {
      tunnelShape.lineTo(x, y)
    }
  }
  tunnelShape.lineTo(radius, 0)
  tunnelShape.lineTo(-radius, 0)
  tunnelShape.closePath()

  const tunnelGeometry = new THREE.ExtrudeGeometry(tunnelShape, {
    depth: 60,
    bevelEnabled: false
  })
  
  const tunnelMaterial = new THREE.MeshStandardMaterial({ 
    color: 0x444444,
    side: THREE.DoubleSide
  })
  
  const tunnel = new THREE.Mesh(tunnelGeometry, tunnelMaterial)
  tunnel.rotation.x = Math.PI / 2
  tunnel.position.z = -30
  tunnelGroup.add(tunnel)

  scene.add(tunnelGroup)

  // 电缆支架（多层）
  for (let z = 0; z > -50; z -= 5) {
    // 左侧支架
    const leftBracket = new THREE.Mesh(
      new THREE.BoxGeometry(0.3, 0.2, 2),
      new THREE.MeshStandardMaterial({ color: 0x666666 })
    )
    leftBracket.position.set(-3.5, 2, z)
    scene.add(leftBracket)

    // 右侧支架
    const rightBracket = new THREE.Mesh(
      new THREE.BoxGeometry(0.3, 0.2, 2),
      new THREE.MeshStandardMaterial({ color: 0x666666 })
    )
    rightBracket.position.set(3.5, 2, z)
    scene.add(rightBracket)

    // 电缆
    const leftCable = new THREE.Mesh(
      new THREE.CylinderGeometry(0.15, 0.15, 60, 8),
      new THREE.MeshStandardMaterial({ color: 0x8b4513 })
    )
    leftCable.rotation.z = Math.PI / 2
    leftCable.position.set(-3.5, 2.3, -25)
    scene.add(leftCable)

    const rightCable = new THREE.Mesh(
      new THREE.CylinderGeometry(0.15, 0.15, 60, 8),
      new THREE.MeshStandardMaterial({ color: 0x8b4513 })
    )
    rightCable.rotation.z = Math.PI / 2
    rightCable.position.set(3.5, 2.3, -25)
    scene.add(rightCable)
  }

  // 地面
  const floor = new THREE.Mesh(
    new THREE.PlaneGeometry(8, 60),
    new THREE.MeshStandardMaterial({ color: 0x2a2a2a })
  )
  floor.rotation.x = -Math.PI / 2
  floor.position.set(0, 0, -30)
  scene.add(floor)
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  // 摄像机向前移动（模拟巡检）
  const time = Date.now() * 0.00005
  camera.position.z = 5 - (time % 50)
  camera.lookAt(0, 2, camera.position.z - 15)

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
  background: linear-gradient(135deg, #1a1a1a 0%, #0d0d0d 100%);
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
