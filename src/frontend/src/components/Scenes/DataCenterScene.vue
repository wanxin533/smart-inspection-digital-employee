<template>
  <div class="scene-container" ref="sceneContainer">
    <div class="scene-info">
      <h3>🖥️ 数据中心场景</h3>
      <p>机柜数量：{{ scenarioData.rackCount || 0 }}</p>
      <p>PUE 值：{{ scenarioData.pue || '1.5' }}</p>
      <p>运行温度：{{ scenarioData.temperature || '22°C' }}</p>
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
  scene.background = new THREE.Color(0x0a0a1a)

  camera = new THREE.PerspectiveCamera(
    75,
    sceneContainer.value.clientWidth / sceneContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(10, 8, 10)
  camera.lookAt(0, 3, 0)

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(sceneContainer.value.clientWidth, sceneContainer.value.clientHeight)
  renderer.setPixelRatio(window.devicePixelRatio)
  sceneContainer.value.appendChild(renderer.domElement)

  // 光源
  const ambientLight = new THREE.AmbientLight(0x0044ff, 0.3)
  scene.add(ambientLight)

  // 机柜指示灯
  const pointLight = new THREE.PointLight(0x00ff00, 0.5, 15)
  pointLight.position.set(0, 5, 0)
  scene.add(pointLight)

  createDataCenter()
  animate()
}

const createDataCenter = () => {
  // 高架地板
  const raisedFloor = new THREE.Mesh(
    new THREE.BoxGeometry(20, 0.5, 15),
    new THREE.MeshStandardMaterial({ color: 0x333333, roughness: 0.8 })
  )
  raisedFloor.position.y = 0.25
  scene.add(raisedFloor)

  // 天花板
  const ceiling = new THREE.Mesh(
    new THREE.BoxGeometry(20, 0.3, 15),
    new THREE.MeshStandardMaterial({ color: 0x222222 })
  )
  ceiling.position.y = 7
  scene.add(ceiling)

  // 服务器机柜阵列
  const rows = 3
  const cols = 8
  const rowSpacing = 4
  const colSpacing = 2

  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      const rackGroup = new THREE.Group()

      // 机柜主体
      const rack = new THREE.Mesh(
        new THREE.BoxGeometry(1.2, 4, 1.5),
        new THREE.MeshStandardMaterial({ 
          color: 0x1a1a2e,
          roughness: 0.5,
          metalness: 0.6
        })
      )
      rack.position.y = 2
      rackGroup.add(rack)

      // 服务器单元（多层）
      for (let server = 0; server < 10; server++) {
        const serverUnit = new THREE.Mesh(
          new THREE.BoxGeometry(1.1, 0.3, 1.4),
          new THREE.MeshStandardMaterial({ color: 0x2a2a3e })
        )
        serverUnit.position.y = 0.5 + server * 0.38
        rackGroup.add(serverUnit)

        // 指示灯
        const ledColor = Math.random() > 0.3 ? 0x00ff00 : 0xff0000
        const led = new THREE.Mesh(
          new THREE.SphereGeometry(0.05, 8, 8),
          new THREE.MeshBasicMaterial({ color: ledColor })
        )
        led.position.set(0.5, 0.5 + server * 0.38, 0.76)
        rackGroup.add(led)
      }

      // 机柜门框
      const doorFrame = new THREE.Mesh(
        new THREE.BoxGeometry(1.3, 4.1, 0.1),
        new THREE.MeshStandardMaterial({ color: 0x444455 })
      )
      doorFrame.position.set(0, 2, 0.76)
      rackGroup.add(doorFrame)

      const x = (col - cols / 2) * colSpacing
      const z = (row - rows / 2) * rowSpacing
      rackGroup.position.set(x, 0.25, z)
      
      scene.add(rackGroup)
    }
  }

  // 空调系统（顶部）
  for (let x = -8; x <= 8; x += 8) {
    const acUnit = new THREE.Mesh(
      new THREE.BoxGeometry(2, 1, 2),
      new THREE.MeshStandardMaterial({ color: 0x666677 })
    )
    acUnit.position.set(x, 6.5, 0)
    scene.add(acUnit)
  }

  // 通道灯带
  for (let z = -6; z <= 6; z += 3) {
    const lightStrip = new THREE.Mesh(
      new THREE.BoxGeometry(0.2, 0.1, 10),
      new THREE.MeshBasicMaterial({ color: 0x0066ff, emissive: 0x0044aa })
    )
    lightStrip.position.set(0, 6.9, z)
    scene.add(lightStrip)
  }
}

const animate = () => {
  animationId = requestAnimationFrame(animate)

  const time = Date.now() * 0.001

  // 指示灯闪烁效果
  scene.traverse((child) => {
    if (child.geometry && child.geometry.type === 'SphereGeometry') {
      if (child.material && child.material.color) {
        const intensity = 0.7 + Math.sin(time * 3) * 0.3
        child.material.emissive = new THREE.Color(
          child.material.color.getHex()
        ).multiplyScalar(intensity)
      }
    }
  })

  // 摄像机缓慢移动
  camera.position.x = Math.sin(time * 0.2) * 12
  camera.position.z = Math.cos(time * 0.2) * 12
  camera.lookAt(0, 3, 0)

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
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border-radius: 8px;
  overflow: hidden;
}

.scene-info {
  position: absolute;
  top: 20px;
  left: 20px;
  color: #00ffff;
  z-index: 10;
  background: rgba(0, 0, 0, 0.8);
  padding: 15px;
  border-radius: 8px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 255, 255, 0.3);
}

.scene-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #00ffff;
}

.scene-info p {
  margin: 5px 0;
  font-size: 14px;
  opacity: 0.9;
  font-family: 'Courier New', monospace;
}
</style>
