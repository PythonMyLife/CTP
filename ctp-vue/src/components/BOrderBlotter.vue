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
          <li class="active">
            <a href="#"><i class="fa fa-table"></i> <span class="nav-label">Order Blotter</span></a>
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
          <h2>Order Blotter</h2>
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
                      <th>Trade ID</th>
                      <!--                        <th>Broker System</th>-->
                      <th>Product ID</th>
                      <th>Time</th>
                      <th>Type</th>
                      <th>Price</th>
                      <th>Amount</th>
                      <th>Trader 1</th>
                      <th>Broker 1</th>
                      <th>Trader 2</th>
                      <th>Broker 2</th>
                    </tr>
                    </thead>
                    <tbody>
                      <tr class="gradeA" v-for="(item, index) of orders" v-bind:key="index">
                        <td>{{ item.value.orderId }}</td>
                        <td>{{ item.value.productId }}</td>
                        <td>{{ item.time }}</td>
                        <td>{{ item.value.type }}</td>
                        <td>{{ item.value.price }}</td>
                        <td>{{ item.value.num }}</td>
                        <td>{{ item.value.trader }}</td>
                        <td>{{ item.value.broker }}</td>
                        <td>{{ item.value.traderTwoId }}</td>
                        <td></td>
                      </tr>
                      <tr class="gradeA" v-for="(item, index) of trades" v-bind:key="index">
                        <td>{{ item.id }}</td>
                        <td>{{ item.productId }}</td>
                        <td>{{ item.time }}</td>
                        <td></td>
                        <td>{{ item.price }}</td>
                        <td>{{ item.num }}</td>
                        <td>{{ item.traderSell }}</td>
                        <td>{{ item.brokerSell }}</td>
                        <td>{{ item.traderBuy }}</td>
                        <td>{{ item.brokerBuy }}</td>
                      </tr>
                    </tbody>
                    <tfoot>
                    </tfoot>
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
      product_id: '0',
      orders: [
        {
          time: '2020-06-08 :07:46:14',
          value: {
            orderId: '',
            type: null,
            price: 26.66,
            num: 100,
            trader: 'q',
            traderTwoId: 'p',
            broker: 's',
            productId: 'aa'
          }
        }
      ],
      trades: [
        {
          time: '',
          id: '',
          price: '',
          num: 0,
          traderSell: '',
          traderBuy: '',
          brokerSell: '',
          brokerBuy: '',
          productId: ''
        }
      ]
    }
  },
  mounted () {
    $(document).ready(function () {
      $('.dataTables-example').DataTable({
        pageLength: 25,
        responsive: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
          { extend: 'copy' },
          { extend: 'csv' },
          { extend: 'excel', title: 'ExampleFile' },
          { extend: 'pdf', title: 'ExampleFile' },
          {
            extend: 'print',
            customize: function (win) {
              $(win.document.body).addClass('white-bg')
              $(win.document.body).css('font-size', '10px')

              $(win.document.body).find('table')
                .addClass('compact')
                .css('font-size', 'inherit')
            }
          }
        ]
      })
    })
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
      this.initWebsocket()
    },
    initWebsocket () {
      const url = 'ws://202.120.40.8:30402/websocket/api/broker/orders'
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
      const msg = JSON.parse(evt.data)
      console.log(msg)
      this.orders = msg.orders
      this.trades = msg.transactions
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
</style>
