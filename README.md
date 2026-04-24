# 💼 Sistema de Folha de Pagamento
### Projeto Final — Programação Orientada a Objetos

---

## 📋 Sobre o Projeto

Sistema desenvolvido em Java para **calcular o salário líquido de funcionários**, aplicando os principais conceitos de Programação Orientada a Objetos. O programa lê um arquivo CSV de entrada com dados de funcionários e seus dependentes, calcula os descontos de INSS e IR conforme as tabelas oficiais e gera um arquivo CSV de saída com os resultados da folha de pagamento.

---

## 👥 Integrantes e Responsabilidades

| Integrante | Etapa | Responsabilidade |
|-----------|-------|-----------------|
| **Pedro Octavio** | Etapa 1 | Modelagem do domínio — classes `Pessoa` (abstrata), `Funcionario`, `Dependente`, `FolhaPagamento`, enum `Parentesco`, interface `Calculavel`, exceções `DependenteException` e `CpfDuplicadoException` |
| **Pedro Mello** | Etapa 2 + Shared | Leitura do CSV de entrada — `CsvReader`, `ICsvReader`; compartilha `ICsvService` e `CsvService` com Bruno Freitas |
| **Enzo Costa** | Etapa 3 | Lógica de cálculo — implementação de `calcularInss()` e `calcularIr()` progressivos dentro de `Funcionario` |
| **Bruno Freitas** | Etapa 4 + Shared | Geração do CSV de saída — método `gerarSaida()` em `CsvService`; compartilha `ICsvService` e `CsvService` com Pedro Mello |
| **Phelipe Damassio** | Etapa 5 | Banco de dados — persistência via JDBC (tabelas `funcionario`, `dependente`, `folha_pagamento`) |
| **Gabriel Botelho** | Etapa 5 | Banco de dados — criação de tabelas, inserções e tratamento de CPF duplicado via SQL |

---

## 🏗️ Estrutura do Projeto

```
src/
├── Main.java                          # Classe principal — orquestra o fluxo da aplicação
│
├── model/
│   ├── Pessoa.java                    # Classe abstrata base (nome, cpf, dataNascimento)
│   ├── Funcionario.java               # Herda Pessoa, implementa Calculavel
│   ├── Dependente.java                # Herda Pessoa, valida idade < 18 anos
│   ├── FolhaPagamento.java            # Consolida os dados de pagamento do funcionário
│   └── Parentesco.java                # Enum: FILHO | SOBRINHO | OUTROS
│
├── service/
│   ├── Calculavel.java                # Interface: calcularInss() e calcularIr()
│   ├── Calculo.java                   # Interface auxiliar de cálculo
│   ├── ICsvService.java               # Interface do serviço CSV (contrato de operações)
│   └── CsvService.java                # Implementação: carrega entrada, calcula folha e gera saída
│
├── util/
│   ├── ICsvReader.java                # Interface de leitura de CSV
│   └── CsvReader.java                 # Lê o CSV, cria objetos Funcionario e Dependente
│
└── exception/
    ├── DependenteException.java       # Lançada quando dependente tem 18+ anos
    └── CpfDuplicadoException.java     # Lançada quando CPF já foi cadastrado
```

---

## ⚙️ Funcionalidades Implementadas

- ✅ Leitura de **CSV de entrada** com funcionários e seus dependentes
- ✅ Separação de blocos por **linha em branco** no CSV
- ✅ Validação de **CPF único** via `HashSet<String>` (funcionários e dependentes)
- ✅ Validação de **idade do dependente** — rejeita com `DependenteException` se ≥ 18 anos
- ✅ Cálculo progressivo do **INSS** faixa a faixa (tabela 2025)
- ✅ Cálculo progressivo do **IR** com dedução por dependente (R$ 189,59 cada)
- ✅ Geração de **CSV de saída** formatado com os dados da folha
- ✅ Encapsulamento com getters/setters e validações nos construtores
- ✅ Contador estático de funcionários (`contadorFuncionarios`) em `Funcionario`
- 🔲 Persistência no banco de dados via JDBC *(Etapa 5 — em desenvolvimento)*

---

## 📊 Tabelas de Cálculo

### INSS — Cálculo Progressivo por Faixas (2025)

| Faixa | Remuneração (R$) | Alíquota |
|-------|-----------------|----------|
| Faixa 1 | Até 1.518,00 | 7,50% |
| Faixa 2 | De 1.518,01 até 2.793,88 | 9,00% |
| Faixa 3 | De 2.793,89 até 4.190,83 | 12,00% |
| Faixa 4 | De 4.190,84 até 8.157,41 | 14,00% |

> ⚠️ Salário acima de R$ 8.157,41: aplica-se 14% somente sobre R$ 8.157,41

### Imposto de Renda — Tabela Mensal

| Base de Cálculo (R$) | Alíquota |
|----------------------|----------|
| Até 2.259,00 | Isento |
| De 2.259,01 até 2.826,65 | 7,5% |
| De 2.826,66 até 3.751,05 | 15% |
| De 3.751,06 até 4.664,68 | 22,5% |
| Acima de 4.664,68 | 27,5% |

> 💡 Dedução por dependente: **R$ 189,59** por dependente cadastrado  
> Base de cálculo do IR = `salarioBruto - descontoInss - (189,59 × númeroDependentes)`

---

## 📂 Formato dos Arquivos

### Entrada (`entrada.csv`)

Cada bloco começa com a linha do funcionário, seguida das linhas dos seus dependentes. Blocos são separados por **linha em branco**.

```
<Nome>;<CPF>;<DataNascimento YYYYMMDD>;<SalarioBruto>
<Nome>;<CPF>;<DataNascimento YYYYMMDD>;<Parentesco>

<Nome>;<CPF>;<DataNascimento YYYYMMDD>;<SalarioBruto>
<Nome>;<CPF>;<DataNascimento YYYYMMDD>;<Parentesco>
```

**Exemplo:**
```
Maria dos Santos;01234567890;20000228;3500.00
João;00011122234;20180301;SOBRINHO
Joana;09876543201;20171001;FILHO

Raquel;01256567450;20000628;9000.00
Bruno;00011122235;20190301;FILHO
```

### Saída (`saida.csv`)

```
<Nome>;<CPF>;<DescontoINSS>;<DescontoIR>;<SalarioLiquido>
```

**Exemplo:**
```
Raquel;01256567450;751.98;1346.71;6901.31
Maria dos Santos;01234567890;341.28;65.67;3093.05
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

| Ferramenta | Versão mínima | Download |
|-----------|--------------|---------|
| **Java JDK** | JDK 17 ou superior | https://www.oracle.com/java/ |
| **IntelliJ IDEA** | Community Edition | https://www.jetbrains.com/ |
| **PostgreSQL** | Qualquer versão recente | https://www.postgresql.org/ |
| **Driver JDBC PostgreSQL** | postgresql-42.7.x.jar | https://jdbc.postgresql.org/download/ |

---

### Passo 1 — Clonar o repositório

```bash
git clone https://github.com/Gabriel-Botz/TrabalhoFinalPOO-JAVA.git
cd TrabalhoFinalPOO-JAVA
```

---

### Passo 2 — Abrir no IntelliJ

- Abra o IntelliJ IDEA
- Clique em **File > Open**
- Navegue até a pasta do projeto clonado e confirme
- Aguarde o IntelliJ indexar o projeto

---

### Passo 3 — Adicionar o driver do PostgreSQL

- Baixe o arquivo `postgresql-42.7.x.jar` em https://jdbc.postgresql.org/download/
- No IntelliJ, vá em **File > Project Structure**
- Clique em **Libraries** no painel esquerdo
- Clique no **+** e selecione **Java**
- Navegue até o `.jar` baixado e confirme
- Clique em **OK** para salvar

> ⚠️ **Atenção:** Sem o driver `.jar` o programa vai lançar o erro `No suitable driver found for jdbc:postgresql`. Não pule esse passo!

---

### Passo 4 — Configurar o banco de dados

Abra o **pgAdmin** e execute os SQLs na ordem:

```sql
-- Cria o banco
CREATE DATABASE folha_pagamento;
```

Depois conecte no banco `folha_pagamento` e execute:

```sql
CREATE SCHEMA folha_pagamento;

CREATE TABLE folha_pagamento.funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    salario_bruto NUMERIC(10, 2) NOT NULL
);

CREATE TABLE folha_pagamento.dependente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    parentesco VARCHAR(10) NOT NULL,
    id_funcionario INT NOT NULL,
    FOREIGN KEY (id_funcionario) REFERENCES folha_pagamento.funcionario(id)
);

CREATE TABLE folha_pagamento.folha_pagamento (
    id SERIAL PRIMARY KEY,
    id_funcionario INT NOT NULL,
    data_pagamento DATE NOT NULL,
    desconto_inss NUMERIC(10, 2) NOT NULL,
    desconto_ir NUMERIC(10, 2) NOT NULL,
    salario_liquido NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (id_funcionario) REFERENCES folha_pagamento.funcionario(id)
);
```

---

### Passo 5 — Configurar a conexão no código

Abra o arquivo `src/dao/ConexaoDB.java` e ajuste as credenciais:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/folha_pagamento";
private static final String USUARIO = "postgres";   // seu usuário do PostgreSQL
private static final String SENHA   = "sua_senha";  // sua senha do PostgreSQL
private static final String SCHEMA  = "folha_pagamento";
```

> 💡 O usuário padrão do PostgreSQL é `postgres`. A senha é a que você definiu na instalação.

---

### Passo 6 — Preparar o arquivo de entrada

Crie uma pasta chamada `EntradaCSV` na raiz do projeto e dentro dela crie um arquivo `entrada.csv`:

```
Maria dos Santos;01234567890;20000228;3500,00
Joao;00011122234;20061201;SOBRINHO
Joana;09876543201;20071001;FILHO

Raquel;01256567450;19950628;9000,00
Bruno;00011122235;20090301;FILHO
```

**Regras do arquivo de entrada:**
- Separador de campos: `;`
- Data de nascimento: formato `YYYYMMDD` (ex: `20000228`)
- Salário bruto: vírgula como separador decimal (ex: `3500,00`)
- Parentesco: somente `FILHO`, `SOBRINHO` ou `OUTROS`
- CPF: somente números, exatamente 11 dígitos
- Blocos separados por **linha em branco**

---

### Passo 7 — Rodar o programa

- No IntelliJ, abra `src/main/Main.java`
- Clique no botão **Run** (▶) ou pressione **Shift + F10**
- Informe o caminho do arquivo de entrada quando solicitado:

```
Exemplo Windows: C:\Users\SeuNome\IdeaProjects\TrabalhoFinalPOO-JAVA\EntradaCSV\entrada.csv
```

- Informe o caminho do arquivo de saída:

```
Exemplo Windows: C:\Users\SeuNome\IdeaProjects\TrabalhoFinalPOO-JAVA\SaidaCSV\saida.csv
```

> 📌 A pasta `SaidaCSV` precisa existir antes de rodar. Crie-a manualmente na raiz do projeto.

---

### Resultados esperados

| Arquivo / Local | Conteúdo |
|----------------|---------|
| `saida_YYYY-MM-DD_HH-mm-ss.csv` | Nome, CPF, desconto INSS, desconto IR e salário líquido de cada funcionário |
| `saida_rejeitados_YYYY-MM-DD.csv` | Registros rejeitados com motivo (CPF duplicado, maior de idade, etc.) |
| **Banco de dados PostgreSQL** | Dados inseridos nas tabelas `funcionario`, `dependente` e `folha_pagamento` |

---

### Erros comuns e soluções

| Erro | Solução |
|------|---------|
| `No suitable driver found for jdbc:postgresql` | O driver `.jar` não foi adicionado. Volte ao Passo 3. |
| `O sistema não pode encontrar o arquivo especificado` | Caminho do arquivo incorreto. Verifique se ele existe. |
| `Acesso negado ao gerar arquivo de saída` | A pasta de saída não existe. Crie-a manualmente. |
| `CPF já existe no banco` | Limpe as tabelas antes de rodar novamente (veja abaixo). |
| `Connection refused / FATAL: password authentication failed` | Usuário ou senha incorretos no `ConexaoDB.java`. |

---

### Como limpar o banco de dados

Execute na ordem no pgAdmin:

```sql
DELETE FROM folha_pagamento.folha_pagamento;
DELETE FROM folha_pagamento.dependente;
DELETE FROM folha_pagamento.funcionario;
```

> ⚠️ Execute na ordem correta — `folha_pagamento` e `dependente` antes de `funcionario`, por causa das chaves estrangeiras.

---

### Como verificar os dados no banco

```sql
-- Funcionários inseridos
SELECT id, nome, cpf, salario_bruto FROM folha_pagamento.funcionario ORDER BY nome;

-- Dependentes vinculados a cada funcionário
SELECT f.nome AS funcionario, d.nome AS dependente, d.parentesco
FROM folha_pagamento.funcionario f
JOIN folha_pagamento.dependente d ON d.id_funcionario = f.id
ORDER BY f.nome;

-- Folha de pagamento com cálculos
SELECT f.nome, fp.desconto_inss, fp.desconto_ir, fp.salario_liquido, fp.data_pagamento
FROM folha_pagamento.funcionario f
JOIN folha_pagamento.folha_pagamento fp ON fp.id_funcionario = f.id
ORDER BY f.nome;
```

---

## 🔗 Fluxo do Sistema

```
┌──────────────────────────────────────────────────────┐
│                      Main.java                       │
└─────────────────────────┬────────────────────────────┘
                          │
               ┌──────────▼──────────┐
               │     CsvService      │  implements ICsvService
               └──────────┬──────────┘
                          │
          ┌───────────────┼───────────────┐
          │               │               │
   ┌──────▼──────┐ ┌──────▼──────┐ ┌─────▼──────┐
   │  CsvReader  │ │  Calcula    │ │  Gera CSV  │
   │(ICsvReader) │ │  INSS / IR  │ │  de saída  │
   └──────┬──────┘ └─────────────┘ └────────────┘
          │
     ┌────▼──────────────────────────┐
     │  Funcionario + Dependente     │
     │  (validações + model)         │
     └───────────────────────────────┘
```

---

## 🧩 Conceitos de POO Aplicados

| Conceito | Onde é usado |
|----------|-------------|
| **Classe Abstrata** | `Pessoa` |
| **Herança** | `Funcionario` e `Dependente` estendem `Pessoa` |
| **Interfaces** | `Calculavel`, `ICsvService`, `ICsvReader`, `Calculo` |
| **Encapsulamento** | Atributos `private` + getters/setters em todas as classes |
| **Enum** | `Parentesco` (FILHO, SOBRINHO, OUTROS) |
| **Exceções customizadas** | `DependenteException`, `CpfDuplicadoException` |
| **Coleções** | `HashSet<String>` para CPFs únicos, `ArrayList<Dependente>` |
| **Arquivos** | `BufferedReader` (leitura) e `BufferedWriter` (escrita) |
| **LocalDate / Period** | Datas em `Pessoa` e `FolhaPagamento`; cálculo de idade em `Dependente` |
| **Modificadores de acesso** | `private`, `public`, `static` aplicados corretamente |
| **Contador estático** | `contadorFuncionarios` em `Funcionario` |
| **Pacotes** | `model`, `service`, `util`, `exception` |

---

## ⚠️ Regras de Negócio

- **CPF único:** Nenhum funcionário ou dependente pode ter CPF repetido. Verificado via `HashSet` na leitura — lança `IllegalArgumentException` se duplicado.
- **Dependente menor de idade:** Todo dependente deve ter menos de 18 anos na data atual. Calculado com `Period.between(dataNascimento, LocalDate.now())` — lança `DependenteException` se inválido.
- **Salário bruto positivo:** Validado no construtor de `Funcionario` — lança `IllegalArgumentException` se ≤ 0.
- **Parentesco válido:** Aceita somente `FILHO`, `SOBRINHO` ou `OUTROS` — lança `IllegalArgumentException` para outros valores.

---

<div align="center">
  <sub>Trabalho Final — Programação Orientada a Objetos · 2025</sub>
</div>
