<script setup lang="ts">
import Login from './components/Login.vue'
import {useMainStore} from '@/stores/main.ts'
import {onMounted, ref, watch} from "vue";

let store = useMainStore()

function logout() {
  store.authorized = false
  store.token = ''
  localStorage.removeItem('token')
}
//
// const isNightModeOn = ref(false)
//
// function toggleNightMode() {
//   let r = document.querySelector(':root');
//
//   if (r !== null) {
//     console.log("not null")
//     r.style.setProperty('--bs-body-bg', '#1a1a1a');
//     r.style.setProperty('--bs-body-color', 'rgba(255, 255, 255, .87)');
//     r.style.setProperty('--card-bg', '#38444d');
//
//     return
//   }
//
//   console.log("null")
// }

onMounted(async () => {
  let token = localStorage.getItem('token')

  let isTokenValid = token != null && token !== ''

  if (isTokenValid) {
    store.authorized = true
    store.token = token
    return
  }

  store.authorized = false
  store.token = ''
  localStorage.removeItem('token')
})

</script>

<template>
  <notifications/>
  <Login v-if="!store.authorized"/>
  <div v-else>
    <nav class="py-2 border-bottom mb-4">
      <div class="container d-flex flex-wrap header">
        <ul class="nav me-auto">
          <li class="nav-item">
            <RouterLink to="/" class="nav-link px-2 active" aria-current="page">Home</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/import" class="nav-link px-2">Import</RouterLink>
          </li>
        </ul>
        <ul class="nav">
<!--          <li class="nav-item">-->
<!--            <div class="mt-2 mr-1">-->
<!--              <i class="bi-moon" @click="toggleNightMode"></i>-->
<!--            </div>-->
<!--          </li>-->
          <li class="nav-item" v-if="store.authorized" @click="logout">
            <a href="" class="nav-link px-2">Logout</a>
          </li>
        </ul>
      </div>
    </nav>
    <RouterView/>
  </div>
</template>

<style scoped>
.header .nav-link {
  color: var(--bs-body-color);
}

</style>
