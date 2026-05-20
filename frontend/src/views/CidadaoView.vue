<template>
  <div class="layout">
    <nav class="topbar">
      <div class="brand">AO Cidadão</div>
      <button @click="sair" class="btn-ghost">Encerrar Sessão</button>
    </nav>

    <div class="container">
      
      <section class="panel mb-8">
        <div class="panel-header border-b">
          <h3>Nova Denúncia</h3>
          <p class="subtitle">Descreva o problema para notificarmos as autoridades.</p>
        </div>

        <form @submit.prevent="enviarDenuncia">
          <div class="form-grid">
            <div class="form-group span-full">
              <label>Título Breve</label>
              <input type="text" v-model="novaDenuncia.titulo" placeholder="Ex: Semáforo quebrado na Av. Brasil" required />
            </div>

            <div class="form-group span-full">
              <label>Descrição Completa</label>
              <textarea v-model="novaDenuncia.desc" rows="3" placeholder="Detalhe o máximo possível..." required></textarea>
            </div>

            <div class="form-group span-full">
              <label>Foto (Opcional)</label>
              <input type="file" @change="processarFoto" accept="image/*" class="file-input" />
              <div v-if="novaDenuncia.fotoUrl" class="image-preview">
                <img :src="novaDenuncia.fotoUrl" alt="Preview" />
                <button type="button" @click="novaDenuncia.fotoUrl = null" class="btn-remover-foto">Remover Foto</button>
              </div>
            </div>

            <div class="form-group">
              <label>Órgão Responsável</label>
              <select v-model="novaDenuncia.orgao.id" required>
                <option value="" disabled>Selecione...</option>
                <option v-for="org in orgaos" :key="org.id" :value="org.id">{{ org.nome }}</option>
              </select>
            </div>

            <div class="form-group">
              <label>Categoria</label>
              <select v-model="novaDenuncia.tipo.id" required>
                <option value="" disabled>Selecione...</option>
                <option v-for="tipo in tipos" :key="tipo.id" :value="tipo.id">{{ tipo.nome }}</option>
              </select>
            </div>

            <div class="form-group">
              <label>Urgência (1 a 5)</label>
              <div class="urgency-picker">
                <input type="range" min="1" max="5" v-model="novaDenuncia.urgencia" class="slider" />
                <span class="urg-value" :class="'text-u-'+novaDenuncia.urgencia">{{ novaDenuncia.urgencia }}</span>
              </div>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn-submit" :disabled="carregando">
              {{ carregando ? 'Processando...' : 'Registrar Denúncia' }}
            </button>
          </div>
        </form>
      </section>

      <section class="panel">
        <div class="panel-header border-b">
          <h3>Meu Histórico</h3>
        </div>

        <div v-if="minhasDenuncias.length === 0" class="empty-state">
          Você não possui nenhum registro ativo.
        </div>

        <div class="history-grid" v-else>
          <div v-for="den in minhasDenuncias" :key="den.id" class="history-card">
            <div class="hc-top">
               <span class="hc-date">{{ den.data || 'Hoje' }}</span>
               <span class="urgency" :class="'u-'+den.urgencia">Lvl {{ den.urgencia }}</span>
            </div>
            <h4>{{ den.titulo }}</h4>
            <p>{{ den.desc }}</p>
            
           <img v-if="den.foto" :src="'http://localhost:8080/uploads/' + den.foto" class="foto-denuncia" alt="Foto da denúncia" />
            <div class="hc-foot">
              <span class="tag">{{ den.orgao?.nome }}</span>
              <span class="tag">{{ den.tipo?.nome }}</span>
            </div>

            <div v-if="den.feedback" class="feedback-box">
              <strong>Resposta Oficial:</strong>
              <p>{{ den.feedback.texto }}</p>
            </div>
          </div>
        </div>
      </section>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../services/api';

const router = useRouter();
const orgaos = ref([]); const tipos = ref([]); const minhasDenuncias = ref([]); const carregando = ref(false);

const denPadrao = { titulo: '', desc: '', urgencia: 1, orgao: { id: '' }, tipo: { id: '' }, fotoUrl: null };
const novaDenuncia = ref({ ...denPadrao });

onMounted(async () => { await carregarListas(); await carregarMinhasDenuncias(); });

const carregarListas = async () => {
  try {
    const resOrgaos = await api.get('/cidadao/orgaos'); const resTipos = await api.get('/cidadao/tipos');
    orgaos.value = resOrgaos.data; tipos.value = resTipos.data;
  } catch (error) { console.error(error); }
};

const carregarMinhasDenuncias = async () => {
  try { const res = await api.get('/cidadao/denuncias'); minhasDenuncias.value = res.data; } catch (error) { console.error(error); }
};

const arquivoFoto = ref(null);

const processarFoto = (event) => {
  const file = event.target.files[0];
  if (!file) return;
  arquivoFoto.value = file;
  novaDenuncia.value.fotoUrl = URL.createObjectURL(file);
};

// Modifique o envio
const enviarDenuncia = async () => {
  carregando.value = true;
  try {
    novaDenuncia.value.data = new Date().toISOString().split('T')[0];

    // O Segredo: Usar FormData para enviar Arquivo + JSON
    const formData = new FormData();
    
    // Anexa a denúncia como um Blob JSON
    const denunciaJson = JSON.stringify({
      titulo: novaDenuncia.value.titulo,
      desc: novaDenuncia.value.desc,
      urgencia: novaDenuncia.value.urgencia,
      data: novaDenuncia.value.data,
      orgao: novaDenuncia.value.orgao,
      tipo: novaDenuncia.value.tipo
    });
    formData.append('denuncia', new Blob([denunciaJson], { type: 'application/json' }));

    if (arquivoFoto.value) {
      formData.append('foto', arquivoFoto.value);
    }

    // Axios envia automaticamente como multipart/form-data quando recebe FormData
    await api.post('/cidadao/denuncias', formData); 
    
    alert('Denúncia registrada com sucesso!');
    novaDenuncia.value = { titulo: '', desc: '', urgencia: 1, orgao: { id: '' }, tipo: { id: '' }, fotoUrl: null };
    arquivoFoto.value = null;
    await carregarMinhasDenuncias();
  } catch (error) { 
    console.error(error);
    alert('Erro no envio.'); 
  } finally { 
    carregando.value = false; 
  }
};

const sair = () => { localStorage.clear(); router.push('/'); };
</script>

<style scoped>
.layout { min-height: 100vh; background: var(--bg-base); }
.topbar { position: sticky; top: 0; z-index: 10; display: flex; justify-content: space-between; align-items: center; padding: 1rem 2rem; background: rgba(10, 10, 10, 0.8); backdrop-filter: blur(12px); border-bottom: 1px solid var(--border-color); }
.brand { font-weight: 700; font-size: 1.2rem; color: var(--text-primary); letter-spacing: -0.5px; }
.btn-ghost { background: transparent; border: 1px solid var(--border-color); color: var(--text-secondary); padding: 0.5rem 1rem; border-radius: 6px; font-size: 0.85rem; cursor: pointer; transition: 0.2s; }
.btn-ghost:hover { color: var(--text-primary); border-color: #52525b; background: var(--bg-elevated); }

.container { max-width: 900px; margin: 0 auto; padding: 2rem; }
.panel { background: var(--bg-surface); border: 1px solid var(--border-color); border-radius: 12px; padding: 2rem; }
.mb-8 { margin-bottom: 2rem; }

.panel-header h3 { font-size: 1.25rem; font-weight: 600; color: var(--text-primary); }
.subtitle { color: var(--text-secondary); font-size: 0.9rem; margin-top: 0.2rem; }
.border-b { border-bottom: 1px solid var(--border-color); padding-bottom: 1.2rem; margin-bottom: 1.5rem; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1.5rem; }
.span-full { grid-column: 1 / -1; }
.form-group label { display: block; font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 0.5rem; font-weight: 500; }

input, select, textarea { width: 100%; padding: 0.75rem 1rem; background: var(--bg-base); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-primary); font-size: 0.95rem; transition: 0.2s; }
input:focus, select:focus, textarea:focus { outline: none; border-color: var(--accent); box-shadow: 0 0 0 2px var(--ring); }
textarea { resize: vertical; }

.file-input { padding: 0.5rem; background: var(--bg-elevated); font-size: 0.85rem; cursor: pointer; }
.image-preview { margin-top: 1rem; display: flex; flex-direction: column; align-items: flex-start; gap: 0.5rem; }
.image-preview img { max-width: 200px; max-height: 150px; border-radius: 8px; border: 1px solid var(--border-color); }
.btn-remover-foto { background: var(--danger); color: white; border: none; padding: 0.4rem 0.8rem; border-radius: 4px; font-size: 0.8rem; cursor: pointer; }

.urgency-picker { display: flex; align-items: center; gap: 1rem; height: 42px; }
.slider { flex: 1; accent-color: var(--accent); cursor: pointer; }
.urg-value { font-weight: 700; font-size: 1.2rem; width: 24px; text-align: center; }
.text-u-1, .text-u-2 { color: var(--accent); } .text-u-3 { color: #eab308; } .text-u-4, .text-u-5 { color: var(--danger); }

.form-actions { margin-top: 2rem; display: flex; justify-content: flex-end; }
.btn-submit { background: var(--accent); color: #fff; padding: 0.8rem 2rem; border: none; border-radius: 8px; font-weight: 600; font-size: 0.95rem; cursor: pointer; transition: 0.2s; box-shadow: 0 4px 12px var(--ring); }
.btn-submit:hover:not(:disabled) { background: var(--accent-hover); transform: translateY(-2px); }
.btn-submit:disabled { opacity: 0.6; cursor: not-allowed; }

.history-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(350px, 1fr)); gap: 1.5rem; }
.history-card { background: var(--bg-base); border: 1px solid var(--border-color); border-radius: 10px; padding: 1.2rem; display: flex; flex-direction: column; gap: 0.8rem; transition: 0.2s; }
.history-card:hover { border-color: #52525b; transform: translateY(-2px); }
.hc-top { display: flex; justify-content: space-between; align-items: center; }
.hc-date { font-size: 0.75rem; color: #71717a; }
.history-card h4 { font-size: 1.05rem; font-weight: 500; margin: 0; }
.history-card p { font-size: 0.9rem; color: var(--text-secondary); line-height: 1.5; flex: 1; }
.foto-denuncia { 
  width: 100%; 
  height: 200px; 
  object-fit: contain; 
  background-color: #000;
  border-radius: 6px; 
  border: 1px solid var(--border-color); 
}
.tag { background: var(--bg-elevated); color: var(--text-secondary); padding: 0.2rem 0.6rem; border-radius: 6px; font-size: 0.75rem; }

.feedback-box { margin-top: 1rem; padding: 0.8rem; background: rgba(16, 185, 129, 0.1); border-left: 3px solid var(--accent); border-radius: 4px; }
.feedback-box strong { color: var(--accent); font-size: 0.85rem; display: block; margin-bottom: 0.3rem; }
.feedback-box p { font-size: 0.9rem; color: var(--text-primary); margin: 0; }

.urgency { font-size: 0.7rem; padding: 0.2rem 0.5rem; border-radius: 4px; font-weight: 600; text-transform: uppercase; }
.u-1 { background: rgba(16, 185, 129, 0.1); color: var(--accent); }
.u-2, .u-3 { background: rgba(234, 179, 8, 0.1); color: #eab308; }
.u-4, .u-5 { background: rgba(239, 68, 68, 0.1); color: var(--danger); }
.empty-state { text-align: center; padding: 4rem 2rem; color: var(--text-secondary); border: 1px dashed var(--border-color); border-radius: 8px; font-size: 0.9rem; }
</style>