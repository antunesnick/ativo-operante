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

    @GetMapping("/orgaos")
    public ResponseEntity<?> listarOrgaos() {
        return ResponseEntity.ok(orgaoService.findAll());
    }

    @PostMapping("/orgaos")
    public ResponseEntity<?> salvarOrgao(@RequestBody Orgao orgao) {
        return ResponseEntity.ok(orgaoService.save(orgao));
    }

    @DeleteMapping("/orgaos/{id}")
    public ResponseEntity<?> apagarOrgao(@PathVariable Long id) {
        orgaoService.delete(id);
        return ResponseEntity.ok("Órgão apagado com sucesso.");
    }

    @GetMapping("/tipos")
    public ResponseEntity<?> listarTipos() {
        return ResponseEntity.ok(tipoService.findAll());
    }

    @PostMapping("/tipos")
    public ResponseEntity<?> salvarTipo(@RequestBody Tipo tipo) {
        return ResponseEntity.ok(tipoService.save(tipo));
    }

    @DeleteMapping("/tipos/{id}")
    public ResponseEntity<?> apagarTipo(@PathVariable Long id) {
        tipoService.delete(id);
        return ResponseEntity.ok("Tipo apagado com sucesso.");
    }

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