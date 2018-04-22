package dataBaseCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCore {
    private Connection connection;
    private Statement statement;

    public DBCore (String filePath, String fileName) throws ClassNotFoundException, SQLException {
        String fileAddress;
        if (filePath == null) fileAddress = fileName;
        else fileAddress = filePath + fileName;

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + fileAddress);
        statement = connection.createStatement();
    }

    /**this method create tables of database, if it don't exist*/
    private void createDB() throws SQLException {
        //create table "basic_information"
        statement.execute("CREATE TABLE if not exists 'basic_information' " +
                "('id_basic_information' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "('users' INTEGER , 'nodes' INTEGER, 'transactions' INTEGER," +
                "'dimentions' INTEGER, 'intensity_of_start' BLOB)");

        //create table "model"
        statement.execute("CREATE TABLE if not exists 'model' " +
                "(id_model INTEGER PRIMARY KEY AUTOINCREMENT, 'about' " +
                "TEXT, 'date_of_create' TEXT, 'id_basic_information')");

        //create table "result_of_modeling"
        statement.execute("CREATE TABLE if not exists 'result_of_modeling' " +
                "('id_result' INTEGER PRIMARY KEY AUTOINCREMENT, 'id_model' INTEGER, " +
                "'array_g' BLOB, 'array_s' BLOB, 'array_h' BLOB)");


        //create table "transaction"
        statement.execute("CREATE TABLE if not exitst 'transaction' " +
                "('id_transaction' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_basic_information' INTEGER, 'used_apps' BLOB, 'used_dim' BLOB, " +
                "'used_users' BLOB, 'orders' BLOB)");

        //create table "param_of_app"
        statement.execute("CREATE TABLE if not exists 'param_of_app' " +
                "('id_param_of_app' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_transaction' INTEGER, 'num_of_param' INTEGER, " +
                "'exchange_app' BLOB, 'exchange_app' BLOB)");
    }

}
