<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🚄 轨道交通场景</h3>
      <p>线路长度：{{ scenarioData.lineLength || '0km' }}</p>
      <p>车站数量：{{ scenarioData.stationCount || 0 }}</p>
      <p>供电方式：{{ scenarioData.powerSupply || 'DC1500V' }}</p>
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
let train = null

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
  camera.position.set(15, 10, 15)
  camera.lookAt(0, 2, 0)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
  directionalLight.position.set(10, 15, 10)
  scene.add(directionalLight)

  createRailTransit()
  animate()
}

const createRailTransit = () => {
  // 地面/轨道基础
  const ground = new THREE.Mesh(
    new THREE.BoxGeometry(30, 0.5, 8),
    new THREE.MeshStandardMaterial({ color: 0x555555 })
  )
  ground.position.y = 0.25
  scene.add(ground)

  // 铁轨（两条）
  const railMaterial = new THREE.MeshStandardMaterial({ 
    color: 0x8b4513,
    roughness: 0.3,
    metalness: 0.8
  })

  const leftRail = new THREE.Mesh(
    new THREE.BoxGeometry(30, 0.3, 0.2),
    railMaterial
  )
  leftRail.position.set(0, 0.65, -1.5)
  scene.add(leftRail)

  const rightRail = new THREE.Mesh(
    new THREE.BoxGeometry(30, 0.3, 0.2),
    railMaterial
  )
  rightRail.position.set(0, 0.65, 1.5)
  scene.add(rightRail)

  // 枕木
  for (let x = -14; x <= 14; x += 1.5) {
    const sleeper = new THREE.Mesh(
      new THREE.BoxGeometry(0.3, 0.2, 3.5),
      new THREE.MeshStandardMaterial({ color: 0x654321 })
    )
    sleeper.position.set(x, 0.5, 0)
    scene.add(sleeper)
  }

  // 列车（简化版）
  train = new THREE.Group()

  // 车厢主体
  const trainBody = new THREE.Mesh(
    new THREE.BoxGeometry(12, 2.5, 2.8),
    new THREE.MeshStandardMaterial({ 
      color: 0x0066cc,
      roughness: 0.4,
      metalness: 0.6
    })
  )
  trainBody.position.y = 2
  train.add(trainBody)

  // 车头（流线型）
  const trainNose = new THREE.Mesh(
    new THREE.ConeGeometry(1.4, 2, 16),
    new THREE.MeshStandardMaterial({ 
      color: 0x0055aa,
      roughness: 0.4,
      metalness: 0.6
    })
  )
  trainNose.rotation.x = Math.PI / 2
  trainNose.position.set(7, 2, 0)
  train.add(trainNose)

  // 车窗
  for (let x = -4; x <= 4; x += 2) {
    const windowLeft = new THREE.Mesh(
      new THREE.BoxGeometry(1.5, 0.8, 0.1),
      new THREE.MeshBasicMaterial({ color: 0x87ceeb })
    )
    windowLeft.position.set(x, 2.5, -1.41)
    train.add(windowLeft)

    const windowRight = new THREE.Mesh(
      new THREE.BoxGeometry(1.5, 0.8, 0.1),
      new THREE.MeshBasicMaterial({ color: 0x87ceeb })
    )
    windowRight.position.set(x, 2.5, 1.41)
    train.add(windowRight)
  }

  // 受电弓
  const pantograph = new THREE.Group()
  const pantographBase = new THREE.Mesh(
    new THREE.BoxGeometry(0.5, 0.8, 0.5),
    new THREE.MeshStandardMaterial({ color: 0x666666 })
  )
  pantographBase.position.y = 3.4
  pantograph.add(pantographBase)

  const pantographArm = new THREE.Mesh(
    new THREE.BoxGeometry(2, 0.1, 0.3),
    new THREE.MeshStandardMaterial({ color: 0x888888 })
  )
  pantographArm.position.set(0, 3.8, 0)
  pantograph.add(pantographArm)

  train.add(pantograph)

  train.position.set(-8, 0.65, 0)
  scene.add(train)

  // 接触网支柱
  for (let x = -12; x <= 12; x += 8) {
    const pole = new THREE.Mesh(
      new THREE.CylinderGeometry(0.2, 0.3, 6, 8),
      new THREE.MeshStandardMaterial({ color: 0x777777 })
    )
    pole.position.set(x, 3, 3.5)
    scene.add(pole)

    // 横臂
    const arm = new THREE.Mesh(
      new THREE.BoxGeometry(4, 0.15, 0.2),
      new THREE.MeshStandardMaterial({ color: 0x666666 })
    )
    arm.position.set(x + 1, 5.8, 3.5)
    scene.add(arm)

    // 接触线
    const wire = new THREE.Mesh(
      new THREE.BoxGeometry(0.05, 0.05, 30),
      new THREE.MeshStandardMaterial({ color: 0x8b4513 })
    )
    wire.position.set(0, 5.5, 3.5)
    scene.add(wire)
  }

  // 站台
  const platform = new THREE.Mesh(
    new THREE.BoxGeometry(15, 1, 3),
    new THREE.MeshStandardMaterial({ color: 0x999999 })
  )
  platform.position.set(0, 1, -5)
  scene.add(platform)

  // 站台顶棚
  const roof = new THREE.Mesh(
    new THREE.BoxGeometry(16, 0.2, 4),
    new THREE.MeshStandardMaterial({ color: 0x666677 })
  )
  roof.position.set(0, 6, -5)
  scene.add(roof)

  // 支撑柱
  for (let x = -6; x <= 6; x += 6) {
    const column = new THREE.Mesh(
      new THREE.CylinderGeometry(0.3, 0.3, 5, 8),
      new THREE.MeshStandardMaterial({ color: 0x888888 })
    )
    column.position.set(x, 3.5, -5)
    scene.add(column)
  }
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  // 列车移动
  if (train) {
    train.position.x += 0.05
    if (train.position.x > 15) {
      train.position.x = -15
    }
  }

  // 摄像机跟随列车
  camera.position.x = train.position.x - 8
  camera.position.z = train.position.z + 12
  camera.lookAt(train.position.x, 2, 0)

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
  border-left: 3px solid #0066cc;
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
