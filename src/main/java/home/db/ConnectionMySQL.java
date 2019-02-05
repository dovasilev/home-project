package home.db;

import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import home.env.ThreadEnv;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectionMySQL {
    private static final Logger logger = Logger.getLogger(Connection.class);
    

    @Step("Запуск SQL скрипта: {0}")
    public synchronized String executeSQL(String dbName, String sql, String column) {
        String response = null;
        try {
            Statement st = ThreadEnv.getByThread().getDb().getConnection(dbName).createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                response = rs.getString(column);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        return response;
    }

    @Step("Запуск SQL скрипта: {0}")
    public synchronized List<String> executeSQLList(String dbName, String sql, String column) {
        List<String> response = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = ThreadEnv.getByThread().getDb().getConnection(dbName).createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                response.add(rs.getString(column));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        finally {
            try{
                if (rs!= null)
                rs.close();
                if (st!=null)
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Step("Update SQL скрипт: {0}")
    public synchronized void updateSQL(String dbName, String sql)  {
        Statement st= null;
        try {
            st = ThreadEnv.getByThread().getDb().getConnection(dbName).createStatement();
            st.executeUpdate(sql);
            logger.info("Update Скрипт выполнен");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        finally {
            if (st!=null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Step("Проверка пользователя на настройки")
    public synchronized boolean checking(String dbName, String sql, String col, String value) {
        String bdVal = executeSQL(dbName, sql, col);
        if (bdVal == null)
            bdVal = "null";
        if (bdVal.equals(value)) {
            logger.info("Настройка соответствует предусловиям");
            return true;
        } else {
            logger.info("Настройка не соответствует предусловию");
            try {
                DatabaseMetaData dmd = ThreadEnv.getByThread().getDb().getConnection(dbName).getMetaData();
                String url = dmd.getURL();
                logger.info("Database: " + url.substring(url.lastIndexOf("/") + 1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
            //TO DO  скрипт по update
        }
    }


    @Step("Запуск SQL скрипта: {0}")
    public synchronized Map<String,String> executeSQLListByList(String dbName, String sql, List<String> column) {
        Map<String,String> response= new LinkedHashMap<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = ThreadEnv.getByThread().getDb().getConnection(dbName).createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                for (String col:column) {
                    response.put(col,rs.getString(col));
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        finally {
            try {
                if (rs!= null)
                    rs.close();
                if (st!=null)
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

}
