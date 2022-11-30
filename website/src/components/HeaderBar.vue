<template>
  <div class="header-bar">
    <h1 class="text-4xl">
      <translated element="h1" text="title" />
    </h1>
    <div class="flex justify-end items-end">
      <translated v-if="!error && refresh === 0" text="refreshing" />
      <translated v-else-if="!error" text="refresh" :values="{refresh}" />
      <translated v-else text="refreshingError" text-tooltip="refreshingErrorDetails" />
    </div>
  </div>
</template>

<script>
import Translated from '@/Translated.vue'
export default {
  components: {Translated},
  props: {
    refresh: {
      type: Number,
      required: true,
    },
    error: {
      type: Boolean,
      required: true,
    },
  },
}
</script>

<style>
.header-bar {
  @apply grid grid-cols-2 p-4 pt-8 bg-white;
  @apply rounded-b-xl
}
</style>
