Plume Database
==============
The main goal of this module is to expose a [HikariCP](https://github.com/brettwooldridge/HikariCP)
`DataSource` through dependency injection.

Moreover this module contains an API to simplify the use of a transaction over a JDBC connection.
This API is available in the `TransactionManager` class.

Configuration
-------------
The configuration is based on the [Plume Config](https://github.com/Coreoz/Plume/tree/master/plume-conf) module
and more specificaly the `com.typesafe.config.Config` object.

By default, all keys right under `db.hikari` are directly passed to Hikari. For example:
```properties
db.hikari.dataSourceClassName="oracle.jdbc.pool.OracleDataSource"
db.hikari."dataSource.url"="jdbc:oracle:thin:@localhost:1521:orcl"
db.hikari."dataSource.user"=login
db.hikari."dataSource.password"=password
db.hikari.minimumIdle=1
db.hikari.maximumPoolSize=4
db.hikari.leakDetectionThreshold=30000
db.hikari.idleTimeout=20000
```
Do note that subkeys should be wrapped inside quotes, so `db.hikari.minimumIdle=1` is ok
but `db.hikari.dataSource.user=login` is not ok, instead it should be written
`db.hikari."dataSource.user"=login`.

Multiple databases
------------------
To connect to several databases, you have to extend `TransactionManager`
and configure which `Config` prefix should be read.

### Extending TransactionManager
For example, if you have 2 databases `stats` and `product`, you will need to create 2 classes:
```java
@Singleton
public class StatsTransactionManager extends TransactionManager {

	@Inject
	public StatsTransactionManager(Config config) {
		super(config, "db.stats");
	}

}
```
and
```java
@Singleton
public class ProductTransactionManager extends TransactionManager {

	@Inject
	public ProductTransactionManager(Config config) {
		super(config, "db.product");
	}

}
```

### Config Properties
This way you can configure your 2 connections with:
```properties
# stats database
db.stats.hikari.dataSourceClassName="oracle.jdbc.pool.OracleDataSource"
db.stats.hikari."dataSource.url"="jdbc:oracle:thin:@localhost:1521:orcl"
db.stats.hikari."dataSource.user"=login
db.stats.hikari."dataSource.password"=password

# product database
db.product.hikari.dataSourceClassName="com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
db.product.hikari."dataSource.url"="jdbc:mysql://localhost/product"
db.product.hikari."dataSource.user"=login
db.product.hikari."dataSource.password"=password
```

### Example
Now when you want to use the `stats` database, just reference the `StatsTransactionManager`:
```java
@Singleton
public class VisitDao {

	private final StatsTransactionManager transactionManager;

	@Inject
	public VisitDao(StatsTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public long countVisits(LocalDate day) {
		return transactionManager.executeAndReturn(connection -> {
			try(PreparedStatement statement
					= connection.prepareStatement("SELECT COUNT(*) FROM VISIT WHERE DAY = ?")) {
				// ...
			}
		});
	}

}
```

### With Plume Querydsl
If you are using [Plume Querydsl](https://github.com/Coreoz/Plume/tree/master/plume-db-querydsl/),
you should extend `TransactionManagerQuerydsl` instead of `TransactionManager`:
```java
@Singleton
public class StatsTransactionManager extends TransactionManagerQuerydsl {

	@Inject
	public StatsTransactionManager(Config config) {
		super(config, "db.stats");
	}

}
```

