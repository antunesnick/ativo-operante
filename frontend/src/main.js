import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // <-- 1. Importe o router aqui

const app = createApp(App)

app.use(router) // <-- 2. Diga para o aplicativo usar as rotas
app.mount('#app')