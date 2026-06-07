// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import basicSsl from '@vitejs/plugin-basic-ssl'

export default defineConfig({
  plugins: [react(), basicSsl()],
  server: {
    https: true,
    host: '0.0.0.0', // L3: 모든 네트워크 인터페이스 수용하되 정책으로 제어
    port: 5176,
    headers: {
      // 🚀 L7: OWASP A03(Injection) 방어를 위한 강력한 CSP 정책
      "Content-Security-Policy": "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.tailwindcss.com; style-src 'self' 'unsafe-inline' https://cdn.tailwindcss.com https://fonts.googleapis.com; img-src 'self' data: https://images.unsplash.com https://*.google.com https://*.gstatic.com; connect-src 'self' https://* http://localhost:5000 http://127.0.0.1:5000; font-src 'self' https://fonts.gstatic.com;",

      // 🚀 L4: OWASP A02(Cryptographic Failures) 방어를 위한 HSTS
      "Strict-Transport-Security": "max-age=31536000; includeSubDomains",

      // 🚀 L7: 클릭재킹 및 MIME Sniffing 방어 정책
      "X-Frame-Options": "DENY",
      "X-Content-Type-Options": "nosniff"
    }
  }
})