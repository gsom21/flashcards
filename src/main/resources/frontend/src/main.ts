import {createApp} from 'vue'
import {createPinia} from 'pinia'

import App from './App.vue'
import router from './router'
import axios from 'axios'
import {useMainStore} from "@/stores/main.ts"
import '@/assets/main.scss'

// Импорт стилей и JavaScript Bootstrap
// import 'bootstrap/dist/css/bootstrap.min.css'
// import 'bootstrap/dist/js/bootstrap.bundle.min.js'
// import 'bootstrap-icons/font/bootstrap-icons.min.css'
import Notifications from '@kyvg/vue3-notification'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Notifications)

app.mount('#app')

let store = useMainStore()
axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (error.response && (error.response.status === 403 || error.response.status === 401)) {
        store.authorized = false
        store.token = ''
        localStorage.removeItem('token')

        return Promise.reject(error);
    }
    return Promise.reject(error);
});