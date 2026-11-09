package com.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
@Getter @Setter @NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String nome;
    @NotBlank
    @Size(max = 50)
    private String tipo;
    @NotNull
    @Min(1)
    private Integer gramatura;
    @NotBlank
    @Size(max = 10)
    private String formato;

    @Column(name = "preco_unitario")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_estoque")
    @NotNull
    @Min(0)
    private Integer quantidadeEstoque = 0;

    @Column(name = "estoque_minimo")
    @NotNull
    @Min(0)
    private Integer estoqueMinimo = 0;

    private Boolean ativo = true;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;
}
