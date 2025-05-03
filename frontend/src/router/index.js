import { createRouter, createWebHistory } from 'vue-router'
import QuestionList from '../components/QuestionList.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../components/Home.vue')
  },
  {
    path: '/questions',
    name: 'QuestionList',
    component: QuestionList
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router