package edu.fipp.ativooperante.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "denuncia")
@NoArgsConstructor
@Getter
@Setter
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "den_id")
    private Long id;

    @Column(name = "den_titulo")
    private String titulo;

    @Column(name = "den_texto")
    private String desc;

    @Column(name = "den_urgencia")
    private int urgencia;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Orgao orgao;

    @Column(name = "den_data")
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "tip_id")
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "usu_id")
    private Usuario usuario;

}
