package hello.jdbc;

import hello.config.db.DatabaseProperties;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getConnection(DatabaseProperties databaseProperties);
}
