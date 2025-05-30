# Sistema de Carrinho de Compras

## Descrição
Este é um sistema de gerenciamento de carrinho de compras desenvolvido em Java com JavaFX. O sistema permite gerenciar produtos, clientes e realizar compras através de um carrinho de compras.

## Funcionalidades

### Gerenciamento de Produtos
- Cadastro de produtos com:
  - Nome
  - Descrição
  - Preço
  - Estoque
  - Categoria (selecionável via ComboBox)
  - Código de barras
- Edição de produtos existentes
- Exclusão de produtos
- Visualização em tabela com edição direta da categoria

### Gerenciamento de Clientes
- Cadastro de clientes com:
  - Nome
  - CPF
  - Email
  - Telefone
  - Endereço
- Edição de clientes existentes
- Exclusão de clientes
- Visualização em tabela

### Carrinho de Compras
- Seleção de cliente para compra
- Adição de produtos ao carrinho
- Ajuste de quantidade de produtos
- Remoção de produtos do carrinho
- Visualização do total da compra
- Finalização da compra

## Tecnologias Utilizadas
- Java 17 ou superior
- JavaFX para interface gráfica
- PostgreSQL para banco de dados
- HikariCP para conexão com banco de dados

## Pré-requisitos
- JDK 17 ou superior
- PostgreSQL 12 ou superior
- Maven para gerenciamento de dependências

## Configuração do Banco de Dados
1. Crie um banco de dados PostgreSQL
2. Execute o script SQL fornecido em `src/main/resources/database.sql`
3. Configure as credenciais do banco de dados no arquivo de configuração

## Instalação
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/shopping-cart.git
```

2. Navegue até o diretório do projeto:
```bash
cd shopping-cart
```

3. Compile o projeto com Maven:
```bash
mvn clean install
```

4. Execute o programa:
```bash
mvn javafx:run
```

## Estrutura do Projeto
```
src/
├── main/
│   ├── java/
│   │   ├── application/    # Classes de aplicação
│   │   ├── dao/           # Data Access Objects
│   │   ├── model/         # Classes de modelo
│   │   └── view/          # Interfaces gráficas
│   └── resources/
│       └── database.sql   # Script do banco de dados
```

## Uso

### Menu Principal
O sistema inicia com um menu principal que oferece três opções:
1. Gerenciar Clientes
2. Gerenciar Produtos
3. Acessar Carrinho

### Gerenciamento de Produtos
- Para adicionar um produto:
  1. Preencha os campos do formulário
  2. Clique em "Adicionar"
- Para editar um produto:
  1. Selecione o produto na tabela
  2. Modifique os campos desejados
  3. Clique em "Editar"
- Para excluir um produto:
  1. Selecione o produto na tabela
  2. Clique em "Excluir"
  3. Confirme a exclusão

### Gerenciamento de Clientes
- Para adicionar um cliente:
  1. Preencha os campos do formulário
  2. Clique em "Adicionar"
- Para editar um cliente:
  1. Selecione o cliente na tabela
  2. Modifique os campos desejados
  3. Clique em "Editar"
- Para excluir um cliente:
  1. Selecione o cliente na tabela
  2. Clique em "Excluir"
  3. Confirme a exclusão

### Carrinho de Compras
1. Selecione um cliente
2. Na tela de produtos:
   - Selecione um produto
   - Defina a quantidade
   - Clique em "Adicionar ao Carrinho"
3. Para ver o carrinho:
   - Clique em "Ver Carrinho"
4. No carrinho:
   - Ajuste quantidades
   - Remova produtos
   - Finalize a compra

## Contribuição
1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Contato
Para sugestões, dúvidas ou problemas:
email: diogomarconato387@gmail.com
