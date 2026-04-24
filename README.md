# 💼 Sistema de Folha de Pagamento
### Projeto Final — Programação Orientada a Objetos

---

## 📋 Sobre o Projeto

Este projeto é uma aplicação Java orientada a objetos desenvolvida para automatizar o cálculo de salários líquidos. O fluxo de dados consiste na importação de um arquivo .csv contendo as informações dos colaboradores e de seus respectivos dependentes. A partir dessa entrada, o sistema processa todas as deduções legais (INSS e IRRF) aplicando as alíquotas oficiais vigentes e gera um arquivo .csv de saída com o fechamento completo e detalhado da folha de pagamento.
---

## 👥 Integrantes e Responsabilidades

| Integrante | Etapa | Responsabilidade |
|-----------|-------|-----------------|
| **Pedro Octavio** | Etapa 1 | Modelagem do domínio — classes `Pessoa` (abstrata), `Funcionario`, `Dependente`, `FolhaPagamento`, enum `Parentesco`, interface `Calculavel`, exceções `DependenteException` e `CpfDuplicadoException` |
| **Pedro Mello** | Etapa 2 + Shared | Leitura do CSV de entrada — `CsvReader`, `ICsvReader`; compartilha `ICsvService` e `CsvService` com Bruno Freitas |
| **Enzo Costa** | Etapa 3 | Lógica de cálculo — implementação de `calcularInss()` e `calcularIr()` progressivos dentro de `Funcionario` |
| **Bruno Freitas** | Etapa 4 + Shared | Geração do CSV de saída — método `gerarSaida()` em `CsvService`; compartilha `ICsvService` e `CsvService` com Pedro Mello |
| **Gabriel Botelho** | Etapa 5 | Banco de dados — criação do banco e schema no pgAdmin, Criação das tabelas Funcionario, FolhaPagamento, Dependentes, adicionado o driver JDBC do PostgreSQL, criação da classe ConexaoDB, criação da classe FuncionarioDAO |
| **Phelipe Damassio** | Etapa 5 | Banco de dados — criação da classe DependenteDAO e criação da classe FolhaPagamentoDAO |

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
- ✅ Persistência no banco de dados via JDBC

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

## 🚀 Como Compilar e Executar

### Pré-requisitos

- Java 11 ou superior
- Terminal ou IDE (IntelliJ IDEA recomendado)

### Compilação via terminal

```bash
# Dentro da pasta /src, compile todos os pacotes:
javac -d out model/*.java exception/*.java service/*.java util/*.java Main.java
```

### Execução

```bash
java -cp out Main
```

Ao iniciar, o programa solicitará:
```
Digite o nome do arquivo de entrada: entrada.csv
Digite o nome do arquivo de saída: saida.csv
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
