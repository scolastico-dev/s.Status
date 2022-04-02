<template>
  <div class="my-4">
    <div class="w-full bg-stone-600 rounded">
      <div class="flex items-center p-2 select-none">
        <arrow-down-circle-icon
          class="cursor-pointer transition-transform duration-500"
          :class="{'rotate-180': dropdown}"
          @click="toggle"
        ></arrow-down-circle-icon>
        <p class="mx-2 flex-grow">content</p>
        <sun-icon class="text-green-500"></sun-icon>
        <cloud-drizzle-icon class="text-yellow-400"></cloud-drizzle-icon>
        <cloud-lightning-icon class="text-red-600"></cloud-lightning-icon>
      </div>
    </div>
    <div ref="wrapper" class="relative w-full bg-stone-600 rounded-b overflow-hidden h-0" :class="{'transition-height': transition, 'duration-500': transition}">
      <div ref="content" class="p-2">

      </div>
    </div>
  </div>
</template>

<script>
import { ArrowDownCircleIcon, CloudLightningIcon, CloudDrizzleIcon, SunIcon} from 'vue-feather-icons'
export default {
  name: 'SmallChartComponent',
  components: {
    ArrowDownCircleIcon,
    CloudLightningIcon,
    CloudDrizzleIcon,
    SunIcon,
  },
  data() {
    return {
      dropdown: false,
      transition: false,
    }
  },
  fetchOnServer: false,
  fetch() {
    window.addEventListener("resize", this.resize);
  },
  destroyed() {
    window.removeEventListener("resize", this.resize);
  },
  methods: {
    toggle() {
      const wrapper = this.$refs.wrapper
      this.dropdown = !this.dropdown
      if (this.dropdown) {
        wrapper.style.height = this.$refs.content.clientHeight + 'px'
        wrapper.addEventListener('transitionend', this.transitionEnd)
        this.transition = true
      } else {
        wrapper.style.height = '0'
        this.transition = true
      }
    },
    resize() {
      if (this.dropdown) {
        this.$refs.wrapper.style.height = this.$refs.content.clientHeight + 'px'
      }
    },
    transitionEnd() {
      const wrapper = this.$refs.wrapper
      wrapper.removeEventListener('transitionend', this.transitionEnd)
      this.transition = false
    },
  }
}
</script>

<style>

</style>
