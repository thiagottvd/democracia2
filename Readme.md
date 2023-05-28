# Projecto de Referência de SpringBoot

## Equipa do Projeto
* Diogo Pinto   fc55179
* Ivo Estrela   fc51051
* Thiago Duarte fc53636

 ## TODO
* [IMPORTANTE] Implementar os casos de uso C à custa de Scheduled Tasks no Spring (https://www.baeldung.com/spring-scheduled-tasks).
* [IMPORTANTE] Escrever um relatório onde explica a arquitectura interna de cada um destes componentes.
* Desktop App.: [IMPORTANTE] Fazer mocks das APIs restantes; melhorar código (como por exemplo resolver duplicações de código); verificar se o padrão MVVM (ou MVC?) está sendo respeitado; verificar se todas as possíveis mensagens das APIs estão sendo tratadas; documentar código; testar.
* API Rest: Verificar se a semântica está de acordo com a semântica rest; documentar código (testes inclusos); testar.
* Web App. (SSR): Criar página de autenticação (parecida com a da aplicação desktop); verificar semântica URL; verificar se o mock do usuário (sessão) está feito de maneira suposta; documentar código; testar.
* Criar novos dados (no ficheiro init-data.sql) para inserir na aplicação ao correr populate-db.sh.
* Outras cenas?

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
