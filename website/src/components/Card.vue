<template>
  <div class="card">
    <card-header :service="service" :blink="expanded && !ready" @click="onClick" />
    <drop-down :open="expanded && ready">
      <card-body v-if="data" :data="data" @ready="readyUp" />
    </drop-down>
  </div>
</template>

<script>
import CardHeader from '@/CardHeader.vue'
import DropDown from '@/DropDown.vue'
import CardBody from '@/CardBody.vue'
import md5 from 'md5'
export default {
  components: {CardBody, DropDown, CardHeader},
  props: {
    service: {
      type: Object,
      required: true,
    },
  },
  data: () => ({
    expanded: false,
    ready: false,
    data: null,
    count: 0,
    md5: null,
  }),
  beforeUnmount() {
    if (this.data) this.$bus.off('refresh', this.update)
  },
  methods: {
    async update() {
      if (!this.expanded) return
      const needRegister = !this.data
      const tz = (new Date().getTimezoneOffset() / 60)*-1
      if (this.md5) {
        const hash = await this.$axios(`hash/${this.service.name}/${tz}`)
        if (hash.data.hash === this.md5) return
      }
      this.data = (await this.$axios(`get/${this.service.name}/${tz}`)).data
      this.md5 = md5(JSON.stringify(this.data.data))
      if (needRegister) this.$bus.on('refresh', this.update)
    },
    onClick() {
      if (this.expanded && !this.data) return
      this.expanded = !this.expanded
      if (!this.data) this.update()
    },
    readyUp() {
      if (this.ready) return
      this.count++
      if (this.count === 2) this.ready = true
    },
  },
}
</script>

<style>
.card {
  @apply bg-white rounded-xl my-4;
}
</style>
