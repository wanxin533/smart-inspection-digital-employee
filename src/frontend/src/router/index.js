import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/inspection',
    name: 'Inspection',
    component: () => import('@/views/Inspection/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/defect',
    name: 'Defect',
    component: () => import('@/views/Defect/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/work-order',
    name: 'WorkOrder',
    component: () => import('@/views/WorkOrder/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/predictive',
    name: 'Predictive',
    component: () => import('@/views/Predictive/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/knowledge',
    name: 'Knowledge',
    component: () => import('@/views/Knowledge/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/scenarios',
    name: 'Scenarios',
    component: () => import('@/views/Scenarios/Index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/scenarios/detail',
    name: 'ScenariosDetail',
    component: () => import('@/views/Scenarios/Detail.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
