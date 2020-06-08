<template>
  <div class="middle-box text-center loginscreen animated fadeInDown">
    <div>
      <div>
        <h1 class="logo-name">CTP</h1>
      </div>
      <h3>Welcome to CTP</h3>
      <p>The Commodities Transaction Platform.</p>
      <form class="m-t" role="form" action="index.html">
        <div class="form-group">
          <input type="email" class="form-control" placeholder="Username" required="">
        </div>
        <div class="form-group">
          <input type="password" class="form-control" placeholder="Password" required="">
        </div>
        <button type="submit" class="btn btn-primary block full-width m-b">Login</button>
        <p class="text-muted text-center"><small>Do not have an account?</small></p>
        <router-link :to="{ name : 'Register' }" class="btn btn-sm btn-white btn-block">Create an account</router-link>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login.vue'
}
</script>

<style scoped>

</style>
