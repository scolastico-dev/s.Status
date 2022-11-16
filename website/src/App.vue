<template>
  <background>
    <header-bar :refresh="refresh" />
    <div class="flex-grow">
      <card
          v-for="service of services"
          :key="service.name"
          :service="service"
      />
    </div>
    <footer-bar />
  </background>
</template>

<script>
import Background from '@/Background.vue'
import HeaderBar from '@/HeaderBar.vue'
import Card from '@/Card.vue'
import FooterBar from '@/FooterBar.vue'
export default {
  components: {
    Background,
    HeaderBar,
    Card,
    FooterBar,
  },
  data: () => ({
    services: [],
    refresh: 0,
    refreshing: false,
    scheduler: null,
  }),
  async beforeMount() {
    await this.refreshStatus()
    this.scheduler = setInterval(this.schedule, 1000)
  },
  beforeUnmount() {
    if (this.scheduler) clearInterval(this.scheduler)
  },
  methods: {
    async schedule() {
      if (this.refreshing) return
      this.refresh--
      if (this.refresh <= 0) {
        this.refreshing = true
        this.$bus.emit('refresh')
        await this.refreshStatus()
        this.refreshing = false
      }
    },
    async refreshStatus() {
      const status = await this.$axios('status')
      this.services = status.data.checks
      this.refresh = status.data.refreshInterval
    },
  },
}
</script>

<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
</style>
