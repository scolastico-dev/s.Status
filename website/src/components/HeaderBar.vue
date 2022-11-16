<template>
  <div class="header-bar">
    <h1 class="text-4xl">s.Status</h1>
    <div class="flex justify-end items-end">
      <p>Refresh in {{ refresh }}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    refresh: {
      type: Number,
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
