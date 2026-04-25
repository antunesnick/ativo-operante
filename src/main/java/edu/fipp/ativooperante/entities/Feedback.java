package edu.fipp.ativooperante.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "feedback")
@NoArgsConstructor
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Long id;

    @Column(name = "fee_texto")
    private String texto;

    @OneToOne
    @JoinColumn(name = "den_id")
    private Denuncia denuncia;
}
