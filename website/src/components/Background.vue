<template>
  <div class="background" :style="{paddingRight: padding + 'px'}">
    <div class="container">
      <slot />
    </div>
  </div>
</template>

<style>
.background {
  @apply bg-gradient-to-br from-zinc-600 to-zinc-800;
  @apply w-full min-h-screen grid grid-cols-8;
  @apply overflow-y-auto;
}
.container {
  @apply min-h-screen col-start-3 col-span-4;
  @apply flex flex-col justify-center;
}
html {
  scrollbar-gutter: stable;
}
</style>
