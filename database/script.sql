CREATE SCHEMA IF NOT EXISTS folha_pagamento;

CREATE TABLE IF NOT EXISTS folha_pagamento.funcionario (
    id SERIAL PRIMARY KEY, -- SERIAL gera o ID automático que seu Java espera
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL, -- UNIQUE impede CPFs duplicados
    data_nascimento DATE NOT NULL,
    salario_bruto NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS folha_pagamento.dependente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    parentesco VARCHAR(50) NOT NULL,
    id_funcionario INTEGER NOT NULL,
    -- Define a chave estrangeira: se apagar o funcionário, apaga os dependentes
    CONSTRAINT fk_funcionario FOREIGN KEY (id_funcionario) 
        REFERENCES folha_pagamento.funcionario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS folha_pagamento.folha_pagamento (
    id SERIAL PRIMARY KEY,
    id_funcionario INTEGER NOT NULL,
    data_pagamento DATE NOT NULL,
    desconto_inss NUMERIC(10, 2) NOT NULL,
    desconto_ir NUMERIC(10, 2) NOT NULL,
    salario_liquido NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_funcionario_folha FOREIGN KEY (id_funcionario) 
        REFERENCES folha_pagamento.funcionario(id) ON DELETE CASCADE
);