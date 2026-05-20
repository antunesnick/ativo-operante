package edu.fipp.ativooperante.services;

import edu.fipp.ativooperante.entities.Tipo;
import edu.fipp.ativooperante.repositories.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService {

    @Autowired
    private TipoRepository tipoRepository;

    public List<Tipo> findAll() {
        return tipoRepository.findAll();
    }

    public Tipo findById(Long id) {
        return tipoRepository.findById(id).orElse(null);
    }

    public Tipo save(Tipo tipo) {
        return tipoRepository.save(tipo);
    }

    public void delete(Long id) {
        tipoRepository.deleteById(id);
    }
}