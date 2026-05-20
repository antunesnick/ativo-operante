package edu.fipp.ativooperante.controllers;

import edu.fipp.ativooperante.entities.Denuncia;
import edu.fipp.ativooperante.entities.Feedback;
import edu.fipp.ativooperante.entities.Orgao;
import edu.fipp.ativooperante.entities.Tipo;
import edu.fipp.ativooperante.services.DenunciaService;
import edu.fipp.ativooperante.services.FeedbackService;
import edu.fipp.ativooperante.services.OrgaoService;
import edu.fipp.ativooperante.services.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adm")
@CrossOrigin("*")
public class AdmController {

    @Autowired
    private OrgaoService orgaoService;

    @Autowired
    private TipoService tipoService;

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private FeedbackService feedbackService;

    // ==========================================
    // CRUD DE ÓRGÃOS
    // ==========================================

    @GetMapping("/orgaos")
    public ResponseEntity<?> listarOrgaos() {
        return ResponseEntity.ok(orgaoService.findAll());
    }

    @GetMapping("/orgaos/{id}")
    public ResponseEntity<?> buscarOrgao(@PathVariable Long id) {
        Orgao orgao = orgaoService.findById(id);
        if (orgao != null) {
            return ResponseEntity.ok(orgao);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/orgaos")
    public ResponseEntity<?> salvarOrgao(@RequestBody Orgao orgao) {
        return ResponseEntity.ok(orgaoService.save(orgao));
    }

    @PutMapping("/orgaos/{id}")
    public ResponseEntity<?> alterarOrgao(@PathVariable Long id, @RequestBody Orgao orgaoAtualizado) {
        Orgao orgaoExistente = orgaoService.findById(id);
        if (orgaoExistente != null) {
            orgaoExistente.setNome(orgaoAtualizado.getNome());
            return ResponseEntity.ok(orgaoService.save(orgaoExistente));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/orgaos/{id}")
    public ResponseEntity<?> apagarOrgao(@PathVariable Long id) {
        orgaoService.delete(id);
        return ResponseEntity.ok("Órgão apagado com sucesso.");
    }

    // ==========================================
    // CRUD DE TIPOS DE PROBLEMA
    // ==========================================

    @GetMapping("/tipos")
    public ResponseEntity<?> listarTipos() {
        return ResponseEntity.ok(tipoService.findAll());
    }

    @GetMapping("/tipos/{id}")
    public ResponseEntity<?> buscarTipo(@PathVariable Long id) {
        Tipo tipo = tipoService.findById(id);
        if (tipo != null) {
            return ResponseEntity.ok(tipo);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/tipos")
    public ResponseEntity<?> salvarTipo(@RequestBody Tipo tipo) {
        return ResponseEntity.ok(tipoService.save(tipo));
    }

    @PutMapping("/tipos/{id}")
    public ResponseEntity<?> alterarTipo(@PathVariable Long id, @RequestBody Tipo tipoAtualizado) {
        Tipo tipoExistente = tipoService.findById(id);
        if (tipoExistente != null) {
            tipoExistente.setNome(tipoAtualizado.getNome());
            return ResponseEntity.ok(tipoService.save(tipoExistente));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tipos/{id}")
    public ResponseEntity<?> apagarTipo(@PathVariable Long id) {
        tipoService.delete(id);
        return ResponseEntity.ok("Tipo apagado com sucesso.");
    }

    // ==========================================
    // DENUNCIAS E FEEDBACK
    // ==========================================

    @GetMapping("/denuncias")
    public ResponseEntity<?> listarTodasDenuncias() {
        return ResponseEntity.ok(denunciaService.findAll());
    }

    @DeleteMapping("/denuncias/{id}")
    public ResponseEntity<?> apagarDenuncia(@PathVariable Long id) {
        denunciaService.delete(id);
        return ResponseEntity.ok("Denúncia apagada com sucesso.");
    }

    @PostMapping("/denuncias/{id}/feedback")
    public ResponseEntity<?> darFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.registrarFeedback(id, feedback));
    }
}