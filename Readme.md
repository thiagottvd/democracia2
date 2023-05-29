# Projecto de Referência de SpringBoot

## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636


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

2. Deve correr o comando `populate-db.sh` para popular a base de dados:
    - Existe uma bill com 9999 supporters que ao apoiar deve fechar-se e criar uma votação(poll) associada.
    - Existe uma bill e uma poll com datas expiradas que devem fechar dentro de um minuto (tempo definido no Schedule) depois de ligar a DB e inserir os dados
    - Temos um delegate (O user principal) que representa dois voters, o que signifca que ao votar numa poll, a poll vai ter 3 votos a favor ou contra dependedo da escolha. A verificação do resultado da poll pode ser vista na DB.
    - Os dados restantes são para testes normais. Os mencionados a cima são para a verificação casos extremos e do Schedule.
