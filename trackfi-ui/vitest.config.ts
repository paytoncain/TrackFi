import { defineConfig } from 'vitest/config'
import defineBaseConfig from './vite.config'

export default defineConfig({
  ...defineBaseConfig,
  test: {
    globals: true,
    environment: 'jsdom',
    css: true
  }
});