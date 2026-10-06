package br.edu.utfpr.dundersystem.model;

import br.edu.utfpr.dundersystem.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido_venda")
@Getter @Setter @NoArgsConstructor
public class PedidoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private Funcionario vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprovador_id")
    private Funcionario aprovador;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.RASCUNHO;

    @Column(name = "desconto_percentual")
    private BigDecimal descontoPercentual = BigDecimal.ZERO;

    @Column(name = "valor_bruto")
    private BigDecimal valorBruto = BigDecimal.ZERO;

    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    private String observacao;

    @Column(name = "justificativa_decisao")
    private String justificativaDecisao;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_decisao")
    private LocalDateTime dataDecisao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
}
