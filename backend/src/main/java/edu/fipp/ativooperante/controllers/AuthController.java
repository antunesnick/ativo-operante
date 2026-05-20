package edu.fipp.ativooperante.controllers;

import edu.fipp.ativooperante.entities.Usuario;
import edu.fipp.ativooperante.security.JwtUtil;
import edu.fipp.ativooperante.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*") // Permite que o Vue.js acesse
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCidadao(@RequestBody Usuario novoUsuario) {
        novoUsuario.setNivel(2);
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));

        usuarioService.save(novoUsuario);
        return ResponseEntity.ok("Cidadão cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());

        if (usuario != null)  {
            if (passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getNivel());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("nivel", usuario.getNivel());

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}