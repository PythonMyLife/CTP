<template>
  <div class="middle-box text-center loginscreen   animated fadeInDown">
    <div>
      <div>
        <h1 class="logo-name">CTP</h1>
      </div>
      <h3>Register to CTP</h3>
      <div class="m-t" role="form">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Username" required="" v-model="form.username">
        </div>
        <div class="form-group">
          <input type="password" class="form-control" placeholder="Password" required="" v-model="form.password">
        </div>
        <div class="form-group">
          <select class="form-control" v-model="id">
            <option v-for="(item, index) in identity" v-bind:key="index">{{ item.id }}</option>
          </select>
        </div>
        <button class="btn btn-primary block full-width m-b" @click="submit()">Register</button>
        <p class="text-muted text-center"><small>Already have an account?</small></p>
        <router-link :to="{ name : 'Login' }" class="btn btn-sm btn-white btn-block">Login</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      form: {
        username: '',
        password: ''
      },
      id: '',
      identity: [
        {
          id: 'trader'
        },
        {
          id: 'broker'
        }
      ]
    }
  },
  mounted () {
  },
  methods: {
    submit () {
      let url = ''
      if (this.id === 'trader') {
        url = '/tui/register'
        const param = {
          params: this.form
        }
        this.$axios.get(url, param).then(response => {
          alert('注册成功！')
        })
      } else if (this.id === 'broker') {
        url = '/bui/api/broker/register'
        const head = {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }
        // const self = this
        this.$axios.post(url, this.form, head).then(response => {
          alert('注册成功！')
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
