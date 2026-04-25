package edu.fipp.ativooperante.services;

import edu.fipp.ativooperante.entities.Denuncia;
import edu.fipp.ativooperante.entities.Usuario;
import edu.fipp.ativooperante.repositories.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Denuncia save(Denuncia denuncia, String emailUsuario) {
        Usuario usuario = usuarioService.findByEmail(emailUsuario);
        denuncia.setUsuario(usuario);
        denuncia.setData(LocalDate.now());

        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> findByCidadao(String email) {
        return denunciaRepository.findByUsuarioEmail(email);
    }

    public List<Denuncia> findAll() {
        return denunciaRepository.findAll();
    }

    public void delete(Long id) {
        denunciaRepository.deleteById(id);
    }
}