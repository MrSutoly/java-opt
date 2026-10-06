package br.edu.utfpr.dundersystem.model;

import jakarta.persistence.*;
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

    private String nome;
    private String tipo;
    private Integer gramatura;
    private String formato;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque = 0;

    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo = 0;

    private Boolean ativo = true;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;
}
