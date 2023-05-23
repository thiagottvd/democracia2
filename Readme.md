# Projecto de Referência de SpringBoot

## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636

## O que já foi feito

 ## TODO
* WEB APP (SSR): Integrar caso de uso 'Votar numa proposta'; melhorar UI/Service do caso de uso 'Escolher Delegado'; verificar semântica URL; verificar se o mock do usuário (sessão) está feito de maneira suposta; testar.

## Dependências

Este projecto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.

## Correr projeto

Deve correr `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres

## Correr testes

Deve correr `test.sh`.
