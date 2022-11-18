<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <component :is="element" v-html="processedText" />
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
    values: {
      type: Object,
      default: () => ({}),
    },
  },
  data: () => ({
    processedText: '',
  }),
  beforeMount() {
    this.processText(this.text, this.values)
  },
  watch: {
    text(newVal) {
      this.processText(newVal, this.values)
    },
    values(newVal) {
      this.processText(this.text, newVal)
    },
  },
  methods: {
    processText(text, values) {
      this.processedText = this.$lang.data[text]
          .replace(/\{\{(\w+?)}}/g, (match, key) => values[key])
          .replace(/\[\[([\s\S]+?)\|(\S+?)]]/g, (match, name, url) => `<a href="${url}" target="_blank">${name}</a>`)
    },
  },
}
</script>
