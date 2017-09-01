Plume Querydsl
==============

This module helps integrate Querydsl SQL with Plume Framework and HikariCP.
It contains mainly:
- `TransactionManagerQuerydsl`: the main class of this module; it will
read the configuration, initialize the SQL connection pool
and provide helper methods to create Querydsl queries,
- The generic DAO `CrudDaoQuerydsl` for CRUD operations.

Querydsl queries can be created:
- **without a `Connection`**: that means that the query will be executed with a connection
from the SQL connection pool. The `Connection` object will be automaticely released in the pool
once the query is executed.
- **with a `Connection`**: that means that the query will be executed on this supplied connection.
This mode is almost always used when a transaction is needed:
```java
transactionManager.execute(connection -> {
  transactionManager.insert(QTable.table, connection).populate(bean).execute();
  transactionManager.delete(QTable.table, connection).where(predicate).execute();
  // the connection is set to autocommit=false and will be commited at the end of the lambda
});
```

To use the CRUD DAO, `CrudDaoQuerydsl`, entities must have a primary key on a column named `id` with the Java type `long`.
A [CRUD DAO example](https://github.com/Coreoz/Plume-demo/blob/master/plume-demo-full-guice-jersey/src/main/java/com/coreoz/demo/db/dao/CityDao.java)
is provided in the demo project.

For Querydsl documentation, have a look at [the official Querydsl documentation](https://github.com/querydsl/querydsl/tree/master/querydsl-sql).

Installation
------------
**Maven**:
```xml
<dependency>
  <groupId>com.coreoz</groupId>
  <artifactId>plume-db-querydsl</artifactId>
</dependency>
<dependency>
  <groupId>com.coreoz</groupId>
  <artifactId>plume-db-querydsl-codegen</artifactId>
  <optional>true</optional>
</dependency>
<!-- do not forget to also include the database driver -->
```

**Guice**: `install(new GuiceQuerydslModule());`

Configuration
-------------
The properties `db.dialect` can one value among : `MYSQL`, `H2`, `ORACLE`, `POSTGRE`, `SQL_SERVEUR`.

Please see the [Plume Database configuration](https://github.com/Coreoz/Plume/tree/master/plume-db#configuration)
for other values.

Connection to multiple databases
--------------------------------
See how it is done with [Plume Database](https://github.com/Coreoz/Plume/tree/master/plume-db#multiple-databases)

Code generation
---------------
To generate Querydsl entities, a good choice is to use this
[Querydsl code generator](https://github.com/Coreoz/Plume/tree/master/plume-db-querydsl-codegen)

