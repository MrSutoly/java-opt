package com.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter @Setter @NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social")
    @NotBlank
    @Size(max = 150)
    private String razaoSocial;

    @NotBlank
    @Size(max = 18)
    private String documento;
    @Email
    @Size(max = 150)
    private String email;
    @Size(max = 20)
    private String telefone;
    @Size(max = 200)
    private String endereco;
    @Size(max = 80)
    private String cidade;
    @Size(max = 2)
    private String estado;
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    @NotNull
    private Funcionario vendedor;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;
}
