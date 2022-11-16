import {createApp} from 'vue'
import App from './App.vue'
import Axios from 'axios'
import mitt from 'mitt'

const app = createApp(App)

app.config.globalProperties.$axios = Axios.create({
  baseURL: '/api/',
})

app.config.globalProperties.$bus = mitt()

app.mount('#app')
