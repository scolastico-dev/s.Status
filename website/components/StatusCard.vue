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
        <sun-icon class="mx-2 text-green-500"></sun-icon>
        <cloud-drizzle-icon class="mx-2 text-yellow-400 animate__animated animate__headShake animate__infinite"></cloud-drizzle-icon>
        <cloud-lightning-icon class="mx-2 text-red-600 animate__animated animate__headShake animate__infinite"></cloud-lightning-icon>
      </div>
    </div>
    <dropwdown-card ref="dropdown" class="-mt-2">
      <div ref="wrapper" class="w-full bg-stone-600 rounded-b">
        <div ref="content" class="p-2">

        </div>
      </div>
    </dropwdown-card>
  </div>
</template>

<script>
import 'animate.css'
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
  methods: {
    toggle() {
      if (this.$refs.dropdown.block) return
      this.dropdown = !this.dropdown
      this.$refs.dropdown.toggleHide(!this.dropdown)
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
