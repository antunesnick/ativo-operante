package edu.fipp.ativooperante.services;

import edu.fipp.ativooperante.entities.Denuncia;
import edu.fipp.ativooperante.entities.Feedback;
import edu.fipp.ativooperante.repositories.DenunciaRepository;
import edu.fipp.ativooperante.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    public Feedback registrarFeedback(Long denunciaId, Feedback feedback) {
        Denuncia denuncia = denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));
        feedback.setDenuncia(denuncia);
        return feedbackRepository.save(feedback);
    }
}