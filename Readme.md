# Projecto de Referência de SpringBoot

## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636

 ## TODO
* DESKTOP APP: Integrar caso de uso 'Votar numa proposta'; melhorar código (como por exemplo resolver duplicações de código); verificar se o padrão MVVM está sendo respeitado; verificar se todas as possíveis mensagens das APIs estão sendo tratadas; documentar código; testar.

## Dependências

Este projecto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.

## Correr projeto

Deve correr `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres

## Correr testes

Deve correr `test.sh`.
