# Botiga Online

Aplicació de consola en Java que gestiona una botiga online amb JDBC: CRUD de
productes, clients i descomptes, creació de comandes amb transaccions i
descomptes amb `Savepoint`, i consultes amb `JOIN`.

## Requisits

- JDK 21.
- VS Code amb l'extensió **Extension Pack for Java**.
- No cal instal·lar cap servidor de base de dades: aquesta versió fa servir
  **H2 embeguda**.

## Com executar-ho

1. Obre la carpeta del projecte a VS Code.
2. Obre `src/main/java/ruben/net/App.java`.
3. Prem **Run**.

També pots executar `ruben.net.Online_shop`, que simplement crida `App`.

La primera execució crea automàticament la base de dades a `dades/shop.mv.db`.

## Base de dades

| Paràmetre | Valor |
|---|---|
| Motor | H2 2.1.214 (embegut) |
| URL | `jdbc:h2:./dades/shop` |
| Usuari | `sa` |
| Contrasenya | *(buida)* |
| Fitxer | `dades/shop.mv.db` |

L'esquema és a `src/main/resources/schema.sql` i `DatabaseInitializer` el executa
a cada arrencada. El fitxer fa `DROP TABLE` i torna a inserir les dades de prova,
de manera que **les dades es reinicien cada cop que executes l'aplicació**.

Dades inicials:

- Clients: Anna, Pere.
- Productes: Mouse, Keyboard, Monitor.
- Descomptes: 10 % al Mouse i 20 € al Monitor.

## Menú

```
===== BOTIGA ONLINE =====
1. Gestionar Productes
2. Gestionar Clients
3. Crear Comanda
4. Llistar Comandes
5. Gestionar Descomptes
0. Sortir
```

Cada opció de productes, clients i descomptes té un submenú amb:

```
1. Llistar
2. Afegir
3. Actualitzar
4. Eliminar
0. Tornar
```

### Crear una comanda

1. Entra a l'opció `3`.
2. Indica l'id del client (per exemple `1`).
3. Indica l'id del producte i la quantitat.
4. Repeteix per a més línies.
5. Escriu `0` com a id de producte per acabar.

La comanda es crea dins d'una transacció:

- Comprova l'estoc.
- Descompta l'estoc.
- Insereix la comanda i les línies.
- Calcula el total.
- Aplica els descomptes amb un `Savepoint`.
- Si l'estoc no és suficient, fa `rollback()`.

### Consultes

L'opció `4` permet:

- Veure totes les comandes amb el total final.
- Veure les comandes d'un client amb les seves línies i productes.

## Estructura

```
src/main/java/ruben/net/
├── App.java                    Menú principal
├── Online_shop.java            Llançador alternatiu
├── model/
│   ├── Client.java
│   ├── Product.java
│   ├── Order.java
│   ├── OrderLine.java
│   └── Discount.java
├── dao/
│   ├── ClientDAO.java          CRUD de clients
│   ├── ProductDAO.java         CRUD de productes
│   ├── DiscountDAO.java        CRUD de descomptes
│   ├── OrderDAO.java           Comandes amb transacció i Savepoint
│   └── ConsultesDAO.java       Consultes amb JOIN
└── util/
    ├── ConnectionDB.java       Connexió a la base de dades
    └── DatabaseInitializer.java Càrrega de schema.sql

src/main/resources/schema.sql   Esquema i dades de prova
```

## Validacions

- El nom d'un producte i d'un client són obligatoris.
- El preu ha de ser més gran que 0.
- L'estoc no pot ser negatiu, però **pot ser 0**.
- La quantitat d'una línia de comanda ha de ser més gran que 0.
- El tipus de descompte només pot ser `%` o `€`.
- Un descompte percentual no pot superar el 100 %.

## Canviar a MySQL

El codi dels DAO és portable. Per fer servir MySQL caldria:

1. Canviar la dependència de `pom.xml` per `mysql-connector-j`.
2. Canviar la URL d'`ConnectionDB` a `jdbc:mysql://localhost:3306/botiga`.
3. Adaptar el dialecte de `schema.sql` (per exemple, l'`ENUM` dels descomptes).
4. Tenir un servidor MySQL en marxa i la base de dades creada.

## Problemes habituals

- **`No suitable driver found`**: el driver no és al classpath. Comprova el
  `pom.xml` i recarrega el projecte Java de VS Code.
- **La base de dades està en ús**: tanca l'altra execució de l'aplicació.
- **Les dades desapareixen**: és el comportament actual, el schema es recrea a
  cada arrencada.
