Plume Querydsl
==============

This module helps integrate [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql)
with [Plume Database](https://github.com/Coreoz/Plume/tree/master/plume-db).
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
This mode is almost always used when a **transaction** is needed:
```java
transactionManager.execute(connection -> {
  transactionManager.insert(QTable.table, connection).populate(bean).execute();
  transactionManager.delete(QTable.table, connection).where(predicate).execute();
  // the connection is set to autocommit=false and will be commited at the end of the lambda
});
```

To use the CRUD DAO `CrudDaoQuerydsl`, entities must have a primary key on a column named `id`
mapped with the Java type `long`.
A [CRUD DAO example](https://github.com/Coreoz/Plume-demo/blob/master/plume-demo-full-guice-jersey/src/main/java/com/coreoz/demo/db/dao/CityDao.java)
is provided in the demo project.

For Querydsl documentation, see [the official Querydsl documentation](https://github.com/querydsl/querydsl/tree/master/querydsl-sql).

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

See the [Plume Database configuration](https://github.com/Coreoz/Plume/tree/master/plume-db#configuration)
for other configuration keys.

Connection to multiple databases
--------------------------------
See how it is done with [Plume Database](https://github.com/Coreoz/Plume/tree/master/plume-db#multiple-databases).

Code generation
---------------
To generate Querydsl entities, a good choice is to use this
[Querydsl code generator](https://github.com/Coreoz/Plume/tree/master/plume-db-querydsl-codegen).

Pagination
----------
**Overview**:

The `SqlPaginatedQuery` class provides a robust and flexible mechanism for paginating results in a QueryDSL query. It abstracts the pagination logic into two generic interfaces —`Slice` and `Page`— which represent paginated results in different ways.

- `Page<U>`: A `Page` contains a list of results, total count of items, total number of pages, and a flag to indicate if there are more pages available.
- `Slice<U>`: A `Slice` contains a list of results and a flag to indicate if there are more items to be fetched, without calculating the total number of items or pages.

This allows you to manage and paginate large datasets efficiently when working with QueryDSL.

**Key Features**:

- **Pagination Logic**: Handles offset-based pagination by calculating the number of records to skip (`offset`) and the number of records to fetch (`limit`) using the page number and page size.
- **Sorting Support**: Allows dynamic sorting of query results by providing an `Expression` and an `Order` (ascending/descending).
- **Efficient Slicing**: Fetches a "slice" of data without loading the entire dataset, useful when you only need to know if there are more results to load (e.g., in infinite scroll scenarios).
- **Full Page Information**: Provides detailed information about the paginated dataset, including the total count, total pages, and whether there are more results.

**Working with Pagination from a WebService**:

First, you need to create a translation between the API sort key and a table column.
This can be done like this: 

```java
public enum SortPath {

    // users
    USER_LIST_EMAIL("email", QUser.user.email),
    USER_LIST_FIRST_NAME("first_name", QUser.user.firstName),
    USER_LIST_LAST_NAME("last_name", QUser.user.lastName),
    USER_LIST_LAST_LOGIN_DATE("last_login_date", QUser.user.lastLogin),
    // more complex cases
    PRIORITY(
        "user_priority",
        new CaseBuilder()
            .when(QUser.user.priority.eq(StudyPriority.MEDIUM.name())).then(1)
            .when(QUser.user.priority.eq(StudyPriority.HIGH.name())).then(2)
            .when(QUser.user.priority.eq(StudyPriority.VERY_HIGH.name())).then(3)
            .otherwise(1)
    ),
    ;

    private final String sortKey;
    private final Expression<?> path;

    @Nullable
    public static SortPath fromSortKey(String sortKey) {
        return Arrays.stream(SortPath.values())
            .filter(entry -> entry.sortKey.equals(sortKey))
            .findFirst()
            .orElse(null);
    }
}
```

Then declare your WebService:

```java
@POST
@Path("/search")
@Operation(description = "Retrieves admin users")
@Consumes(MediaType.APPLICATION_JSON)
public Page<AdminUser> searchUsers(
    @QueryParam("page") Long page,
    @QueryParam("size") Long size,
    @QueryParam("sort") String sort,
    @QueryParam("sortDirection") Order sortDirection,
    UserSearchRequest userSearchRequest
) {
    // check the pagination that comes from the API call
    if (page < 1) {
        throw new WsException(WsError.REQUEST_INVALID, List.of("page"));
    }
    if (size < 1) {
        throw new WsException(WsError.REQUEST_INVALID, List.of("size"));
    }
    return usersDao.searchUsers(
        userSearchRequest,
        page,
        size,
        SortPath.fromSortKey(sort),
        sortDirection
    );
}
```

Then apply the pagination from the API call with `SqlPaginatedQuery` : 

```java
public Page<AdminUser> searchUsers(
    UserSearchRequest userSearchRequest,
    Long page,
    Long size,
    Expression<?> path,
    Order sortDirection
) {
    return SqlPaginatedQuery
        .fromQuery(
            this.transactionManagerQuerydsl.selectQuery()
                .select(QUser.user)
                .from(QUser.user)
                .where(
                    QUser.user.firstName.containsIgnoreCase(userSearchRequest.searchText())
                        .or(QUser.user.lastName.containsIgnoreCase(userSearchRequest.searchText()))
                        .or(QUser.user.email.containsIgnoreCase(userSearchRequest.searchText()))
                )
        )
        .withSort(path, sortDirection)
        .fetchPage(page, size);
}
```
