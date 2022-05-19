<template>
  <transition
    enter-active-class="animate__animated animate__fadeIn animate__fast"
    leave-active-class="animate__animated animate__fadeOut animate__fast"
    mode="out-in"
  >
    <div v-if="loading" id="loading-wrapper" class="background">
      <div
        class="absolute pb-6 -translate-y-full transition-opacity duration-500 opacity-0 flex justify-center items-center"
        :class="{'opacity-100': error !== null}"
      >
        <frown-icon color="#fff" size="30" />
        <div class="flex-grow ml-4 text-white">
          <h1 class="text-2xl">Whooops, that should not happen!</h1>
          <h2 class="text-sm">{{ "Error: " + (error !== null ? error : "(No error message to show!)") }}</h2>
        </div>
      </div>
      <div>
        <transition
          enter-active-class="animate__animated animate__fadeIn animate__faster"
          leave-active-class="animate__animated animate__fadeOut animate__faster"
          mode="out-in"
        >
          <div v-if="error !== null" key="error-icon">
            <alert-triangle-icon
              color="#b91c1c"
              size="60"
            />
          </div>
          <div v-if="error === null" key="loading-spinner">
            <self-building-square-spinner
              color="#fff"
              size="60"
            />
          </div>
        </transition>
      </div>
    </div>
  </transition>
</template>

<script>
import 'animate.css'
import { SelfBuildingSquareSpinner  } from 'epic-spinners'
import { AlertTriangleIcon, FrownIcon } from 'vue-feather-icons'
export default {
  components: {
    SelfBuildingSquareSpinner,
    AlertTriangleIcon,
    FrownIcon
  },
  data: () => ({
    loading: false,
    error: null,
  }),
  methods: {
    start() {
      this.loading = true
    },
    finish() {
      this.loading = false
    },
    fail(error) {
      this.error = error
    }
  }
}
</script>

<style>
#loading-wrapper {
  @apply z-[100] fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center;
}
</style>
