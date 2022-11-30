import {createApp} from 'vue'
import App from './App.vue'
import Axios from 'axios'
import mitt from 'mitt'

(async () => {
  console.log('s.Status made with ♥ by https://scolasti.co')
  const app = createApp(App)

  const axios = Axios.create({})
  let currentLang = localStorage.getItem('lang') || navigator.language
  const availableLang = (await axios('assets/languages.json')).data
  while (!availableLang.includes(currentLang)) {
    if (currentLang !== 'en-US') {
      currentLang = 'en-US'
    } else {
      currentLang = availableLang[0]
    }
  }
  localStorage.setItem('lang', currentLang)
  app.config.globalProperties.$lang = {
    current: currentLang,
    available: availableLang,
    data: (await axios(`assets/lang/${currentLang}.json`)).data,
    default: ((await axios(`assets/lang/default.json`).catch(() => null)) || {}).data,
  }

  app.config.globalProperties.$axios = Axios.create({
    baseURL: '/api/',
  })

  app.config.globalProperties.$bus = mitt()

  app.mount('#app')
})()
