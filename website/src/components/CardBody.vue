<template>
  <div class="card-body">
    <canvas ref="newest" />
    <canvas ref="uptime" />
    <canvas ref="duration" />
  </div>
</template>

<script>
import Chart from 'chart.js/auto'
export default {
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  data: () => ({
    charts: {},
  }),
  emits: ['ready'],
  mounted() {
    this.update(this.data.data)
    this.$emit('ready')
  },
  watch: {
    data(newVal) {
      this.update(newVal.data)
    },
  },
  methods: {
    async update(data) {
      this.generate('newest', 'Latest Response Time (ms)', 'duration', data.newest, true)
      this.generate('uptime', '24H Average Uptime', 'uptime', data.averages, false, 0, 100)
      this.generate('duration', '24H Average Response Time (ms)', 'duration', data.averages, false)
    },
    generate(name, label, watch, data, time, min, max) {
      const generateDate = (unix) => {
        const date = new Date(Number(unix)*1000)
        if (time) return date.toLocaleTimeString()
        else return date.toLocaleDateString()
      }
      const payload = {
        labels: data.map((x) => generateDate(x.at)),
        datasets: [{
          data: data.map((x) => x[watch]),
          backgroundColor: '#272727',
          label: label,
        }],
      }
      if (this.charts[name]) {
        this.charts[name].update(payload)
      } else {
        this.charts[name] = new Chart(this.$refs[name], {
          type: 'bar',
          data: payload,
          options: min && max ? {
            scales: {
              y: {
                min: min,
                max: max,
              },
            },
          } : undefined,
        })
      }
    },
  },
}
</script>

<style>
.card-body {
  @apply border-t-2 border-gray-200;
  @apply mx-4 mb-4 mt-1 pt-4;
}
</style>
