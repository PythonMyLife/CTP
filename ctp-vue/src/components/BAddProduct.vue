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
          <h2>Add New Product</h2>
        </div>
        <div class="col-lg-2">
        </div>
      </div>
      <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
          <div class="col-lg-12">
            <div class="ibox ">
              <div class="ibox-content">
                <div class="form-group  row"><label class="col-sm-2 col-form-label">Name</label>
                  <div class="col-sm-10"><input type="text" class="form-control" v-model="product.name"></div>
                </div>
                <div class="form-group  row"><label class="col-sm-2 col-form-label">Category</label>
                  <div class="col-sm-10"><input type="text" class="form-control" v-model="product.category"></div>
                </div>
                <div class="form-group  row"><label class="col-sm-2 col-form-label">Period</label>
                  <div class="col-sm-10"><input type="text" class="form-control" v-model="product.period"></div>
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
      username: 'Broker1',
      product_id: '0',
      product: {
        name: '',
        category: '',
        period: ''
      }
    }
  },
  mounted () {
    this.check()
  },
  methods: {
    check () {
      this.username = localStorage.getItem('username')
      const id = localStorage.getItem('id')
      if (id !== 'broker' || this.username === '') {
        alert('未登录或身份不正确！')
        this.$router.push({ name: 'Login' })
      }
    },
    cancel () {
      this.$router.push({ name: 'Broker' })
    },
    submit () {
      const url = '/bui/api/broker/addProduct'
      const head = {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }
      // const self = this
      this.$axios.post(url, this.product, head).then(response => {
        if (response.data.status === 1) {
          alert('添加成功！')
        } else {
          alert('添加失败！')
        }
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
</style>
