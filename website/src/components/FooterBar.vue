<template>
  <div class="footer-bar">
    <div class="footer-bar-child">
      <translated text="footerLeft" />
    </div>
    <div class="relative">
      <div class="lang-icon">
        <img :src="globeIcon" alt="" class="w-[1.25rem] h-[1.25rem]"/>
      </div>
      <select v-model="lang" class="lang-selector">
        <option v-for="lang in $lang.available" :key="lang" :value="lang">{{ lang }}</option>
      </select>
    </div>
    <div class="footer-bar-child justify-end">
      <translated text="footerRight" />
    </div>
  </div>
</template>

<script>
import Translated from '@/Translated.vue'
import globeIcon from 'feather-icons/dist/icons/globe.svg?url'
export default {
  components: {Translated},
  data: () => ({
    lang: '',
    globeIcon: globeIcon,
  }),
  watch: {
    lang(newVal, oldVal) {
      if (!oldVal || oldVal === '' || oldVal === newVal) return
      localStorage.setItem('lang', newVal)
      window.location.reload()
    },
  },
  beforeMount() {
    this.lang = this.$lang.current
  },
}
</script>

<style>
.footer-bar {
  @apply grid grid-cols-7 p-4 bg-white rounded-t-xl;
}
.footer-bar-child {
  @apply col-span-3 flex items-center;
}
.lang-selector {
  @apply p-1 rounded border border-gray-300 text-sm;
  @apply outline-0 w-full pl-6;
}
.lang-icon {
  @apply absolute inset-y-0 left-0 select-none;
  @apply flex items-center ml-1;
  @apply pointer-events-none;
}
</style>
