package hello.jdbc;

import hello.config.db.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WmConfigConnection implements ConnectionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(WmConfigConnection.class);
    @Override
    public Connection getConnection(DatabaseProperties databaseProperties) {
        try {
            return DriverManager.getConnection(databaseProperties.getUrl(),databaseProperties.getUsername(),databaseProperties.getPassword());
        } catch (SQLException e) {
            LOGGER.error(this.getClass() + ": " + e.toString());
            return null;
        }
    }
}
