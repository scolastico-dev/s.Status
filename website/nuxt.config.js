export default {
  loading: '~/components/LoadingBar.vue',

  ssr: false,

  target: 'static',

  head: {
    title: 's.Status',
    htmlAttrs: {
      lang: 'en',
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  css: [
    '~/assets/global.css'
  ],

  plugins: [
    '~/plugins/TimeHelper.js',
    '~/plugins/VShowSlide.js',
  ],

  components: [
    '~/components'
  ],

  buildModules: [
    '@nuxtjs/eslint-module',
    '@nuxtjs/tailwindcss',
  ],

  eslint: {
    cache: false,
  },

  modules: [
    '@nuxtjs/axios',
  ],

  axios: {
    baseURL: process.env.BASE_API_URL,
    browserBaseURL: process.env.BASE_API_URL,
  },

  build: {},
}
