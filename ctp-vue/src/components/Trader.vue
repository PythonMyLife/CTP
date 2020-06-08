<template>
  <div id="wrapper">
    <nav class="navbar-default navbar-static-side" role="navigation">
      <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
          <li class="nav-header">
            <div class="dropdown profile-element" align="center">
              <img alt="image" class="rounded-circle" src="../assets/img/profile_small.jpg"/>
              <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                <span class="block m-t-xs font-bold">David Williams</span>
                <span class="text-muted text-xs block">Trader</span>
              </a>
            </div>
            <div class="logo-element">
              CTP
            </div>
          </li>
          <li>
            <router-link :to="{ name : 'MarketDepth' }"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">Market Depth</span></router-link>
          </li>
          <li>
            <router-link :to="{ name : 'OrderBlotter' }"><i class="fa fa-table"></i> <span class="nav-label">Order Blotter</span></router-link>
          </li>
          <li class="active">
            <a href="#"><i class="fa fa-shopping-cart"></i> <span class="nav-label">Commodity Trade</span></a>
          </li>
        </ul>
      </div>
    </nav>

    <div id="page-wrapper" class="gray-bg dashbard-1">
      <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
          <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href=""><i class="fa fa-bars"></i> </a>
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
      <div>
        <div class="row wrapper border-bottom white-bg page-heading">
          <div class="col-lg-10">
            <h2>Commodity list</h2>
          </div>
          <div class="col-lg-2">
          </div>
        </div>
        <div class="wrapper wrapper-content animated fadeInRight ecommerce">

          <div class="ibox-content m-b-sm border-bottom">
            <div class="row">
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="col-form-label" for="product_name">Product Name</label>
                  <input type="text" id="product_name" name="product_name" value="" placeholder="Product Name" class="form-control">
                </div>
              </div>
              <div class="col-sm-2">
                <div class="form-group">
                  <label class="col-form-label" for="price">Price</label>
                  <input type="text" id="price" name="price" value="" placeholder="Price" class="form-control">
                </div>
              </div>
              <div class="col-sm-2">
                <div class="form-group">
                  <label class="col-form-label" for="quantity">Quantity</label>
                  <input type="text" id="quantity" name="quantity" value="" placeholder="Quantity" class="form-control">
                </div>
              </div>
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="col-form-label" for="status">Status</label>
                  <select name="status" id="status" class="form-control">
                    <option value="1" selected>Enabled</option>
                    <option value="0">Disabled</option>
                  </select>
                </div>
              </div>
            </div>

          </div>

          <div class="row">
            <div class="col-lg-12">
              <div class="ibox">
                <div class="ibox-content">

                  <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="15">
                    <thead>
                      <tr>
                        <th data-toggle="true">Product Name</th>
                        <th data-hide="phone">Model</th>
                        <th data-hide="all">Description</th>
                        <th data-hide="phone">Price</th>
                        <th data-hide="phone,tablet" >Quantity</th>
                        <th data-hide="phone">Status</th>
                        <th class="text-right" data-sort-ignore="true">Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>
                          Example product 1
                        </td>
                        <td>
                          Model 1
                        </td>
                        <td>
                          It is a long established fact that a reader will be distracted by the readable
                          content of a page when looking at its layout. The point of using Lorem Ipsum is
                          that it has a more-or-less normal distribution of letters, as opposed to using
                          'Content here, content here', making it look like readable English.
                        </td>
                        <td>
                          $50.00
                        </td>
                        <td>
                          1000
                        </td>
                        <td>
                          <span class="label label-primary">Enable</span>
                        </td>
                        <td class="text-right">
                          <div class="btn-group">
                            <button class="btn-white btn btn-xs">View</button>
                            <button class="btn-white btn btn-xs">Edit</button>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          Example product 2
                        </td>
                        <td>
                          Model 2
                        </td>
                        <td>
                          It is a long established fact that a reader will be distracted by the readable
                          content of a page when looking at its layout. The point of using Lorem Ipsum is
                          that it has a more-or-less normal distribution of letters, as opposed to using
                          'Content here, content here', making it look like readable English.
                        </td>
                        <td>
                          $40.00
                        </td>
                        <td>
                          4300
                        </td>
                        <td>
                          <span class="label label-primary">Enable</span>
                        </td>
                        <td class="text-right">
                          <div class="btn-group">
                            <button class="btn-white btn btn-xs">View</button>
                            <button class="btn-white btn btn-xs">Edit</button>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          Example product 3
                        </td>
                        <td>
                          Model 3
                        </td>
                        <td>
                          It is a long established fact that a reader will be distracted by the readable
                          content of a page when looking at its layout. The point of using Lorem Ipsum is
                          that it has a more-or-less normal distribution of letters, as opposed to using
                          'Content here, content here', making it look like readable English.
                        </td>
                        <td>
                          $22.00
                        </td>
                        <td>
                          300
                        </td>
                        <td>
                          <span class="label label-danger">Disabled</span>
                        </td>
                        <td class="text-right">
                          <div class="btn-group">
                            <button class="btn-white btn btn-xs">View</button>
                            <button class="btn-white btn btn-xs">Edit</button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                    <tfoot>
                      <tr>
                        <td colspan="6">
                          <ul class="pagination float-right"></ul>
                        </td>
                      </tr>
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
    }
  },
  mounted () {
    $(document).ready(function () {
      $('.footable').footable()
    })
  },
  methods: {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">

</style>
