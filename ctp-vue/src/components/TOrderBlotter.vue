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
                <span class="text-muted text-xs block">Trader</span>
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
            <router-link :to="{ name : 'Trader' }"><i class="fa fa-shopping-cart"></i> <span class="nav-label">Commodity Trade</span></router-link>
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
                        <th></th>
                        <th>Trade ID</th>
                        <th>Broker System</th>
                        <th>Product ID</th>
                        <th>Time</th>
                        <th>Type</th>
                        <th>Price</th>
                        <th>Amount</th>
                        <th>Trader 1</th>
                        <th>Trader 2</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr class="gradeA" v-for="(item, index) of orders" v-bind:key="index">
                        <td>{{ index + 1 }}</td>
                        <td>{{ item.id }}</td>
                        <td>{{ item.brokerId }}</td>
                        <td>{{ item.commodityId }}</td>
                        <td>{{ item.time }}</td>
                        <td>{{ item.type }}</td>
                        <td>{{ item.price }}</td>
                        <td>{{ item.amount }}</td>
                        <td>{{ item.traderOneId }}</td>
                        <td>{{ item.traderTwoId }}</td>
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
      username: 'Trader1',
      product_id: '0',
      orders: [
        {
          id: 'qp2020-06-08 :07:46:14',
          time: '2020-06-08 :07:46:14',
          type: null,
          price: 26.66,
          amount: 100,
          traderOneId: 'q',
          traderTwoId: 'p',
          brokerId: 's',
          commodityId: 'aa'
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
  },
  methods: {
    loadData () {
      const url = '/getOrderByName'
      const param = {
        params: {
          name: this.username
        }
      }
      this.$axios.get(url, param).then(response => {
        this.orders = response.data
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
</style>
