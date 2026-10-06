-- =====================================================================
-- DunderSystem - V1: criação do schema inicial
-- Entidades: funcionario, cliente, produto, pedido_venda, item_pedido
-- =====================================================================

-- Funcionários (usuários do sistema).
-- perfil: ADMIN (gerente) | USER (vendedor) | ESTOQUE (almoxarifado)
CREATE TABLE funcionario (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    nome          VARCHAR(120) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    senha         VARCHAR(100) NOT NULL,           -- hash BCrypt
    perfil        VARCHAR(20)  NOT NULL,
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    data_criacao  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_funcionario_email UNIQUE (email),
    CONSTRAINT ck_funcionario_perfil CHECK (perfil IN ('ADMIN', 'USER', 'ESTOQUE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Clientes (carteira de clientes, ligada ao vendedor responsável).
CREATE TABLE cliente (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    razao_social   VARCHAR(150) NOT NULL,
    documento      VARCHAR(18)  NOT NULL,          -- CNPJ ou CPF
    email          VARCHAR(150),
    telefone       VARCHAR(20),
    endereco       VARCHAR(200),
    cidade         VARCHAR(80),
    estado         CHAR(2),
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE,
    vendedor_id    BIGINT       NOT NULL,
    data_criacao   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_cliente_documento UNIQUE (documento),
    CONSTRAINT fk_cliente_vendedor FOREIGN KEY (vendedor_id) REFERENCES funcionario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Produtos (papéis de diferentes tipos e gramaturas).
CREATE TABLE produto (
    id                 BIGINT        NOT NULL AUTO_INCREMENT,
    nome               VARCHAR(120)  NOT NULL,
    tipo               VARCHAR(50)   NOT NULL,     -- Sulfite, Couché, Reciclado, Kraft...
    gramatura          INT           NOT NULL,     -- g/m²
    formato            VARCHAR(10)   NOT NULL,     -- A4, A3, Carta, Ofício...
    preco_unitario     DECIMAL(10,2) NOT NULL,     -- preço por resma
    quantidade_estoque INT           NOT NULL DEFAULT 0,
    estoque_minimo     INT           NOT NULL DEFAULT 0,
    ativo              BOOLEAN       NOT NULL DEFAULT TRUE,
    data_criacao       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT ck_produto_preco CHECK (preco_unitario >= 0),
    CONSTRAINT ck_produto_estoque CHECK (quantidade_estoque >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Pedidos de venda.
-- status: RASCUNHO | AGUARDANDO_APROVACAO | APROVADO | RECUSADO | CONFIRMADO | CANCELADO
CREATE TABLE pedido_venda (
    id                    BIGINT        NOT NULL AUTO_INCREMENT,
    cliente_id            BIGINT        NOT NULL,
    vendedor_id           BIGINT        NOT NULL,
    aprovador_id          BIGINT        NULL,      -- gerente que decidiu sobre o desconto
    status                VARCHAR(30)   NOT NULL DEFAULT 'RASCUNHO',
    desconto_percentual   DECIMAL(5,2)  NOT NULL DEFAULT 0.00,
    valor_bruto           DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    valor_total           DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    observacao            VARCHAR(255),
    justificativa_decisao VARCHAR(255),
    data_criacao          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_decisao          DATETIME      NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pedido_cliente   FOREIGN KEY (cliente_id)   REFERENCES cliente (id),
    CONSTRAINT fk_pedido_vendedor  FOREIGN KEY (vendedor_id)  REFERENCES funcionario (id),
    CONSTRAINT fk_pedido_aprovador FOREIGN KEY (aprovador_id) REFERENCES funcionario (id),
    CONSTRAINT ck_pedido_desconto CHECK (desconto_percentual BETWEEN 0 AND 100),
    CONSTRAINT ck_pedido_status CHECK (status IN
        ('RASCUNHO','AGUARDANDO_APROVACAO','APROVADO','RECUSADO','CONFIRMADO','CANCELADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Itens do pedido (carrinho).
CREATE TABLE item_pedido (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    pedido_id       BIGINT        NOT NULL,
    produto_id      BIGINT        NOT NULL,
    quantidade      INT           NOT NULL,
    preco_unitario  DECIMAL(10,2) NOT NULL,        -- preço no momento da venda
    subtotal        DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_item_pedido  FOREIGN KEY (pedido_id)  REFERENCES pedido_venda (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produto (id),
    CONSTRAINT ck_item_quantidade CHECK (quantidade > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_cliente_vendedor ON cliente (vendedor_id);
CREATE INDEX idx_pedido_status    ON pedido_venda (status);
CREATE INDEX idx_pedido_cliente   ON pedido_venda (cliente_id);
CREATE INDEX idx_item_pedido      ON item_pedido (pedido_id);
