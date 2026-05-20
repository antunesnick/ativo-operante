import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/cadastro',
      name: 'cadastro',
      component: () => import('../views/CadastroView.vue')
    },
    {
      path: '/cidadao',
      name: 'cidadao',
      // Lazy loading: só carrega o arquivo quando acessar a rota
      component: () => import('../views/CidadaoView.vue'),
      meta: { requiresAuth: true, nivelRequerido: 2 } // 2 = Cidadão
    },
    {
      path: '/adm',
      name: 'adm',
      component: () => import('../views/AdmView.vue'),
      meta: { requiresAuth: true, nivelRequerido: 1 } // 1 = Admin
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const nivel = parseInt(localStorage.getItem('nivel'));

  if (to.meta.requiresAuth) {
    if (!token) {
      next('/');
    } else if (to.meta.nivelRequerido !== undefined && to.meta.nivelRequerido !== nivel) {
      if (nivel === 1) next('/adm');
      else next('/cidadao');
    } else {
      next(); 
    }
  } else {
    next(); 
  }
});

export default router;