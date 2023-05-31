# Projecto da Cadeira Construção de Sistemas de Software: Democracia 2.0


## Contexto

No enunciado do projeto é abordada a questão dos altos índices de abstenção e insatisfação com a representação política, propondo um modelo alternativo denominado “democracia direta com representação facultativa”. Em seguida, é dito que a nossa equipa foi encarregada para desenvolver uma plataforma de teste para este novo modelo.

Sobre o novo modelo:
Neste novo modelo proposto, os eleitores podem votar diretamente em todas as propostas de lei que vão à votação no parlamento. Como neste modelo perde-se o conceito de deputados (o que traz algumas desvantagens), de forma a manter a hipótese de delegar votos, é possível uma pessoa voluntariar-se como delegado (semelhante ao papel do atual deputado, mas sem eleições). Os delegados, diferentemente dos eleitores, possuem votos públicos. Também é possível que um cidadão delegue os seus votos a um ou mais delegados, seja na totalidade ou por tema (saúde, educação, etc.).

Mais informações sobre o contexto do projeto encontradas nos enunciados do projeto (enunciado_fase1.pdf e enunciado_fase2.pdf).


# Funcionalidades da Aplicação

* A. Autenticação com o Cartão de Cidadão (https://github.com/amagovpt/doc-AUTENTICACAO).
* B. Apresentar-se como delegado. Um cidadão que seja delegado terá votos públicos em vez de votos secretos. Passa também a poder ser escolhido como delegado.
* C. Listar as votações passadas.
* D. Listar as votações em curso. Este caso de uso permite obter uma listagem das propostas de lei em votação neste momento.
* E. Apresentar um projeto de lei. Neste caso de uso, um delegado poderá propor um projeto de lei. Um projeto de lei é constituído por um título, um texto descritivo, um anexo PDF com o conteúdo principal do projeto de lei, uma data e hora de validade (máximo de um ano), um tema associado e o delegado proponente.
* F. Fechar projetos de lei expirados. Todos os projetos de lei cuja data limite já tenha decorrido deverão ser fechados, sendo que não podem receber mais nenhuma assinatura.
* G. Consultar projetos de lei. Deve ser possível listar e consultar os projetos de lei não expirados.
* H. Apoiar projetos de lei. Cada projeto de lei pode ser apoiado por cidadãos (no limite de 1 apoio por cidadão). Quando um projeto de lei tiver pelo menos 10.000 apoios, é criada uma votação para esse projeto de lei imediatamente, com uma data de fecho igual à data de expiração do projeto de lei, com um limite mínimo de 15 dias e um limite máximo de 2 meses. Ao abrir a votação, é lançado automaticamente o voto do delegado proponente, como favorável.
* I. Escolher delegado. Um cidadão pode escolher vários delegados, mas apenas um para cada tema. Ou seja, pode ter um delegado para o tema de Saúde, um para Educação e um para outros temas. Quando fecha uma votação onde o cidadão não votou, é feito um voto automático com base no delegado do tema mais específico da proposta de lei.
* J. Votar numa proposta. Um cidadão deve pedir uma listagem das votações e escolher a que lhe interessa. Deverá ver qual o voto por omissão caso não o faça explicitamente (isto é, o voto do seu delegado para o tópico), caso este esteja disponível. Caso não concorde, o cidadão poderá lançar o seu voto (favorável ou desfavorável, não existem votos em branco nem nulos) numa proposta concreta. Deve ser verificado se o cidadão já votou nesta proposta em concreto, mas não deve ser registado em que opção foi o voto (dica: poderá ter de dividir o voto em duas partes, a do cidadão e a do conteúdo).
Se o cidadão for também um delegado, então deverá ser registado um voto público, que qualquer um poderá confirmar.
* K. Fechar uma votação. Assim que termina o prazo de uma votação, são atribuídos os votos dos delegados para cada cidadão que não tenha votado explicitamente. Depois são contados os votos, e se mais de metade dos votos forem favoráveis, então a proposta é fechada como aprovada. Caso contrário é fechada como rejeitada.

Para o projeto, tivemos que implementar os casos de uso D, E, F, G, H, I, J, e K. Os demais, A, B e C, não foram necessários de implementar.
Além disso, foi necessário desenvolver:
* Uma aplicação WEB com Server-Side Rendering para os casos de uso: D, E, G, H, I, e J;
* Uma API REST para os casos de uso: D, G, H, e J; 
* Uma aplicação desktop usando JavaFX ou outra framework nativa, que permita executar os casos de uso: D, G, H, J, e fazer Mocks dos restantes casos de uso;
* Implementar os casos de uso: F, e K a custa dos Scheduled Tasks no Spring.


# Informações Sobre a Estrutura do Projeto

O estilo de arquitetura do projeto foi baseado no estilo arquitetural em camadas.

1. Camada de Dados: A camada de dados foi implementada seguindo o padrão Data Mapper feito a partir do ORM (Object Relational Mapping).
2. Camada de Negócio: A camada de negócio foi implementada seguindo o padrão Domain Model.
3. Camada de Apresentação: A camada de apresentação foi implementada seguindo o padrão Model-View-Controller (MVC), tanto para a aplicação WEB, quanto para a aplicação Desktop.

Foi utilizada a base de dados PostgresSQL para a aplicação, e H2 para os testes.

# Mais informações

* Para o projeto, tivemos uma série de requisitos não funcionais, como por exemplo: "Toda a informação deverá ser armazenada numa base de dados relacional.", "A plataforma deverá ser implementada em Spring Boot", "A camada de dados deverá usar JPA e Spring Data.", "A camada de negócios deverá usar o Domain Model.", "Deverá ser exposta uma API REST.", "O repositório deverá aceitar apenas código aceite pelo pre-commit.", "O projecto deverá correr dentro de um docker.", entre outros.

* Foram feitos testes para cobertura dos casos de uso e casos extraordinários; testes para os endpoints da API REST.

* Foram feitos relatórios que incluiam desenhos como "Modelo de Domínio", "Diagrama de Classes", "SSDs para alguns casos de uso", e com outras informações técnicas.


## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636


## Dependências

Este projeto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.


## Iniciar aplicação (backend)

Deve correr o comando `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres.

Após a inicialização, ao navegar no endereço http://localhost:8080, você será redirecionado à página de autenticação da aplicação.


## Correr testes

Deve correr o comando `test.sh`.


## Correr cliente Desktop

Deve correr o comando `client.sh`.


## Inserir dados na base de dados (postgres) para melhor testagem da aplicação

1. Deve correr o comando `run.sh` (caso ainda não o tenha feito) para iniciar a aplicação.

2. Deve correr o comando `populate-db.sh` para popular a base de dados:
