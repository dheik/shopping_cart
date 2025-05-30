-- Tabela Cliente
CREATE TABLE IF NOT EXISTS Cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefone VARCHAR(20),
    endereco VARCHAR(200)
);

-- Tabela Produto
CREATE TABLE IF NOT EXISTS Produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco NUMERIC(10,2) NOT NULL,
    estoque INTEGER NOT NULL,
    categoria VARCHAR(50),
    codigoBarras VARCHAR(20) UNIQUE
);

-- Tabela Carrinho
CREATE TABLE IF NOT EXISTS Carrinho (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    valor_total NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

-- Tabela ItemCarrinho
CREATE TABLE IF NOT EXISTS ItemCarrinho (
    id SERIAL PRIMARY KEY,
    carrinho_id INTEGER NOT NULL,
    produto_id INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    FOREIGN KEY (carrinho_id) REFERENCES Carrinho(id),
    FOREIGN KEY (produto_id) REFERENCES Produto(id)
);

-- Dados de exemplo para Cliente
INSERT INTO Cliente (nome, cpf, email, telefone, endereco) VALUES
    ('Victor Thiago', '123.456.789-00', 'vbitkill@email.com', '(11) 98765-4321', 'Rua A, 123'),
    ('Maria Santos', '987.654.321-00', 'maria@email.com', '(11) 91234-5678', 'Rua B, 456');

-- Dados de exemplo para Produto
INSERT INTO Produto (nome, descricao, preco, estoque, categoria, codigoBarras) VALUES 
    ('Notebook Dell', 'Notebook Dell Inspiron 15 8GB RAM 256GB SSD', 3499.99, 10, 'Informática', '7891234567890'),
    ('Smartphone Samsung', 'Samsung Galaxy S21 128GB', 2999.99, 15, 'Celulares', '7899876543210'),
    ('Smart TV LG', 'Smart TV LG 50" 4K', 2599.99, 8, 'Eletrônicos', '7897654321098'),
    ('Fone de Ouvido JBL', 'Fone de Ouvido JBL Bluetooth', 199.99, 30, 'Acessórios', '7891234509876'),
    ('Mouse Logitech', 'Mouse sem fio Logitech', 89.99, 25, 'Informática', '7893216549870');
