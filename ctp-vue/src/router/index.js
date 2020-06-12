import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login'
import Register from '../components/Register'
import Trader from '../components/Trader'
import Broker from '../components/Broker'
import TOrderBlotter from '../components/TOrderBlotter'
import TMarketDepth from '../components/TMarketDepth'
import TMakeOrder from '../components/TMakeOrder'
import BMarketDepth from '../components/BMarketDepth'
import BOrderBlotter from '../components/BOrderBlotter'
import BAddProduct from '../components/BAddProduct'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Login
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
    path: '/tOrderBlotter',
    name: 'TOrderBlotter',
    component: TOrderBlotter
  },
  {
    path: '/tMarketDepth',
    name: 'TMarketDepth',
    component: TMarketDepth
  },
  {
    path: '/tMakeOrder',
    name: 'TMakeOrder',
    component: TMakeOrder
  },
  {
    path: '/bMarketDepth',
    name: 'BMarketDepth',
    component: BMarketDepth
  },
  {
    path: '/bOrderBlotter',
    name: 'BOrderBlotter',
    component: BOrderBlotter
  },
  {
    path: '/bAddProduct',
    name: 'BAddProduct',
    component: BAddProduct
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
