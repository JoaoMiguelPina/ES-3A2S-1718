# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es18al_24-project.svg?token=J7gbx9uGUSTB6m91BeyY&branch=develop)](https://travis-ci.com/tecnico-softeng/es18al_24-project)[![codecov](https://codecov.io/gh/tecnico-softeng/es18al_24-project/branch/develop/graph/badge.svg?token=fVUMtVSYwj)](https://codecov.io/gh/tecnico-softeng/es18al_24-project)
To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |                  Email                    |   Name GitHUb  | Grupo |
| ---------- | ----------------------- | ----------------------------------------- | ---------------| ----- |
|   83530    |      Miguel Regouga     |     miguelregouga@tecnico.ulisboa.pt      |     regouga    |   1   |
|   83540    |      Pedro Lopes        |    pedro.daniel.l@tecnico.ulisboa.pt      | pedrodaniel10  |   1   |
|   83473    |     Hélio Domingos      |       helio_domingos@hotmail.com          |  heliodomingos |   1   |
|   83539    |     Pedro Caldeira      |        caldeira.a.pedro@gmail.com         | PedroACaldeira |   2   |
|   83521    |     Mariana Mendes      |        marianagprmendes@gmail.com         |   mgprmendes   |   2   |
|   85080    |       João Pina         |     joaomfpina@tecnico.ulisboa.pt         | JoaoMiguelPina |   2   |

- **Group 1:**
- **Group 2:**

### Infrastructure

This project includes the persistent layer, as offered by the FénixFramework.
This part of the project requires to create databases in mysql as defined in `resources/fenix-framework.properties` of each module.

See the lab about the FénixFramework for further details.

#### Docker (Alternative to installing Mysql in your machine)

To use a containerized version of mysql, follow these stesp:

```
docker-compose -f local.dev.yml up -d
docker exec -it mysql sh
```

Once logged into the container, enter the mysql interactive console

```
mysql --password
```

And create the 7 databases for the project as specified in
the `resources/fenix-framework.properties`.
