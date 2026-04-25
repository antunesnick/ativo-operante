package edu.fipp.ativooperante.services;

import edu.fipp.ativooperante.entities.Usuario;
import edu.fipp.ativooperante.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario findByEmail(String email) {
        return usuarioRepository.findUsuarioByEmail(email).getFirst();
    }

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
