<template>
  <div id="body-wrapper" class="flex justify-center background text-white">
    <div class="w-8/12 flex flex-col min-h-screen">
      <header id="title-bar" class="my-2 grid grid-cols-1 md:grid-cols-2">
        <div class="flex justify-center md:justify-start text-6xl">
          <h1>s.Status</h1>
        </div>
        <div class="flex justify-center md:items-end md:justify-end">
          <p>Next update in {{ refresh }} seconds.</p>
        </div>
      </header>

      <main class="flex-grow">
        <status-card
          v-for="check in status.checks"
          ref="checks"
          :key="check.name"
          :check-i-d="check.name"
        ></status-card>
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
    status: {},
    refresh: 0,
    scheduler: null,
  }),
  beforeDestroy() {
    if (this.scheduler !== null) {
      window.clearInterval(this.scheduler)
    }
  },
  mounted() {
    const interval = this.status.refreshInterval
    this.refresh = interval
    this.scheduler = window.setInterval(
      () => {
        this.refresh -= 1
        if (this.refresh <= 0) {
          this.refresh = interval
          this.update()
        }
      },
      interval * 1000
    )
  },
  methods: {
    async update() {

    },
  }
}
</script>
