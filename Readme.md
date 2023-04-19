# Projecto de Referência de SpringBoot

## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636

## O que já foi feito

 * Use Case D: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case E: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case F: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case G: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case H: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case I: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case J: Implementado; Totalmente documentado; Testes Implementados.
 * Use Case K: Implementado; Totalmente documentado; POSSÍVEIS BUGS; testes parciais.

 ## TODO

 * **[CRÍTICO]** Implementar testes (completos) para o use case: K.
 * Verificar se o mapeamento está o melhor possível.
 * **[CRÍTICO]** Fazer um script test.sh que corra os testes dentro de um container docker e testa-los na base de dados Postgres.
 * **[CRÍTICO]** Entregar projeto.

## Dependências

Este projecto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.

## Correr projeto

Deve correr `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres

## Correr testes

Deve correr `test.sh`.
