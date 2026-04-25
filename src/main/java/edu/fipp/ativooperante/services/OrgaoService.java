package edu.fipp.ativooperante.services;

import edu.fipp.ativooperante.entities.Orgao;
import edu.fipp.ativooperante.repositories.OrgaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgaoService {

    @Autowired
    private OrgaoRepository orgaoRepository;

    public List<Orgao> findAll() {
        return orgaoRepository.findAll();
    }

    public Orgao findById(Long id) {
        return orgaoRepository.findById(id).orElse(null);
    }

    public Orgao save(Orgao orgao) {
        return orgaoRepository.save(orgao);
    }

    public void delete(Long id) {
        orgaoRepository.deleteById(id);
    }
}