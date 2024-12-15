CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE plano (
    id BIGSERIAL PRIMARY KEY,
    dificuldade VARCHAR(15) NOT NULL DEFAULT 'NAO_DEFINIDO',
    cliente_id BIGINT UNIQUE,
    CONSTRAINT fk_cliente_plano FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);


CREATE TABLE equipamento (
    codigo BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT,
    status TEXT DEFAULT 'ATIVO'
);

CREATE TABLE item (
    id BIGSERIAL PRIMARY KEY,
    descricao TEXT NOT NULL,
    plano_id BIGINT,
    equipamento_id BIGINT,
    CONSTRAINT fk_plano_item FOREIGN KEY (plano_id) REFERENCES plano (id),
    CONSTRAINT fk_equipamento_item FOREIGN KEY (equipamento_id) REFERENCES equipamento (codigo)
);