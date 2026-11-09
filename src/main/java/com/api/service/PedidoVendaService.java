package com.api.service;

import com.api.model.*;
import com.api.model.enums.StatusPedido;
import com.api.repository.*;
import com.api.dto.PedidoVendaResponse;
import com.api.event.PedidoVendaAlteradoEvent;
import com.api.event.PedidoVendaAlteradoEvent.Operacao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoVendaService {
    private final PedidoVendaRepository repository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoVendaService(PedidoVendaRepository repository, ClienteRepository clienteRepository,
                              FuncionarioRepository funcionarioRepository,
                              ProdutoRepository produtoRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public Page<PedidoVendaResponse> listar(StatusPedido status, Pageable pageable) {
        Page<PedidoVenda> pedidos = status == null
                ? repository.findAll(pageable) : repository.findByStatus(status, pageable);
        return pedidos.map(PedidoVendaResponse::de);
    }

    @Transactional(readOnly = true)
    public PedidoVendaResponse buscar(Long codigo) {
        return PedidoVendaResponse.de(repository.findById(codigo).orElseThrow(
                () -> new EmptyResultDataAccessException("Pedido não encontrado", 1)));
    }

    @Transactional
    public PedidoVendaResponse salvar(PedidoVenda pedido) {
        pedido.setId(null);
        validarRelacionamentos(pedido);
        recalcular(pedido);
        PedidoVenda salvo = repository.save(pedido);
        publicarEvento(salvo.getId(), Operacao.CRIADO);
        return PedidoVendaResponse.de(salvo);
    }

    @Transactional
    public PedidoVendaResponse atualizar(Long codigo, PedidoVenda fonte) {
        PedidoVenda destino = repository.findById(codigo).orElseThrow(
                () -> new EmptyResultDataAccessException("Pedido não encontrado", 1));
        fonte.setCliente(clienteAtivo(fonte.getCliente()));
        fonte.setVendedor(funcionarioAtivo(fonte.getVendedor(), "vendedor"));
        fonte.setAprovador(funcionarioOpcional(fonte.getAprovador()));
        List<ItemPedido> itens = prepararItens(fonte);
        BeanUtils.copyProperties(fonte, destino, "id", "cliente", "vendedor", "aprovador", "itens",
                "valorBruto", "valorTotal", "dataCriacao");
        destino.setCliente(fonte.getCliente());
        destino.setVendedor(fonte.getVendedor());
        destino.setAprovador(fonte.getAprovador());
        destino.getItens().clear();
        itens.forEach(destino::adicionarItem);
        recalcular(destino);
        PedidoVenda atualizado = repository.save(destino);
        publicarEvento(atualizado.getId(), Operacao.ATUALIZADO);
        return PedidoVendaResponse.de(atualizado);
    }

    @Transactional
    public void excluir(Long codigo) {
        PedidoVenda pedido = repository.findById(codigo).orElseThrow(
                () -> new EmptyResultDataAccessException("Pedido não encontrado", 1));
        repository.delete(pedido);
        publicarEvento(codigo, Operacao.EXCLUIDO);
    }

    private void publicarEvento(Long codigo, Operacao operacao) {
        eventPublisher.publishEvent(new PedidoVendaAlteradoEvent(codigo, operacao, Instant.now()));
    }

    private void validarRelacionamentos(PedidoVenda pedido) {
        pedido.setCliente(clienteAtivo(pedido.getCliente()));
        pedido.setVendedor(funcionarioAtivo(pedido.getVendedor(), "vendedor"));
        pedido.setAprovador(funcionarioOpcional(pedido.getAprovador()));
        List<ItemPedido> itens = prepararItens(pedido);
        if (pedido.getItens() == null) {
            pedido.setItens(new ArrayList<>());
        } else {
            pedido.getItens().clear();
        }
        itens.forEach(pedido::adicionarItem);
    }

    private List<ItemPedido> prepararItens(PedidoVenda pedido) {
        List<ItemPedido> preparados = new ArrayList<>();
        if (pedido.getItens() == null) {
            return preparados;
        }
        for (ItemPedido item : pedido.getItens()) {
            if (item.getQuantidade() == null || item.getQuantidade() <= 0
                    || item.getProduto() == null || item.getProduto().getId() == null) {
                throw new IllegalArgumentException("Cada item deve informar um produto e uma quantidade positiva.");
            }
            Produto produto = produtoRepository.findById(item.getProduto().getId()).orElseThrow(
                    () -> new EmptyResultDataAccessException("Produto do pedido não encontrado", 1));
            if (!Boolean.TRUE.equals(produto.getAtivo())) {
                throw new IllegalStateException("Não é possível adicionar um produto inativo ao pedido.");
            }
            ItemPedido novoItem = new ItemPedido();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(item.getQuantidade());
            novoItem.setPrecoUnitario(produto.getPrecoUnitario());
            novoItem.setSubtotal(produto.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            preparados.add(novoItem);
        }
        return preparados;
    }

    private Cliente clienteAtivo(Cliente referencia) {
        if (referencia == null || referencia.getId() == null) {
            throw new IllegalArgumentException("Informe o código do cliente.");
        }
        Cliente cliente = clienteRepository.findById(referencia.getId()).orElseThrow(
                () -> new EmptyResultDataAccessException("Cliente não encontrado", 1));
        if (!Boolean.TRUE.equals(cliente.getAtivo())) {
            throw new IllegalStateException("Não é possível criar pedido para cliente inativo.");
        }
        return cliente;
    }

    private Funcionario funcionarioAtivo(Funcionario referencia, String papel) {
        Funcionario funcionario = funcionarioOpcional(referencia);
        if (funcionario == null) {
            throw new IllegalArgumentException("Informe o código do " + papel + ".");
        }
        if (!Boolean.TRUE.equals(funcionario.getAtivo())) {
            throw new IllegalStateException("Não é possível associar um " + papel + " inativo ao pedido.");
        }
        return funcionario;
    }

    private Funcionario funcionarioOpcional(Funcionario referencia) {
        if (referencia == null) {
            return null;
        }
        if (referencia.getId() == null) {
            throw new IllegalArgumentException("Informe o código do funcionário relacionado.");
        }
        return funcionarioRepository.findById(referencia.getId()).orElseThrow(
                () -> new EmptyResultDataAccessException("Funcionário não encontrado", 1));
    }

    private void recalcular(PedidoVenda pedido) {
        if (pedido.getStatus() == null) {
            pedido.setStatus(StatusPedido.RASCUNHO);
        }
        BigDecimal bruto = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal desconto = pedido.getDescontoPercentual() == null
                ? BigDecimal.ZERO : pedido.getDescontoPercentual();
        if (desconto.signum() < 0 || desconto.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("O desconto deve estar entre 0 e 100 por cento.");
        }
        pedido.setDescontoPercentual(desconto);
        pedido.setValorBruto(bruto.setScale(2, RoundingMode.HALF_UP));
        pedido.setValorTotal(bruto.multiply(BigDecimal.ONE.subtract(
                desconto.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP));
    }
}
