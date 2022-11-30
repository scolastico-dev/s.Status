<template>
  <div class="background">
    <div class="position">
      <div ref="container" class="container">
        <slot />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    vh: 0,
    scheduler: null,
  }),
  mounted() {
    document.addEventListener('resize', this.updateVh)
    this.scheduler = setInterval(this.updateVh, 1000)
    this.updateVh()
  },
  beforeUnmount() {
    if (this.scheduler) clearInterval(this.scheduler)
    document.removeEventListener('resize', this.updateVh)
  },
  methods: {
    updateVh() {
      this.vh = window.innerHeight * 0.01
      this.$refs.container.style.setProperty('--vh', `${this.vh}px`)
    },
  },
}
</script>

<style>
.background {
  @apply bg-gradient-to-br from-zinc-600 to-zinc-800;
  @apply w-full overflow-y-auto;
  scrollbar-gutter: stable;
}
.position {
  @apply w-full grid grid-cols-8;
}
.container {
  @apply min-h-screen col-span-8;
  @apply flex flex-col justify-center;
  @apply xl:col-start-3 xl:col-span-4;
  min-height: calc(var(--vh, 1vh) * 100);
}
</style>
