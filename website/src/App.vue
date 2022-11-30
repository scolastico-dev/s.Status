<template>
  <background>
    <header-bar :refresh="refresh" :error="error" />
    <div class="flex-grow">
      <transition-group
          enter-active-class="duration-300 ease-out"
          enter-from-class="transform opacity-0"
          enter-to-class="opacity-100"
          leave-active-class="duration-200 ease-in"
          leave-from-class="opacity-100"
          leave-to-class="transform opacity-0"
      >
        <card
            v-for="service of services"
            :key="service.name"
            :service="service"
        />
      </transition-group>
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
    error: false,
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
      try {
        if (this.refreshing) return
        this.refresh--
        if (this.refresh <= 0) {
          this.refreshing = true
          this.$bus.emit('refresh')
          await this.refreshStatus()
          this.refreshing = false
        }
      } catch (e) {
        console.error('Could not get status! Is the API running?', e)
        clearInterval(this.scheduler)
        this.error = true
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

a {
  @apply transition-colors duration-300;
  @apply italic hover:text-blue-900;
}
</style>
