package edu.fipp.ativooperante.controllers;

import edu.fipp.ativooperante.entities.Denuncia;
import edu.fipp.ativooperante.services.DenunciaService;
import edu.fipp.ativooperante.services.OrgaoService;
import edu.fipp.ativooperante.services.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cidadao")
@CrossOrigin("*")
public class CidadaoController {

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private OrgaoService orgaoService;

    @Autowired
    private TipoService tipoService;

    @GetMapping("/orgaos")
    public ResponseEntity<?> listarOrgaos() {
        return ResponseEntity.ok(orgaoService.findAll());
    }

    @GetMapping("/tipos")
    public ResponseEntity<?> listarTipos() {
        return ResponseEntity.ok(tipoService.findAll());
    }

    @PostMapping("/denuncias")
    public ResponseEntity<?> enviarDenuncia(@RequestBody Denuncia denuncia, Authentication authentication) {
        // authentication.getName() pega o e-mail do token JWT automaticamente!
        Denuncia salva = denunciaService.save(denuncia, authentication.getName());
        return ResponseEntity.ok(salva);
    }

    @GetMapping("/denuncias")
    public ResponseEntity<?> minhasDenuncias(Authentication authentication) {
        return ResponseEntity.ok(denunciaService.findByCidadao(authentication.getName()));
    }
}