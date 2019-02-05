package home.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static final Logger logger = Logger.getLogger(Connection.class);

    private ConcurrentHashMap<String, Connection> dataSources = new ConcurrentHashMap<>();
    private String URL;

    public  Database(String url) {
        this.dataSources.clear();
        this.URL = url;
    }

    synchronized Connection getConnection(String dbName) throws SQLException {
        BasicDataSource dataSource;
        String connectionString = URL.replace("{databaseName}",dbName);
        if (dataSources.containsKey(connectionString)) {
            logger.info("Подключился к "+connectionString);
            return dataSources.get(connectionString);
        } else {
            dataSource = new BasicDataSource();
            logger.info("Пробую подключится к бд по URL: "+connectionString+"&autoReconnect=true&characterEncoding=UTF-8&useUnicode=true");
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl(connectionString+"&autoReconnect=true&characterEncoding=UTF-8&useUnicode=true");
            Connection con = dataSource.getConnection();
            dataSources.put(connectionString, con);
            return con;
        }
    }




}