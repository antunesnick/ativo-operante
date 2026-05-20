<template>
  <div class="login-wrapper">
    <div class="glow-bg"></div>
    <div class="login-card">
      <div class="logo-area">
        <div class="logo-icon">AO</div>
        <h2>Criar Conta</h2>
        <p>Junte-se ao Ativo e Operante</p>
      </div>

      <form @submit.prevent="fazerCadastro" v-if="!sucesso">
        <div class="form-group">
          <label for="cpf">CPF</label>
          <input 
            type="text" id="cpf" v-model="novoUsuario.cpf" 
            required placeholder="000.000.000-00" autocomplete="off"
          />
        </div>

        <div class="form-group">
          <label for="email">E-mail</label>
          <input 
            type="email" id="email" v-model="novoUsuario.email" 
            required placeholder="cidadao@cidade.br" autocomplete="off"
          />
        </div>

        <div class="form-group">
          <label for="senha">Senha</label>
          <input 
            type="password" id="senha" v-model="novoUsuario.senha" 
            required placeholder="Crie uma senha forte" 
          />
        </div>

        <button type="submit" class="btn-login" :disabled="carregando">
          <span v-if="!carregando">Cadastrar</span>
          <span v-else class="loader"></span>
        </button>

        <div v-if="erro" class="erro-toast">{{ erro }}</div>

        <div class="link-footer">
          <p>Já tem uma conta? <router-link to="/">Faça login</router-link></p>
        </div>
      </form>

      <!-- Mensagem de Sucesso -->
      <div v-else class="success-state">
        <div class="success-icon">✓</div>
        <h3>Conta Criada!</h3>
        <p>Seu cadastro foi realizado com sucesso.</p>
        <router-link to="/" class="btn-login" style="text-decoration: none; display: inline-block; margin-top: 1rem; text-align: center; box-sizing: border-box;">
          Ir para o Login
        </router-link>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from '../services/api';

const novoUsuario = ref({ cpf: '', email: '', senha: '' });
const erro = ref('');
const sucesso = ref(false);
const carregando = ref(false);

const fazerCadastro = async () => {
  erro.value = '';
  carregando.value = true;
  try {
    // Chama a rota de cadastro do Spring Boot
    await api.post('/auth/cadastrar', novoUsuario.value);
    sucesso.value = true;
  } catch (error) {
    erro.value = 'Erro ao cadastrar. Verifique se o CPF/E-mail já existem.';
  } finally {
    carregando.value = false;
  }
};
</script>

<style scoped>
/* Aproveitando a mesma base visual premium do LoginView */
.login-wrapper { display: flex; justify-content: center; align-items: center; min-height: 100vh; position: relative; overflow: hidden; }

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
.logo-area h2 { font-size: 1.4rem; font-weight: 600; color: var(--text-primary); letter-spacing: -0.5px; margin-bottom: 0.2rem; }
.logo-area p { color: var(--text-secondary); font-size: 0.9rem; }

.form-group { margin-bottom: 1.25rem; }
.form-group label { display: block; font-size: 0.85rem; font-weight: 500; color: var(--text-secondary); margin-bottom: 0.5rem; }

input {
  width: 100%; padding: 0.75rem 1rem;
  background: var(--bg-base); border: 1px solid var(--border-color);
  border-radius: 8px; color: var(--text-primary); font-size: 0.95rem; transition: 0.2s; box-sizing: border-box;
}
input:focus { outline: none; border-color: var(--accent); box-shadow: 0 0 0 3px var(--ring); }
input::placeholder { color: #52525b; }

.btn-login {
  width: 100%; padding: 0.8rem; margin-top: 1rem;
  background: var(--text-primary); color: var(--bg-base);
  border: none; border-radius: 8px; font-weight: 600; font-size: 0.95rem;
  cursor: pointer; transition: 0.2s;
}
.btn-login:hover:not(:disabled) { background: #d4d4d8; transform: translateY(-1px); }
.btn-login:disabled { opacity: 0.7; cursor: not-allowed; }

.erro-toast {
  margin-top: 1rem; padding: 0.75rem; border-radius: 6px;
  background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.2);
  color: #f87171; font-size: 0.85rem; text-align: center; font-weight: 500;
}

.link-footer { text-align: center; margin-top: 1.5rem; font-size: 0.85rem; color: var(--text-secondary); }
.link-footer a { color: var(--accent); text-decoration: none; font-weight: 500; transition: 0.2s; }
.link-footer a:hover { color: var(--accent-hover); text-decoration: underline; }

.success-state { text-align: center; padding: 1rem 0; }
.success-icon {
  width: 60px; height: 60px; background: rgba(16, 185, 129, 0.1); color: var(--accent);
  border-radius: 50%; display: flex; align-items: center; justify-content: center;
  font-size: 2rem; margin: 0 auto 1.5rem; border: 2px solid var(--accent);
}
.success-state h3 { color: var(--text-primary); margin-bottom: 0.5rem; }
.success-state p { color: var(--text-secondary); font-size: 0.9rem; }

.loader {
  display: inline-block; width: 1.2rem; height: 1.2rem;
  border: 2px solid var(--bg-base); border-bottom-color: transparent;
  border-radius: 50%; animation: rotation 1s linear infinite;
}
@keyframes rotation { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
</style>