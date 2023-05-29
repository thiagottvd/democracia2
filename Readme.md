# Projecto de Referência de SpringBoot

## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636

 ## TODO
* Desktop App.: Documentar código.
* API Rest: Verificar se a semântica está de acordo com a semântica rest; documentar código (testes e Poll); -> IVO
* Web App: (SSR): criar página de autenticação (parecida com a da aplicação desktop); documentar código; testar: -> IVO
* Criar novos dados (no ficheiro init-data.sql) para inserir na aplicação ao correr populate-db.sh. DAR FIX DO NUM_SUPPORTS BUG NO INSERT -> IVO

## Dependências

Este projecto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.

## Iniciar aplicação (backend)

Deve correr o comando `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres

## Correr testes

Deve correr o comando `test.sh`.

## Inserir dados na base de dados (postgres) para melhor testagem da aplicação

1. Deve correr o comando `run.sh` (caso ainda não o tenha feito) para iniciar a aplicação.
2. Deve correr o comando `populate-db.sh` para popular a base de dados.
