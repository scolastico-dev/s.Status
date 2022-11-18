<template>
  <div class="background">
    <div class="position">
      <div class="container">
        <slot />
      </div>
    </div>
  </div>
</template>

<style>
.background {
  @apply bg-gradient-to-br from-zinc-600 to-zinc-800;
  @apply w-full h-screen overflow-y-auto;
  scrollbar-gutter: stable;
}
.position {
  @apply w-full min-h-screen grid grid-cols-8;
}
.container {
  @apply min-h-screen col-start-3 col-span-4;
  @apply flex flex-col justify-center;
}
</style>
