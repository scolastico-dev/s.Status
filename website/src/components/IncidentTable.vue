<template>
  <div class="incident-table">
    <translated element="h3" :text="incidents ? 'incidents' : 'maintenance'" />
    <div>
      <translated text="from" />
      <translated text="to" />
      <translated :text="incidents ? 'messages' : 'message'" />
    </div>
    <div v-for="(item, index) of data" :key="index">
      <p>
        {{ formatTime(item.from) }}
      </p>
      <p>
        {{ formatTime(item.until) }}
      </p>
      <p class="whitespace-pre">{{ incidents ? item.messages.join('\n') : item.message }}</p>
    </div>
  </div>
</template>

<script>
import Translated from '@/Translated.vue'
export default {
  components: {Translated},
  props: {
    incidents: {
      type: Boolean,
      default: true,
    },
    data: {
      type: Object,
      required: true,
    },
  },
  methods: {
    formatTime(time) {
      if (time === null) return ''
      const date = new Date(Number(time)*1000)
      return date.toLocaleDateString() + ' - ' + date.toLocaleTimeString()
    },
  },
}
</script>

<style>
.incident-table {
  @apply mt-12;
}
.incident-table>div:first-of-type {
  @apply uppercase font-bold;
}
.incident-table>div:nth-of-type(even) {
  @apply bg-gray-200;
}
.incident-table>div {
  @apply grid grid-cols-3 mt-2;
}
.incident-table>div>p:last-child {
  @apply text-right;
}
</style>
