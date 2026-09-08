import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: [
      { find: '@', replacement: path.resolve(__dirname, 'src') }
    ],
  },
  server:{
    host: '0.0.0.0',
    proxy: {
      '/shop-goods': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/shop-user': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/shop-order': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/shop-file': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/auth': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/file': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/goods': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/user': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/order': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
    }
  }

})

