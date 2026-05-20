package edu.fipp.ativooperante.services;

import edu.fipp.ativooperante.entities.Usuario;
import edu.fipp.ativooperante.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario findByEmail(String email) {
        List<Usuario> usuarios = usuarioRepository.findUsuarioByEmail(email);

        if (usuarios != null && !usuarios.isEmpty()) {
            return usuarios.get(0);
        }

        return null;
    }

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}