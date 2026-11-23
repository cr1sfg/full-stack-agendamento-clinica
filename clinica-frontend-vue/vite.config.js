import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5173,
  },
  build: {
    // Element Plus importado por completo fica num chunk separado (cacheável)
    chunkSizeWarningLimit: 1100,
    rollupOptions: {
      output: {
        manualChunks: { 'element-plus': ['element-plus', '@element-plus/icons-vue'] },
      },
    },
  },
})
