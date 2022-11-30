<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <component :is="element" v-html="processedText" :title="processedTooltip" />
</template>

<script>
export default {
  props: {
    element: {
      type: String,
      default: 'p',
    },
    text: {
      type: String,
      required: true,
    },
    textTooltip: {
      type: String,
      default: null,
    },
    values: {
      type: Object,
      default: () => ({}),
    },
  },
  data: () => ({
    processedText: '',
    processedTooltip: '',
  }),
  beforeMount() {
    this.processAllText(this.text, this.textTooltip, this.values)
  },
  watch: {
    text(newVal) {
      this.processAllText(newVal, this.textTooltip, this.values)
    },
    textTooltip(newVal) {
      this.processAllText(this.text, newVal, this.values)
    },
    values(newVal) {
      this.processAllText(this.text, this.textTooltip, newVal)
    },
  },
  methods: {
    processAllText(text, tooltip, values) {
      this.processedText = this.processText(text, values)
      this.processedTooltip = tooltip ? this.processText(tooltip, values) : null
    },
    processText(text, values) {
      return (this.$lang.data[text] || this.$lang.default[text] || `{{ ${text} }}`)
          .replace(/\{\{(\w+?)}}/g, (match, key) => values[key])
          .replace(/\[\[([\s\S]+?)\|(\S+?)]]/g, (match, name, url) => `<a href="${url}" target="_blank">${name}</a>`)
    },
  },
}
</script>
