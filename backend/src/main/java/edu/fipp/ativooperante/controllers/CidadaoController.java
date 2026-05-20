package edu.fipp.ativooperante.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.fipp.ativooperante.entities.Denuncia;
import edu.fipp.ativooperante.services.DenunciaService;
import edu.fipp.ativooperante.services.OrgaoService;
import edu.fipp.ativooperante.services.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

    @PostMapping(value = "/denuncias", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> enviarDenuncia(
            @RequestPart("denuncia") String denunciaJson,
            @RequestPart(value = "foto", required = false) MultipartFile foto,
            Authentication authentication) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // convete json para o objeto
            Denuncia denuncia = objectMapper.readValue(denunciaJson, Denuncia.class);

            if (foto != null && !foto.isEmpty()) {
                String nomeArquivo = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                Path caminhoPasta = Paths.get("uploads/");

                if (!Files.exists(caminhoPasta)) {
                    Files.createDirectories(caminhoPasta);
                }

                Path caminhoArquivo = caminhoPasta.resolve(nomeArquivo);
                foto.transferTo(caminhoArquivo);

                denuncia.setFoto(nomeArquivo);
            }

            Denuncia salva = denunciaService.save(denuncia, authentication.getName());
            return ResponseEntity.ok(salva);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao processar arquivo ou salvar denúncia.");
        }
    }

    @GetMapping("/denuncias")
    public ResponseEntity<?> minhasDenuncias(Authentication authentication) {
        return ResponseEntity.ok(denunciaService.findByCidadao(authentication.getName()));
    }
}