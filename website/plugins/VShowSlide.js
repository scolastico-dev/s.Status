import Vue from 'vue'
import VShowSlide from "v-show-slide";
export default () => {
  Vue.use(VShowSlide, {
    customEasing: {
      tailwind: 'cubic-bezier(0.4, 0, 0.2, 1)',
    },
  })
}
