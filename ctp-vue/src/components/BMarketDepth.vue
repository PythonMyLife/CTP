<template>
  <div id="wrapper">
    <nav class="navbar-default navbar-static-side" role="navigation">
      <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
          <li class="nav-header">
            <div class="dropdown profile-element" align="center">
              <img alt="image" class="rounded-circle" src="../assets/img/profile_small.jpg"/>
              <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                <span class="block m-t-xs font-bold">{{ username }}</span>
                <span class="text-muted text-xs block">Broker</span>
              </a>
            </div>
            <div class="logo-element">
              CTP
            </div>
          </li>
          <li>
            <router-link :to="{ name : 'BOrderBlotter' }"><i class="fa fa-table"></i> <span class="nav-label">Order Blotter</span></router-link>
          </li>
          <li>
            <router-link :to="{ name : 'Broker' }"><i class="fa fa-shopping-cart"></i> <span class="nav-label">Commodity Trade</span></router-link>
          </li>
        </ul>
      </div>
    </nav>

    <div id="page-wrapper" class="gray-bg dashbard-1">
      <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
          <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
          </div>
          <ul class="nav navbar-top-links navbar-right">
            <li style="padding: 20px">
              <span class="m-r-sm text-muted welcome-message">Welcome to Commodities Transaction Platform.</span>
            </li>
            <li>
              <router-link :to="{ name : 'Login' }"><i class="fa fa-sign-out"></i>Logout</router-link>
            </li>
          </ul>
        </nav>
      </div>
      <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
          <h2>Market Depth</h2>
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <a>{{ product.category }}</a>
            </li>
            <li class="breadcrumb-item">
              <a>{{ product.period }}</a>
            </li>
            <li class="breadcrumb-item active">
              <strong>{{ product.name }}</strong>
            </li>
          </ol>
        </div>
        <div class="col-lg-2">
        </div>
      </div>
      <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
          <div class="col-lg-12">
            <div class="ibox ">
              <div class="ibox-content">
                <div class="table-responsive">
                  <table class="table table-striped table-bordered table-hover dataTables-example" >
                    <thead>
                    <tr>
                      <th>Level</th>
                      <th>Buy Vol</th>
                      <th>Price</th>
                      <th>Sell Vol</th>
                      <th>Level</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="gradeA" v-for="(item, index) of sell" v-bind:key="index">
                      <td></td>
                      <td></td>
                      <td>{{ item.price }}</td>
                      <td>{{ item.num }}</td>
                      <td>{{ item.level }}</td>
                    </tr>
                    <tr class="gradeA" v-for="(item, index) of buy" v-bind:key="index">
                      <td>{{ item.level }}</td>
                      <td>{{ item.num }}</td>
                      <td>{{ item.price }}</td>
                      <td></td>
                      <td></td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      username: 'Broker1',
      product: {
        id: '',
        name: '',
        category: '',
        period: ''
      },
      buy: [
        // {
        //   level: 1,
        //   vol: 90,
        //   price: 1248
        // },
        // {
        //   level: 2,
        //   vol: 290,
        //   price: 1246
        // },
        // {
        //   level: 3,
        //   vol: 187,
        //   price: 1244
        // }
      ],
      sell: [
        // {
        //   level: 3,
        //   vol: 100,
        //   price: 1250
        // },
        // {
        //   level: 2,
        //   vol: 32,
        //   price: 1252
        // },
        // {
        //   level: 1,
        //   vol: 127,
        //   price: 1254
        // }
      ],
      wsocket: null
    }
  },
  mounted () {
    this.check()
  },
  destroyed () {
    this.wsocket.close()
  },
  methods: {
    check () {
      this.username = localStorage.getItem('username')
      const id = localStorage.getItem('id')
      if (id !== 'broker' || this.username === '') {
        alert('未登录或身份不正确！')
        this.$router.push({ name: 'Login' })
      }
      this.getParams()
    },
    getParams () {
      this.product.id = this.$route.params.pid
      this.product.name = this.$route.params.pname
      this.product.category = this.$route.params.pcategory
      this.product.period = this.$route.params.pperiod
      this.initWebsocket()
    },
    initWebsocket () {
      const url = 'ws://202.120.40.8:30402/websocket/api/broker/depth/' + this.product.id
      this.wsocket = new WebSocket(url)
      this.wsocket.onopen = this.onOpen
      this.wsocket.onmessage = this.onMessage
      this.wsocket.onerror = this.onError
      this.wsocket.onclose = this.onClose
    },
    onOpen () {
      const joinMsg = {}
      joinMsg.type = 'join'
      joinMsg.username = this.username
      joinMsg.productId = this.product.id
      const jsonstr = JSON.stringify(joinMsg)
      this.wsocket.send(jsonstr)
      console.log('connect to ws.')
    },
    onError () {
      console.log('connect error')
    },
    onClose (e) {
      console.log('connect close', e)
    },
    onMessage (evt) {
      this.buy = []
      this.sell = []
      const msg = JSON.parse(evt.data)
      msg.sort(function (a, b) {
        if (a.price > b.price) {
          return -1
        } else if (a.price === b.price) {
          return 0
        } else {
          return 1
        }
      })
      for (let i = 0; i < msg.length; i++) {
        // console.log(msg[i])
        // if (m.type !== 'stopBuy' && m.type !== 'stopSell') {
        if (msg[i].action === 'buy') {
          if (this.buy.length > 0 && this.buy[this.buy.length - 1].price === msg[i].price) {
            this.buy[this.buy.length - 1].num += msg[i].num
          } else {
            this.buy.push(msg[i])
          }
        } else {
          if (this.sell.length > 0 && this.sell[this.sell.length - 1].price === msg[i].price) {
            // console.log(this.sell.length)
            // console.log(this.sell[this.sell.length - 1])
            this.sell[this.sell.length - 1].num += msg[i].num
          } else {
            this.sell.push(msg[i])
          }
        }
        // }
      }
      for (let i = 0; i < this.buy.length; i++) {
        this.buy[i].level = i + 1
      }
      for (let i = 0; i < this.sell.length; i++) {
        this.sell[i].level = this.sell.length - i
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
</style>
