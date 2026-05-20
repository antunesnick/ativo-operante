<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<style>
/* Importando uma fonte moderna (Inter) */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

:root {
  /* Paleta Premium Dark Mode */
  --bg-base: #0a0a0a;       /* Fundo da tela (preto profundo) */
  --bg-surface: #171717;    /* Fundo dos cards (cinza chumbo) */
  --bg-elevated: #262626;   /* Hover states e inputs */
  --border-color: #333333;  /* Bordas sutis */
  
  --text-primary: #ededed;  /* Branco suave (não cansa a vista) */
  --text-secondary: #a1a1aa;/* Cinza claro para descrições */
  
  --accent: #10b981;        /* Verde Esmeralda (Ação principal) */
  --accent-hover: #059669;  
  --danger: #ef4444;        /* Vermelho (Exclusões) */
  --danger-hover: #dc2626;

  --ring: rgba(16, 185, 129, 0.25); /* Efeito de brilho (glow) */
}

/* Reset Absoluto */
*, *::before, *::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'Inter', sans-serif;
  background-color: var(--bg-base);
  color: var(--text-primary);
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
  color-scheme: dark;
}

/* Scrollbar estilo MacOS (opcional, mas elegante) */
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-track { background: var(--bg-base); }
::-webkit-scrollbar-thumb { background: var(--bg-elevated); border-radius: 4px; }
::-webkit-scrollbar-thumb:hover { background: #404040; }
</style>