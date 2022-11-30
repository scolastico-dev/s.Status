<template>
  <div class="relative">
    <div class="drop-down-helper">
      <div ref="helper">
        <slot />
      </div>
    </div>
    <div
        ref="actual"
        class="drop-down"
        :style="{height}"
        @transitionend="onTransitionEnd"
    >
      <slot />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    open: {
      type: Boolean,
      required: true,
    },
  },
  data: () => ({
    state: false,
    height: 0,
    scheduler: null,
  }),
  beforeMount() {
    this.update(this.state)
  },
  beforeUnmount() {
    this.clearScheduler()
  },
  watch: {
    open(newVal) {
      this.update(newVal)
    },
  },
  methods: {
    update(newVal) {
      this.state = newVal
      if (!this.$refs.helper) {
        this.height = newVal ? 'auto' : 0
        return
      }
      if (newVal) {
        this.scheduler = setInterval(this.schedule, 25)
      } else {
        this.height = this.$refs.actual.offsetHeight + 'px'
        setTimeout(() => {
          this.height = 0
        }, 0)
        this.clearScheduler()
      }
    },
    schedule() {
      this.height = this.$refs.helper.scrollHeight + 'px'
    },
    onTransitionEnd() {
      if (this.state) {
        this.height = 'auto'
        this.clearScheduler()
      }
    },
    clearScheduler() {
      if (this.scheduler) clearInterval(this.scheduler)
    },
  },
}
</script>

<style>
.drop-down {
  @apply overflow-hidden;
  @apply transition-all duration-500;
}
.drop-down-helper {
  @apply absolute inset-x-0 top-0 h-0 overflow-hidden;
  @apply opacity-0 pointer-events-none;
}
</style>
