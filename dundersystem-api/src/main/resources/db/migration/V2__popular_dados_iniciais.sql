-- =====================================================================
-- DunderSystem - V2: dados iniciais (seed)
-- Senha de TODOS os usuários de teste: 123456 (hash BCrypt)
-- =====================================================================

INSERT INTO funcionario (nome, email, senha, perfil) VALUES
('Michael Scott',  'michael.scott@dundermifflin.com',  '$2a$10$GRG9fNDQCajNgEmwF2Cp.e2ps0vLi4v10/V8V6mHQ3JsNwMzsKl6i', 'ADMIN'),
('Dwight Schrute', 'dwight.schrute@dundermifflin.com', '$2a$10$GRG9fNDQCajNgEmwF2Cp.e2ps0vLi4v10/V8V6mHQ3JsNwMzsKl6i', 'USER'),
('Jim Halpert',    'jim.halpert@dundermifflin.com',    '$2a$10$GRG9fNDQCajNgEmwF2Cp.e2ps0vLi4v10/V8V6mHQ3JsNwMzsKl6i', 'USER'),
('Darryl Philbin', 'darryl.philbin@dundermifflin.com', '$2a$10$GRG9fNDQCajNgEmwF2Cp.e2ps0vLi4v10/V8V6mHQ3JsNwMzsKl6i', 'ESTOQUE');

INSERT INTO produto (nome, tipo, gramatura, formato, preco_unitario, quantidade_estoque, estoque_minimo) VALUES
('Sulfite Branco 75g A4',      'Sulfite',   75,  'A4',    24.90, 500, 100),
('Sulfite Branco 90g A4',      'Sulfite',   90,  'A4',    29.90, 300, 80),
('Sulfite Branco 75g A3',      'Sulfite',   75,  'A3',    49.90, 200, 50),
('Papel Reciclado 75g A4',     'Reciclado', 75,  'A4',    27.50, 250, 60),
('Couché Brilho 115g A4',      'Couché',    115, 'A4',    59.90, 120, 30),
('Couché Fosco 150g A4',       'Couché',    150, 'A4',    74.90, 100, 30),
('Papel Kraft 80g Ofício',     'Kraft',     80,  'Ofício',32.00, 150, 40),
('Papel Cartão 240g A4',       'Cartão',    240, 'A4',    89.90, 80,  20);

INSERT INTO cliente (razao_social, documento, email, telefone, endereco, cidade, estado, vendedor_id) VALUES
('Vance Refrigeration',        '11.222.333/0001-44', 'compras@vancerefrigeration.com', '(570) 555-0101', 'Rua Wallace, 100',  'Scranton',     'PA', 2),
('Lackawanna County',          '22.333.444/0001-55', 'suprimentos@lackawanna.gov',     '(570) 555-0102', 'Av. Central, 250',  'Scranton',     'PA', 2),
('Blue Cross of Pennsylvania', '33.444.555/0001-66', 'office@bluecross-pa.com',        '(570) 555-0103', 'Rua Harper, 45',    'Scranton',     'PA', 3),
('Prince Family Paper',        '44.555.666/0001-77', 'contato@princepaper.com',        '(570) 555-0104', 'Rua Prince, 12',    'Wilkes-Barre', 'PA', 3);
