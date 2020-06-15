<template>
  <div class="middle-box text-center loginscreen animated fadeInDown">
    <div>
      <div>
        <h1 class="logo-name">CTP</h1>
      </div>
      <h3>Welcome to CTP</h3>
      <p>The Commodities Transaction Platform.</p>
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
        <button class="btn btn-primary block full-width m-b" @click="submit()">Login</button>
        <p class="text-muted text-center"><small>Do not have an account?</small></p>
        <router-link :to="{ name : 'Register' }" class="btn btn-sm btn-white btn-block">Create an account</router-link>
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
        url = '/tui/login'
        const param = {
          params: this.form
        }
        const self = this
        this.$axios.get(url, param).then(response => {
          localStorage.setItem('username', self.form.username)
          localStorage.setItem('id', 'trader')
          this.$router.push({ name: 'Trader' })
        })
      } else if (this.id === 'broker') {
        url = '/bui/api/broker/login'
        const head = {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }
        const self = this
        this.$axios.post(url, this.form, head).then(response => {
          localStorage.setItem('username', self.form.username)
          localStorage.setItem('id', 'broker')
          this.$router.push({ name: 'Broker' })
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
