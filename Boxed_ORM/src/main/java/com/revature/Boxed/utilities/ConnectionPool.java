package com.revature.Boxed.utilities;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This interface defines the public API of a basic connection pool.
 * Loosely coupled design based on just one single interface
 * <p>
 * @author Baeldung
 */
public interface ConnectionPool {
    Connection getConnection() throws SQLException;
    boolean releaseConnection(Connection connection);
    String getUrl();
    String getUser();
    String getPassword();

}
