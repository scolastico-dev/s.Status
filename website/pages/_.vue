<template>
  <div id="body-wrapper" class="flex justify-center bg-gradient-to-br from-gray-700 to-gray-800 text-white">
    <div class="w-8/12 flex flex-col min-h-screen">
      <header id="title-bar" class="my-2 grid grid-cols-1 md:grid-cols-2">
        <div class="flex justify-center md:justify-start text-6xl">
          <h1>s.Status</h1>
        </div>
        <div class="flex justify-center md:items-end md:justify-end">
          <p>Next update in X seconds.</p>
        </div>
      </header>

      <main class="flex-grow">
        <status-card></status-card>
        <status-card></status-card>
      </main>

      <div class="my-2">
        <p>footer</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'IndexPage',
  fetchOnServer: false,
  async asyncData(context) {
    const status = await context.$axios.$get('status').catch(() => {
      context.error(new Error("Failed to get API status."))
      return {status: null}
    })
    return {status}
  },
  data: () => ({
    status: {}
  })
}
</script>

<style>
body, #body-wrapper {
  @apply min-h-screen min-w-full
}
</style>
