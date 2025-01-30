import {ref, computed} from 'vue'
import {defineStore} from 'pinia'

export const useMainStore = defineStore('main', () => {
    const authorized = ref(false)
    const token = ref('')

    return {authorized, token}
})
