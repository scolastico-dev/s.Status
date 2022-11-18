<template>
  <div class="info-card" :class="{'error': error}">
    <alert-triangle />
    <translated element="h2" :text="text" :values="{notice}" />
  </div>
</template>

<script>
import Translated from '@/Translated.vue'
import AlertTriangle from 'feather-icons/dist/icons/alert-triangle.svg?component'
export default {
  components: {
    Translated,
    AlertTriangle,
  },
  props: {
    notice: {
      type: String,
      required: true,
    },
    text: {
      type: String,
      required: true,
    },
    error: {
      type: Boolean,
      required: true,
    },
  },
  data: () => ({
    AlertTriangle,
  }),
}
</script>

<style>
.info-card {
  @apply mt-4 p-2 rounded-xl border-2;
  @apply flex items-center w-full;
}
.info-card.error {
  @apply bg-red-200 border-red-400;
}
.info-card.error svg {
  @apply text-red-600 mr-2;
}
.info-card:not(.error) {
  @apply bg-yellow-200 border-yellow-400;
}
.info-card:not(.error) svg {
  @apply text-yellow-600 mr-2;
}
</style>
