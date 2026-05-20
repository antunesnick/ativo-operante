<template>
  <div class="login-wrapper">
    <div class="glow-bg"></div>
    <div class="login-card">
      <div class="logo-area">
        <div class="logo-icon">AO</div>
        <h2>Ativo e Operante</h2>
        <p>Portal do Cidadão</p>
      </div>

      <form @submit.prevent="fazerLogin">
        <div class="form-group">
          <label for="email">E-mail</label>
          <input 
            type="email" id="email" v-model="credenciais.email" 
            required placeholder="admin@pm.br" autocomplete="off"
          />
        </div>

        <div class="form-group">
          <label for="senha">Senha</label>
          <input 
            type="password" id="senha" v-model="credenciais.senha" 
            required placeholder="••••••" 
          />
        </div>

        <button type="submit" class="btn-login" :disabled="carregando">
          <span v-if="!carregando">Acessar Sistema</span>
          <span v-else class="loader"></span>
        </button>

        <div v-if="erro" class="erro-toast">{{ erro }}</div>
        <div class="link-footer">
          <p>Não possui uma conta? <router-link to="/cadastro">Cadastre-se</router-link></p>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '../services/api';

const router = useRouter();
const credenciais = ref({ email: '', senha: '' });
const erro = ref('');
const carregando = ref(false);

const fazerLogin = async () => {
  erro.value = '';
  carregando.value = true;
  try {
    const resposta = await api.post('/auth/login', credenciais.value);
    const { token, nivel } = resposta.data;
    localStorage.setItem('token', token);
    localStorage.setItem('nivel', nivel);
    router.push(nivel === 1 ? '/adm' : '/cidadao');
  } catch (error) {
    erro.value = error.response?.status === 401 ? 'Credenciais inválidas.' : 'Servidor indisponível.';
  } finally {
    carregando.value = false;
  }
};
</script>

<style scoped>
.link-footer { text-align: center; margin-top: 1.5rem; font-size: 0.85rem; color: var(--text-secondary); }
.link-footer a { color: var(--accent); text-decoration: none; font-weight: 500; transition: 0.2s; }
.link-footer a:hover { color: var(--accent-hover); text-decoration: underline; }
.login-wrapper {
  display: flex; justify-content: center; align-items: center; min-height: 100vh;
  position: relative; overflow: hidden;
}

/* Efeito de luz sutil no fundo (Estilo Vercel) */
.glow-bg {
  position: absolute; width: 600px; height: 600px;
  background: radial-gradient(circle, rgba(16,185,129,0.1) 0%, rgba(10,10,10,0) 70%);
  top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 0; pointer-events: none;
}

.login-card {
  position: relative; z-index: 1; width: 100%; max-width: 380px;
  background: var(--bg-surface); padding: 2.5rem 2rem;
  border-radius: 16px; border: 1px solid var(--border-color);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}

.logo-area { text-align: center; margin-bottom: 2rem; }
.logo-icon {
  width: 48px; height: 48px; margin: 0 auto 1rem;
  background: linear-gradient(135deg, var(--accent), #047857);
  color: #fff; font-weight: 700; font-size: 1.2rem;
  display: flex; align-items: center; justify-content: center;
  border-radius: 12px; box-shadow: 0 4px 12px var(--ring);
}
.logo-area h2 { font-size: 1.4rem; font-weight: 600; color: var(--text-primary); letter-spacing: -0.5px; }
.logo-area p { color: var(--text-secondary); font-size: 0.9rem; margin-top: 0.2rem; }

.form-group { margin-bottom: 1.25rem; }
.form-group label { display: block; font-size: 0.85rem; font-weight: 500; color: var(--text-secondary); margin-bottom: 0.5rem; }

input {
  width: 100%; padding: 0.75rem 1rem;
  background: var(--bg-base); border: 1px solid var(--border-color);
  border-radius: 8px; color: var(--text-primary); font-size: 0.95rem;
  transition: all 0.2s ease;
}
input:focus { outline: none; border-color: var(--accent); box-shadow: 0 0 0 3px var(--ring); }
input::placeholder { color: #52525b; }

.btn-login {
  width: 100%; padding: 0.8rem; margin-top: 1rem;
  background: var(--text-primary); color: var(--bg-base);
  border: none; border-radius: 8px; font-weight: 600; font-size: 0.95rem;
  cursor: pointer; transition: all 0.2s;
}
.btn-login:hover:not(:disabled) { background: #d4d4d8; transform: translateY(-1px); }
.btn-login:disabled { opacity: 0.7; cursor: not-allowed; }

.erro-toast {
  margin-top: 1rem; padding: 0.75rem; border-radius: 6px;
  background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.2);
  color: #f87171; font-size: 0.85rem; text-align: center; font-weight: 500;
}

.loader {
  display: inline-block; width: 1.2rem; height: 1.2rem;
  border: 2px solid var(--bg-base); border-bottom-color: transparent;
  border-radius: 50%; animation: rotation 1s linear infinite;
}
@keyframes rotation { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
</style>