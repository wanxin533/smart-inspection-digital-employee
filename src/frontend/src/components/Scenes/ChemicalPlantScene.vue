<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🏭 石油化工场景</h3>
      <p>储罐数量：{{ scenarioData.tankCount || 0 }}</p>
      <p>管道长度：{{ scenarioData.pipelineLength || '0km' }}</p>
      <p>压力监测点：{{ scenarioData.pressurePoints || 0 }}</p>
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
  scene.background = new THREE.Color(0x2c2c3a)

  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(20, 15, 20)
  camera.lookAt(0, 5, 0)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.5)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffaa00, 0.8)
  directionalLight.position.set(10, 15, 10)
  scene.add(directionalLight)

  createChemicalPlant()
  animate()
}

const createChemicalPlant = () => {
  // 地面
  const ground = new THREE.Mesh(
    new THREE.PlaneGeometry(50, 50),
    new THREE.MeshStandardMaterial({ color: 0x4a4a4a })
  )
  ground.rotation.x = -Math.PI / 2
  ground.position.y = 0
  scene.add(ground)

  // 大型储罐
  const tankPositions = [
    { x: -10, z: -5, radius: 4, height: 6 },
    { x: 10, z: -5, radius: 4, height: 6 },
    { x: -10, z: 5, radius: 3, height: 5 },
    { x: 10, z: 5, radius: 3, height: 5 }
  ]

  tankPositions.forEach((tank, index) => {
    const tankGroup = new THREE.Group()

    // 罐体
    const tankBody = new THREE.Mesh(
      new THREE.CylinderGeometry(tank.radius, tank.radius, tank.height, 32),
      new THREE.MeshStandardMaterial({ 
        color: 0x8b9dc3,
        roughness: 0.4,
        metalness: 0.7
      })
    )
    tankBody.position.y = tank.height / 2
    tankGroup.add(tankBody)

    // 罐顶
    const tankTop = new THREE.Mesh(
      new THREE.SphereGeometry(tank.radius, 32, 16, 0, Math.PI * 2, 0, Math.PI / 2),
      new THREE.MeshStandardMaterial({ 
        color: 0x9babc3,
        roughness: 0.4,
        metalness: 0.7
      })
    )
    tankTop.position.y = tank.height
    tankGroup.add(tankTop)

    // 管道连接
    const pipe = new THREE.Mesh(
      new THREE.CylinderGeometry(0.3, 0.3, 8, 8),
      new THREE.MeshStandardMaterial({ color: 0x654321 })
    )
    pipe.rotation.z = Math.PI / 2
    pipe.position.set(0, tank.height * 0.7, tank.radius)
    tankGroup.add(pipe)

    tankGroup.position.set(tank.x, 0, tank.z)
    scene.add(tankGroup)
  })

  // 蒸馏塔
  const distillationTower = new THREE.Group()
  
  const towerBody = new THREE.Mesh(
    new THREE.CylinderGeometry(1.5, 2, 15, 16),
    new THREE.MeshStandardMaterial({ color: 0x778899, metalness: 0.6 })
  )
  towerBody.position.y = 7.5
  distillationTower.add(towerBody)

  // 塔平台（多层）
  for (let i = 0; i < 5; i++) {
    const platform = new THREE.Mesh(
      new THREE.CylinderGeometry(2.5, 2.5, 0.2, 16),
      new THREE.MeshStandardMaterial({ color: 0x555555 })
    )
    platform.position.y = 2 + i * 3
    distillationTower.add(platform)

    // 护栏
    const railing = new THREE.Mesh(
      new THREE.TorusGeometry(2.5, 0.1, 8, 32),
      new THREE.MeshStandardMaterial({ color: 0xff4500 })
    )
    railing.rotation.x = Math.PI / 2
    railing.position.y = 2.2 + i * 3
    distillationTower.add(railing)
  }

  distillationTower.position.set(0, 0, -8)
  scene.add(distillationTower)

  // 管道网络
  const pipeMaterial = new THREE.MeshStandardMaterial({ color: 0x8b4513 })
  
  // 横向管道
  for (let z = -10; z <= 10; z += 5) {
    const horizontalPipe = new THREE.Mesh(
      new THREE.CylinderGeometry(0.4, 0.4, 25, 8),
      pipeMaterial
    )
    horizontalPipe.rotation.z = Math.PI / 2
    horizontalPipe.position.set(0, 3, z)
    scene.add(horizontalPipe)
  }

  // 纵向管道
  for (let x = -12; x <= 12; x += 6) {
    const verticalPipe = new THREE.Mesh(
      new THREE.CylinderGeometry(0.4, 0.4, 25, 8),
      pipeMaterial
    )
    verticalPipe.rotation.x = Math.PI / 2
    verticalPipe.position.set(x, 3, 0)
    scene.add(verticalPipe)
  }
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  const time = Date.now() * 0.0001

  // 摄像机环绕
  camera.position.x = Math.cos(time * 0.3) * 25
  camera.position.z = Math.sin(time * 0.3) * 25
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
  background: linear-gradient(135deg, #2c2c3a 0%, #3a3a4a 100%);
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
