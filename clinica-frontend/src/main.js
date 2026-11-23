import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import ptBr from 'element-plus/es/locale/lang/pt-br'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'
import './assets/styles/main.css'

createApp(App).use(ElementPlus, { locale: ptBr }).use(router).mount('#app')
