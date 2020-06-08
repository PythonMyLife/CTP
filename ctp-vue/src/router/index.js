import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../components/HelloWorld.vue'
import Login from '../components/Login.vue'
import Register from '../components/Register'
import Trader from '../components/Trader'
import Broker from '../components/Broker'
import OrderBlotter from '../components/OrderBlotter'
import MarketDepth from '../components/MarketDepth'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/trader',
    name: 'Trader',
    component: Trader
  },
  {
    path: '/broker',
    name: 'Broker',
    component: Broker
  },
  {
    path: '/orderBlotter',
    name: 'OrderBlotter',
    component: OrderBlotter
  },
  {
    path: '/marketDepth',
    name: 'MarketDepth',
    component: MarketDepth
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
