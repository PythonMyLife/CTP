<template>
  <div class="middle-box text-center loginscreen   animated fadeInDown">
    <div>
      <div>
        <h1 class="logo-name">CTP</h1>
      </div>
      <h3>Register to CTP</h3>
      <form class="m-t" role="form" action="login.html">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Name" required="">
        </div>
        <div class="form-group">
          <input type="password" class="form-control" placeholder="Password" required="">
        </div>
        <button type="submit" class="btn btn-primary block full-width m-b">Register</button>
        <p class="text-muted text-center"><small>Already have an account?</small></p>
        <router-link :to="{ name : 'Login' }" class="btn btn-sm btn-white btn-block">Login</router-link>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Register'
}
</script>

<style scoped>

</style>
