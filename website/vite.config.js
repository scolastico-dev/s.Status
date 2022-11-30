import Path from 'path'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'
import {defineConfig} from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), svgLoader()],
  server: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:42010',
        changeOrigin: true,
      },
    },
  },
  resolve: {
    alias: {
      '~': Path.resolve(__dirname, 'src'),
      '?': Path.resolve(__dirname, 'src/lib'),
      '@': Path.resolve(__dirname, 'src/components'),
    },
  },
})
