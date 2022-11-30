<template>
  <div class="card-body">
    <info-card v-for="(card, index) in cards" :key="index" v-bind="card"/>
    <translated element="h3" text="latestResponse" />
    <canvas ref="newest" />
    <translated element="h3" text="uptime" />
    <canvas ref="uptime" />
    <translated element="h3" text="averageResponse" />
    <canvas ref="duration" />
    <incident-table :incidents="false" :data="maintenances" />
    <incident-table :incidents="true" :data="downtimes" />
  </div>
</template>

<script>
import Translated from '@/Translated.vue'
import Chart from 'chart.js/auto'
import {shallowRef} from 'vue'
import IncidentTable from '@/IncidentTable.vue'
import InfoCard from '@/InfoCard.vue'
export default {
  components: {InfoCard, IncidentTable, Translated},
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  data: () => ({
    charts: {},
    cards: [],
    maintenances: [],
    downtimes: [],
  }),
  emits: ['ready'],
  mounted() {
    if (this.data) this.update(this.data)
    this.$emit('ready')
  },
  watch: {
    data(newVal) {
      this.update(newVal)
    },
  },
  methods: {
    async update(data) {
      const cards = []
      const {newest, averages, maintenances, downtimes} = data.data

      this.maintenances = maintenances.slice().sort((a, b) => a.from - b.from).reverse()
      this.downtimes = downtimes.slice().sort((a, b) => a.from - b.from).reverse()

      this.generate('newest', 'latestResponse', 'duration', newest, data.chartColorsResponse, true)
      this.generate('uptime', 'uptime', 'uptime', averages, data.chartColorsUptime, false, 0, 100)
      this.generate('duration', 'averageResponse', 'duration', averages, data.chartColorsResponse, false)

      const openDowntimes = downtimes.filter((d) => d.until === null || Number(d.until) > Date.now() / 1000)
      const downtime = openDowntimes.sort((a, b) => Number(a.from) - Number(b.from))[0]
      if (downtime) cards.push({error: true, text: 'currentIncident', notice: downtime.message})

      const currentMaintenances = maintenances.filter((m) =>
        Number(m.from) < Date.now() / 1000 && (m.until === null || Number(m.until) > Date.now() / 1000),
      )

      const currentMaintenance = currentMaintenances.sort((a, b) => Number(a.from) - Number(b.from))[0]
      if (currentMaintenance) cards.push({error: false, text: 'currentMaintenance', notice: currentMaintenance.message})

      const nextMaintenances = maintenances.filter((m) => Number(m.from) > Date.now() / 1000)
      const maintenance = nextMaintenances.sort((a, b) => Number(a.from) - Number(b.from))[0]
      if (maintenance) cards.push({error: false, text: 'plannedMaintenance', notice: maintenance.message})
      if (JSON.stringify(cards) !== JSON.stringify(this.cards)) this.cards = cards
    },
    generate(name, label, watch, data, colors, time, min, max) {
      data = data.sort((a, b) => Number(a.at - Number(b.at)))
      const generateDate = (unix) => {
        const date = new Date(Number(unix)*1000)
        if (time) return date.toLocaleTimeString()
        else return date.toLocaleDateString()
      }

      const payload = {
        labels: data.map((x) => generateDate(x.at)),
        datasets: [{
          data: data.map((x) => x[watch]),
          backgroundColor: data.map((x) => {
            const val = x[watch]
            return Object.keys(colors).reduce((acc, curr) => {
              if (val >= curr) return colors[curr]
              else return acc
            }, colors[0])
          }),
          label: this.$lang.data[label],
        }],
      }

      if (this.charts[name]) {
        this.charts[name].data = payload
        this.charts[name].update('none')
      } else {
        this.charts[name] = shallowRef(new Chart(this.$refs[name], {
          type: 'bar',
          data: payload,
          options: {
            plugins: {
              legend: {
                display: false,
              },
            },
            scales: min && max ? {
              y: {
                min: min,
                max: max,
              },
            } : undefined,
          },
        }))
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
.card-body h3 {
  @apply text-center text-xl mt-4;
}
</style>
