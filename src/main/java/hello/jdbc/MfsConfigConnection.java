package hello.jdbc;

import hello.config.db.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MfsConfigConnection implements ConnectionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MfsConfigConnection.class);
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
