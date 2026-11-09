package com.api.model;

import com.api.model.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    @NotNull
    private Funcionario vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprovador_id")
    private Funcionario aprovador;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusPedido status = StatusPedido.RASCUNHO;

    @Column(name = "desconto_percentual")
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal descontoPercentual = BigDecimal.ZERO;

    @Column(name = "valor_bruto")
    private BigDecimal valorBruto = BigDecimal.ZERO;

    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Size(max = 255)
    private String observacao;

    @Column(name = "justificativa_decisao")
    @Size(max = 255)
    private String justificativaDecisao;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_decisao")
    private LocalDateTime dataDecisao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }
}
