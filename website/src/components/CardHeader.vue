<template>
  <div class="card-header" @click="$emit('click')">
    <h2
        class="flex-grow transition-colors duration-500"
        :class="{'card-loading': blink}"
    >
      {{ service.displayName }}
    </h2>
    <div class="relative">
      <svg
          class="status-dot absolute"
          :class="{
              'online': service.status === 'ONLINE',
              'offline': service.status === 'OFFLINE',
              'maintenance': service.status === 'MAINTENANCE',
            }"
      >
        <!-- dot svg -->
        <circle
            cx="50%"
            cy="50%"
            r="50%"
            fill="currentColor"
        />
      </svg>
      <svg
          class="status-dot"
          :class="{
              'online': service.status === 'ONLINE',
              'offline': service.status === 'OFFLINE',
              'maintenance': service.status === 'MAINTENANCE',
            }"
      >
        <!-- dot svg -->
        <circle
            cx="50%"
            cy="50%"
            r="50%"
            fill="currentColor"
        />
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    service: {
      type: Object,
      required: true,
    },
    blink: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['click'],
}
</script>

<style>
.card-header {
  @apply flex items-center text-xl p-4;
  @apply select-none cursor-pointer;
}
.card-loading {
  @apply animate-pulse text-blue-900;
}
.status-dot {
  @apply h-8 w-8;
}
.status-dot.online {
  @apply text-green-500;
}
.status-dot.offline {
  @apply text-red-500;
}
.status-dot.maintenance {
  @apply text-yellow-500;
}
.status-dot.offline.absolute, .status-dot.maintenance.absolute {
  @apply animate-ping;
}
.status-dot.online.absolute {
  @apply hidden;
}
.status-dot.online {
  @apply animate-pulse;
}
</style>
