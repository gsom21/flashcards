import {ref, computed} from 'vue'
import {defineStore} from 'pinia'

export const useMainStore = defineStore('main', () => {
    const authorized = ref(false)
    const token = ref('')

    const nightMode = localStorage.getItem('nightMode')
    const isNightModeOn = ref(nightMode === 'true')
    const root = document.querySelector(':root');
    const defaultBodyBg = getComputedStyle(root).getPropertyValue('--bs-body-bg');
    const defaultBodyColor = getComputedStyle(root).getPropertyValue('--bs-body-color');
    const defaultCardBg = getComputedStyle(root).getPropertyValue('--card-bg');
    const defaultCardBoxShadow = getComputedStyle(root).getPropertyValue('--card-box-shadow');
    const defaultBorderColor = getComputedStyle(root).getPropertyValue('--bs-border-color');

    function setModeStyles(nightMode: boolean) {
        if (root == null) {
            return;
        }

        if (nightMode) {
            root.style.setProperty('--bs-body-bg', '#1a1a1a');
            root.style.setProperty('--bs-body-color', 'rgba(255, 255, 255, .87)');
            root.style.setProperty('--card-bg', '#242424');
            root.style.setProperty('--card-box-shadow', 'none');
            root.style.setProperty('--bs-border-color', '#242424');
        } else {
            root.style.setProperty('--bs-body-bg', defaultBodyBg);
            root.style.setProperty('--bs-body-color', defaultBodyColor);
            root.style.setProperty('--card-bg', defaultCardBg);
            root.style.setProperty('--card-box-shadow', defaultCardBoxShadow);
            root.style.setProperty('--bs-border-color', defaultBorderColor);
        }
    }

    setModeStyles(isNightModeOn.value)

    function toggleNightMode() {
        isNightModeOn.value = !isNightModeOn.value
        localStorage.setItem('nightMode', isNightModeOn.value ? 'true' : 'false')
        setModeStyles(isNightModeOn.value)
    }

    return {authorized, token, isNightModeOn, toggleNightMode}
})
