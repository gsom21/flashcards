<script setup>
import {ref} from 'vue'
import axios from 'axios'
import {useMainStore} from '@/stores/main.ts';

let login = ref('')
let password = ref('')
let store = useMainStore()

function submit() {
  axios.post(import.meta.env.VITE_API_URL + '/user/login', {
    u: login.value,
    p: password.value,
  }).then(response => {
    if (response.status === 200) {
      store.authorized = true
      store.token = "Basic " + btoa(login.value + ":" + password.value)
      localStorage.setItem('token', store.token)
    }
    console.log(response)
  }).catch(error => {
    console.log(error)
  })
}

</script>

<template>
  <div class="container vh-100 d-flex justify-content-center" style="align-items: flex-start;">
    <div class="row w-100 justify-content-center" style="margin-top: 30vh;">
      <div class="col-md-6 login-form">
        <label class="form-label" for="login">Login: </label>
        <input class="form-control" placeholder="Login" v-model="login" id="login"/><br />
        <label class="form-label" for="password">Password: </label>
        <input class="form-control" placeholder="Password" id="password" v-model="password" type="password"/><br />
        <button @click="submit" class="btn btn-outline-primary">Sign In/Sign Up</button>
      </div>
    </div>
  </div>
</template>


<style scoped>
.login-form {
  background-color: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.2);
}
</style>