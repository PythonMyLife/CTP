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
          <li>
            <router-link :to="{ name : 'TOrderBlotter' }"><i class="fa fa-table"></i> <span class="nav-label">Order Blotter</span></router-link>
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
          <h2>Make An Order</h2>
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
                <div class="form-group row"><label class="col-sm-2 col-form-label">Order Type</label>
                  <div class="col-sm-10">
                    <select class="form-control m-b" v-model="order.type">
                      <option v-for="(item, index) in types" v-bind:key="index">{{ item.name }}</option>
                    </select>
                  </div>
                </div>
                <div class="form-group row"><label class="col-sm-2 col-form-label">Action</label>
                  <div class="col-sm-10">
                    <select class="form-control m-b" v-model="order.action">
                      <option>Buy</option>
                      <option>Sell</option>
                    </select>
                  </div>
                </div>
                <div class="form-group row"><label class="col-sm-2 col-form-label">Price</label>
                  <div class="col-sm-10">
                    <div class="input-group m-b">
                      <div class="input-group-prepend">
                        <span class="input-group-addon">$</span>
                      </div>
                      <input type="text" class="form-control" v-model="order.price">
                      <div class="input-group-append">
                        <span class="input-group-addon">.00</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-group row"><label class="col-sm-2 col-form-label">Quantity</label>
                  <div class="col-sm-10">
                    <div class="input-group m-b">
                      <input type="text" class="form-control" v-model="order.num">
                      <div class="input-group-append">
                        <span class="input-group-addon">.00</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-group row"><label class="col-sm-2 col-form-label">Broker</label>
                  <div class="col-sm-10">
                    <select class="form-control m-b" v-model="order.brokerId">
                      <option v-for="(item, index) in brokers" v-bind:key="index">{{ item.id }}</option>
                    </select>
                  </div>
                </div>
                <div class="hr-line-dashed"></div>
                <div class="form-group row">
                  <div class="col-sm-4 col-sm-offset-2">
                    <button class="btn btn-white btn-sm" @click="cancel()">Cancel</button>
                    <button class="btn btn-primary btn-sm" @click="submit()">Submit</button>
                  </div>
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
      product: {
        id: '',
        name: '',
        category: '',
        period: ''
      },
      brokers: [
        {
          id: 'broker1'
        },
        {
          id: 'broker2'
        }
      ],
      types: [
        {
          name: 'Market Order'
        },
        {
          name: 'Limit Order'
        },
        {
          name: 'Stop Order'
        },
        {
          name: 'Cancel Order'
        }
      ],
      order: {
        type: '',
        action: '',
        price: '',
        num: '',
        brokerId: '',
        traderId: '',
        productId: ''
      }
    }
  },
  mounted () {
    this.getParams()
  },
  methods: {
    getParams () {
      this.product.id = this.$route.params.pid
      this.product.name = this.$route.params.pname
      this.product.category = this.$route.params.pcategory
      this.product.period = this.$route.params.pperiod
      this.username = this.$route.params.username
    },
    loadBroker () {
      const url = '/getBrokerAll'
      const param = {
        params: {
          name: this.username
        }
      }
      this.$axios.get(url, param).then(response => {
        this.brokers = response.data
      })
    },
    cancel () {
      this.$router.push({ name: 'Trader' })
    },
    submit () {
      this.order.productId = this.product.id
      this.order.traderId = this.username
      console.log(this.order)
      const url = '/saveOrder'
      const param = {
        params: this.order
      }
      this.$axios.get(url, param).then(response => {
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
</style>
