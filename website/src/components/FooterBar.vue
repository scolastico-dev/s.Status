<template>
  <div class="footer-bar">
    <translated text="footerLeft" />
    <translated text="footerRight" class="text-right" />
  </div>
</template>

<script>
import Translated from '@/Translated.vue'
export default {
  components: {Translated},
}
</script>

<style>
.footer-bar {
  @apply grid grid-cols-2 p-4 bg-white rounded-t-xl;
}
</style>
