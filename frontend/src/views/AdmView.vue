<template>
  <div class="layout">
    <nav class="topbar">
      <div class="brand">AO Admin</div>
      <button @click="sair" class="btn-ghost">Encerrar Sessão</button>
    </nav>

    <div class="container">
      <div class="grid-layout">
        <aside class="sidebar">
          
          <div class="panel">
            <div class="panel-header">
              <h3>Órgãos Competentes</h3>
            </div>
            <form @submit.prevent="salvarOrgao" class="inline-form">
              <input type="text" v-model="formOrgao.nome" placeholder="Nome do Órgão" required />
              <button type="submit" class="btn-icon" :class="{ 'btn-edit-mode': editandoOrgao }">
                {{ editandoOrgao ? '✓' : '+' }}
              </button>
              <button v-if="editandoOrgao" @click="cancelarEdicaoOrgao" type="button" class="btn-icon btn-del">×</button>
            </form>
            <ul class="data-list">
              <li v-for="org in orgaos" :key="org.id">
                <span>{{ org.nome }}</span>
                <div class="actions">
                  <button @click="prepararEdicaoOrgao(org)" class="btn-action edit">✎</button>
                  <button @click="apagarOrgao(org.id)" class="btn-action del">×</button>
                </div>
              </li>
            </ul>
          </div>

          <div class="panel mt-4">
            <div class="panel-header">
              <h3>Tipos de Problema</h3>
            </div>
            <form @submit.prevent="salvarTipo" class="inline-form">
              <input type="text" v-model="formTipo.nome" placeholder="Novo Tipo" required />
              <button type="submit" class="btn-icon" :class="{ 'btn-edit-mode': editandoTipo }">
                {{ editandoTipo ? '✓' : '+' }}
              </button>
              <button v-if="editandoTipo" @click="cancelarEdicaoTipo" type="button" class="btn-icon btn-del">×</button>
            </form>
            <ul class="data-list">
              <li v-for="tipo in tipos" :key="tipo.id">
                <span>{{ tipo.nome }}</span>
                <div class="actions">
                  <button @click="prepararEdicaoTipo(tipo)" class="btn-action edit">✎</button>
                  <button @click="apagarTipo(tipo.id)" class="btn-action del">×</button>
                </div>
              </li>
            </ul>
          </div>
        </aside>

        <main class="main-content">
          <div class="panel">
            <div class="panel-header border-b">
              <h3>Gestão de Denúncias</h3>
              <span class="badge-count">{{ denuncias.length }} Registros</span>
            </div>
            
            <div v-if="denuncias.length === 0" class="empty-state">
              Nenhuma denúncia pendente no momento.
            </div>

            <div class="ticket-list" v-else>
              <div v-for="den in denuncias" :key="den.id" class="ticket">
                <div class="ticket-head">
                  <div class="title-group">
                    <h4>{{ den.titulo }}</h4>
                    <span class="urgency" :class="'u-'+den.urgencia">Nível {{ den.urgencia }}</span>
                  </div>
                  <button @click="apagarDenuncia(den.id)" class="btn-del-text">Excluir</button>
                </div>
                
                <p class="desc">{{ den.desc }}</p>
                
                <img v-if="den.foto" :src="'http://localhost:8080/uploads/' + den.foto" class="foto-denuncia" alt="Foto da denúncia" />
                
                <div class="ticket-meta">
                  <span><strong>Autor:</strong> {{ den.usuario?.email }}</span>
                  <span class="dot">•</span>
                  <span><strong>Destino:</strong> {{ den.orgao?.nome }}</span>
                  <span class="dot">•</span>
                  <span>{{ den.tipo?.nome }}</span>
                  <span class="dot">•</span>
                  <span><strong>Data:</strong> {{ den.data }}</span>
                </div>

                <div class="feedback-action" v-if="!den.feedback">
                  <input type="text" v-model="feedbacks[den.id]" placeholder="Adicionar feedback oficial..." />
                  <button @click="darFeedback(den.id)" class="btn-primary-sm">Enviar</button>
                </div>
                <div v-else class="feedback-dado">
                  <strong>Feedback enviado:</strong> {{ den.feedback.texto }}
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../services/api';

const router = useRouter();
const orgaos = ref([]); const tipos = ref([]); const denuncias = ref([]);
const feedbacks = ref({});

const formOrgao = ref({ id: null, nome: '' });
const editandoOrgao = ref(false);

const formTipo = ref({ id: null, nome: '' });
const editandoTipo = ref(false);

onMounted(async () => { await carregarDados(); });

const carregarDados = async () => {
  try {
    const [resOrgaos, resTipos, resDenuncias] = await Promise.all([
      api.get('/adm/orgaos'), api.get('/adm/tipos'), api.get('/adm/denuncias')
    ]);
    orgaos.value = resOrgaos.data; tipos.value = resTipos.data; denuncias.value = resDenuncias.data;
  } catch (error) { console.error(error); }
};

const salvarOrgao = async () => { 
  if (editandoOrgao.value) {
    await api.put(`/adm/orgaos/${formOrgao.value.id}`, { nome: formOrgao.value.nome });
  } else {
    await api.post('/adm/orgaos', { nome: formOrgao.value.nome }); 
  }
  cancelarEdicaoOrgao();
  await carregarDados(); 
};
const prepararEdicaoOrgao = (org) => { formOrgao.value = { ...org }; editandoOrgao.value = true; };
const cancelarEdicaoOrgao = () => { formOrgao.value = { id: null, nome: '' }; editandoOrgao.value = false; };
const apagarOrgao = async (id) => { if(confirm('Excluir órgão?')) { await api.delete(`/adm/orgaos/${id}`); await carregarDados(); } };

const salvarTipo = async () => { 
  if (editandoTipo.value) {
    await api.put(`/adm/tipos/${formTipo.value.id}`, { nome: formTipo.value.nome });
  } else {
    await api.post('/adm/tipos', { nome: formTipo.value.nome }); 
  }
  cancelarEdicaoTipo();
  await carregarDados(); 
};
const prepararEdicaoTipo = (tipo) => { formTipo.value = { ...tipo }; editandoTipo.value = true; };
const cancelarEdicaoTipo = () => { formTipo.value = { id: null, nome: '' }; editandoTipo.value = false; };
const apagarTipo = async (id) => { if(confirm('Excluir tipo?')) { await api.delete(`/adm/tipos/${id}`); await carregarDados(); } };

const apagarDenuncia = async (id) => { if(confirm('Excluir denúncia?')) { await api.delete(`/adm/denuncias/${id}`); await carregarDados(); } };
const darFeedback = async (id) => {
  const texto = feedbacks.value[id];
  if (!texto) return;
  try { await api.post(`/adm/denuncias/${id}/feedback`, { texto }); alert("Feedback enviado!"); feedbacks.value[id] = ''; await carregarDados(); } 
  catch(e) { alert("Erro ao enviar"); }
};

const sair = () => { localStorage.clear(); router.push('/'); };
</script>

<style scoped>
.layout { min-height: 100vh; background: var(--bg-base); }
.topbar { position: sticky; top: 0; z-index: 10; display: flex; justify-content: space-between; align-items: center; padding: 1rem 2rem; background: rgba(10, 10, 10, 0.8); backdrop-filter: blur(12px); border-bottom: 1px solid var(--border-color); }
.brand { font-weight: 700; font-size: 1.2rem; color: var(--text-primary); letter-spacing: -0.5px; }

.btn-ghost { background: transparent; border: 1px solid var(--border-color); color: var(--text-secondary); padding: 0.5rem 1rem; border-radius: 6px; font-size: 0.85rem; cursor: pointer; transition: 0.2s; }
.btn-ghost:hover { color: var(--text-primary); border-color: #52525b; background: var(--bg-elevated); }

.container { max-width: 1200px; margin: 0 auto; padding: 2rem; }
.grid-layout { display: grid; grid-template-columns: 320px 1fr; gap: 2rem; align-items: start; }
@media (max-width: 768px) { .grid-layout { grid-template-columns: 1fr; } }

.panel { background: var(--bg-surface); border: 1px solid var(--border-color); border-radius: 12px; padding: 1.5rem; }
.panel-header { margin-bottom: 1.2rem; display: flex; justify-content: space-between; align-items: center; }
.panel-header h3 { font-size: 1.1rem; font-weight: 600; color: var(--text-primary); }
.border-b { border-bottom: 1px solid var(--border-color); padding-bottom: 1rem; margin-bottom: 1.5rem; }

.badge-count { background: var(--bg-elevated); padding: 0.2rem 0.6rem; border-radius: 20px; font-size: 0.75rem; color: var(--text-secondary); }

input, select, textarea { width: 100%; padding: 0.6rem 1rem; background: var(--bg-base); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-primary); font-size: 0.9rem; transition: 0.2s; }
input:focus, select:focus, textarea:focus { outline: none; border-color: var(--accent); box-shadow: 0 0 0 2px var(--ring); }

.inline-form { display: flex; gap: 0.5rem; margin-bottom: 1.5rem; }
.btn-icon { background: var(--text-primary); color: var(--bg-base); border: none; border-radius: 8px; width: 36px; height: 36px; font-size: 1.2rem; font-weight: bold; cursor: pointer; transition: 0.2s; }
.btn-icon:hover { opacity: 0.9; }
.btn-edit-mode { background: var(--accent); }

.data-list { list-style: none; display: flex; flex-direction: column; gap: 0.5rem; }
.data-list li { display: flex; justify-content: space-between; align-items: center; padding: 0.75rem 1rem; background: var(--bg-base); border: 1px solid var(--border-color); border-radius: 8px; font-size: 0.9rem; }
.actions { display: flex; gap: 0.5rem; }
.btn-action { background: transparent; border: none; cursor: pointer; font-size: 1rem; transition: 0.2s; }
.edit { color: var(--text-secondary); } .edit:hover { color: var(--accent); }
.del { color: var(--text-secondary); } .del:hover { color: var(--danger); }

.ticket-list { display: flex; flex-direction: column; gap: 1rem; }
.ticket { background: var(--bg-base); border: 1px solid var(--border-color); border-radius: 10px; padding: 1.5rem; transition: 0.2s; }
.ticket:hover { border-color: #52525b; }

.ticket-head { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.75rem; }
.title-group { display: flex; align-items: center; gap: 1rem; }
.ticket-head h4 { font-size: 1.1rem; font-weight: 500; }
.urgency { font-size: 0.7rem; padding: 0.2rem 0.5rem; border-radius: 4px; font-weight: 600; text-transform: uppercase; }
.u-1 { background: rgba(16, 185, 129, 0.1); color: var(--accent); border: 1px solid rgba(16, 185, 129, 0.2); }
.u-2, .u-3 { background: rgba(234, 179, 8, 0.1); color: #eab308; border: 1px solid rgba(234, 179, 8, 0.2); }
.u-4, .u-5 { background: rgba(239, 68, 68, 0.1); color: var(--danger); border: 1px solid rgba(239, 68, 68, 0.2); }

.btn-del-text { background: transparent; border: none; color: var(--danger); font-size: 0.8rem; cursor: pointer; opacity: 0; transition: 0.2s; }
.ticket:hover .btn-del-text { opacity: 1; }

.desc { color: var(--text-secondary); font-size: 0.95rem; margin-bottom: 1rem; line-height: 1.5; }

/* CORRIGIDO: Aplicado object-fit para manter a proporção correta sem achatar */
.foto-denuncia { 
  width: 100%; 
  max-width: 400px; 
  height: 220px; 
  object-fit: cover; 
  object-position: center;
  border-radius: 6px; 
  border: 1px solid var(--border-color); 
  margin-bottom: 1rem; 
}

.ticket-meta { display: flex; align-items: center; gap: 0.5rem; font-size: 0.8rem; color: #71717a; margin-bottom: 1.5rem; flex-wrap: wrap; }
.ticket-meta strong { color: var(--text-secondary); font-weight: 500; }
.dot { font-size: 1.2rem; line-height: 0; }

.feedback-action { display: flex; gap: 0.5rem; }
.btn-primary-sm { background: var(--text-primary); color: var(--bg-base); border: none; border-radius: 6px; padding: 0 1rem; font-weight: 600; cursor: pointer; }
.feedback-dado { background: rgba(16, 185, 129, 0.1); color: var(--accent); padding: 0.6rem 1rem; border-radius: 6px; font-size: 0.85rem; }

.empty-state { text-align: center; padding: 4rem 2rem; color: var(--text-secondary); border: 1px dashed var(--border-color); border-radius: 8px; font-size: 0.9rem; }
.mt-4 { margin-top: 1.5rem; }
</style>