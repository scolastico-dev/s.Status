<template>
  <div class="my-4">
    <div class="w-full bg-stone-600 rounded">
      <div class="flex items-center p-2 select-none">
        <arrow-down-circle-icon
          class="cursor-pointer transition-transform duration-500"
          :class="{'rotate-180': dropdown}"
          @click="toggle"
        ></arrow-down-circle-icon>
        <p class="mx-2 flex-grow">{}</p>
        <sun-icon class="mx-2 text-green-500"></sun-icon>
        <cloud-drizzle-icon class="mx-2 text-yellow-400 animate__animated animate__headShake animate__infinite"></cloud-drizzle-icon>
        <cloud-lightning-icon class="mx-2 text-red-600 animate__animated animate__headShake animate__infinite"></cloud-lightning-icon>
      </div>
    </div>
    <div v-show-slide:400:tailwind="dropdown" class="w-full bg-stone-600 rounded-b -mt-2 p-2">

    </div>
  </div>
</template>

<script>
import 'animate.css'
import {ArrowDownCircleIcon, CloudDrizzleIcon, CloudLightningIcon, SunIcon} from 'vue-feather-icons'

export default {
  name: 'SmallChartComponent',
  components: {
    ArrowDownCircleIcon,
    CloudLightningIcon,
    CloudDrizzleIcon,
    SunIcon,
  },
  props: {
    checkID: {
      type: String,
      required: true,
    },
  },
  data: () => ({
    dropdown: false,
    data: null,
  }),
  fetchOnServer: false,
  async fetch() {
    this.data = await this.$axios.$get('get/' + this.checkID + '/' + this.$timezoneOffset().toString() ).catch(() => {
      this.error(new Error("Failed to get API check data for '" + this.checkID + "'."))
    })

  },
  methods: {
    toggle() {
      this.dropdown = !this.dropdown
    },
    async update() {

    },
  },
}
</script>

<style>

</style>
