<template>
  <div
    ref="wrapper"
    class="overflow-y-hidden overflow-x-hidden"
    :style="'max-height:' + (hidden ? '0' : 'none') + ';'"
  >
    <div ref="detector">
      <div ref="content">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
export default {
  props: {
    hidden: {
      type: Boolean,
      default: true,
    },
  },
  data: () => ({
    hide: true,
    block: false,
  }),
  created() {
    this.hide = this.hidden
  },
  methods: {
    toggleHide(mode = null) {
      if (mode !== null) {
        if (mode && this.hide) {
          return
        } else if (!mode && !this.hide) return
      }
      if (!this.block) {
        const wrapper = this.$refs.wrapper
        const detector = this.$refs.detector
        if (detector.clientHeight > 0) {
          this.block = true
          wrapper.addEventListener('transitionend', this.endHideTransition)
        }
        this.hide = !this.hide
        if (this.hide) {
          wrapper.style.maxHeight = wrapper.clientHeight + 'px'
          Vue.nextTick(() => {
            if (detector.clientHeight > 0) {
              wrapper.classList.add('dropdown-card-transition')
            }
            wrapper.style.maxHeight = '0'
          })
        } else if (detector.clientHeight > 0) {
          wrapper.classList.add('dropdown-card-transition')
          wrapper.style.maxHeight = detector.clientHeight + 'px'
        } else wrapper.style.maxHeight = 'none'
      }
    },
    endHideTransition() {
      this.block = false
      const wrapper = this.$refs.wrapper
      wrapper.removeEventListener('transitionend', this.endHideTransition)
      wrapper.classList.remove('dropdown-card-transition')
      if (!this.hide) wrapper.style.maxHeight = 'none'
    },
  },
}
</script>

<style>
.dropdown-card-transition {
  @apply transition-max-height duration-500;
}
</style>
