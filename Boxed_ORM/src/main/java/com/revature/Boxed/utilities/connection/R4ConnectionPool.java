package com.revature.Boxed.utilities.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic functionality for a Connection Pool including getting and releasing a pooled connection
 * Not quite C3P0 efficient, but R4 functional
 *
 * @author Baeldung
 */
public class R4ConnectionPool implements ConnectionPool {
    //Attributes ----------------------------------------------------
    private final String url;
    private final String user;
    private final String password;
    private final String schema;
    private final List<Connection> connectionPool;

    private final List<Connection> usedConnections = new ArrayList<>();

    private final static int INITIAL_POOL_SIZE = 1;
    private final static int MAX_POOL_SIZE = 50;
    private final static int MAX_TIMEOUT = 50;


    //Static --------------------------------------------------------
    static{
        try {
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static R4ConnectionPool create(String url, String user, String password, String schema) throws SQLException{
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, user, password, schema));
        }
        return new R4ConnectionPool(url, user, password, schema, pool);
    }

    //Constructors --------------------------------------------------
    private R4ConnectionPool(String url, String user, String password, String schema, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.schema = schema;
        this.connectionPool = connectionPool;
    }

    //Overrides -----------------------------------------------------
    //TODO: allow for connection that does not require schema?
    @Override
    public Connection getConnection() throws SQLException{
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password, schema));
            }else{
                throw new RuntimeException("Maximum pool size reached, no available connections!");
            }
        }
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        if (!connection.isValid(MAX_TIMEOUT)){
            connection = createConnection(url, user, password, schema);
        }
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    //Getters and Setters -------------------------------------------
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }

    public int getSizeUsedPool() { return usedConnections.size();}

    //Other ---------------------------------------------------------
    private static Connection createConnection( String url, String user,
                                                String password, String schema) throws SQLException{
        Connection conn =  DriverManager.getConnection(url, user, password);
        conn.setSchema(schema);
        return conn;
    }
}
