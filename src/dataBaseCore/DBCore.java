package dataBaseCore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import geneticAlgirithmCore.BasicInfo;
import geneticAlgirithmCore.GenomeOfAgent;
import geneticAlgirithmCore.ParamOfApp;
import geneticAlgirithmCore.Transaction;

public class DBCore {
    private Connection connection;
    private Statement statement;

    public DBCore(String filePath, String fileName) throws ClassNotFoundException, SQLException {
        String fileAddress;
        if (filePath == null) fileAddress = fileName;
        else fileAddress = filePath + fileName;

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + fileAddress);
        statement = connection.createStatement();
        createDB();
    }

    /**this method create tables of database, if it don't exist*/
    private void createDB() throws SQLException {
        //create table "basic_information"
        statement.execute("CREATE TABLE if not exists 'basic_information' " +
                "('id_basic_information' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'apps' INTEGER, 'users' INTEGER , 'nodes' INTEGER, " +
                "'transactions' INTEGER, 'dimentions' INTEGER, 'intensity_of_start' BLOB);");

        //create table "model"
        statement.execute("CREATE TABLE if not exists 'model' " +
                "(id_model INTEGER PRIMARY KEY AUTOINCREMENT, 'about' " +
                "TEXT, 'date_of_create' TEXT, 'id_basic_information' INTEGER);");

        //create table "result_of_modeling"
        statement.execute("CREATE TABLE if not exists 'result_of_modeling' " +
                "('id_of_result' INTEGER PRIMARY KEY AUTOINCREMENT, 'id_model' INTEGER, " +
                "'array_g' BLOB, 'array_s' BLOB, 'array_h' BLOB);");


        //create table "transaction"
        statement.execute("CREATE TABLE if not exists 'transaction_b' " +
                "('id_transaction' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_basic_information' INTEGER, 'used_apps' BLOB, 'used_dim' BLOB, " +
                "'used_users' BLOB, 'orders' BLOB);");

        //create table "param_of_app"
        statement.execute("CREATE TABLE if not exists 'param_of_app' " +
                "('id_param_of_app' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_transaction' INTEGER, 'num_of_param' INTEGER, " +
                "'exchange_app' BLOB, 'exchange_storage' BLOB);");
    }

    public void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

    public ParamOfApp [] getParamOfAppByTransactionID(int transactionId, int countOfApp)
            throws SQLException, IOException, ClassNotFoundException {
        ResultSet set = statement.executeQuery("SELECT * FROM param_of_app WHERE id_transaction = '"
                + transactionId +"';");
        ParamOfApp[] params = new ParamOfApp[countOfApp];
        while (set.next()) {
            ParamOfApp paramOfApp = new ParamOfApp();
            paramOfApp.exchangeBetweenApp =
                    BinaryObjectConverter.byteToArr1DI(set.getBytes("exchange_app"));
            paramOfApp.exchangeBetweenStorage =
                    BinaryObjectConverter.byteToArr1DI(set.getBytes("exchange_storage"));
            int inx = set.getInt("num_of_param");
            params[inx] = paramOfApp;
        }
        set.close();
        return params;
    }

    public Transaction [] getTransactionByBasicInfoID(int basicInfoID, int countOfTransaction, int countOfApp)
            throws SQLException, IOException, ClassNotFoundException {
        Transaction [] transactions = new Transaction[countOfTransaction];
        int inx = 0;
        ResultSet set = statement.executeQuery("SELECT * FROM transaction_b WHERE id_basic_information ='"
                + basicInfoID + "';");
        while (set.next()) {
            Transaction trs = new Transaction();
            trs.a = BinaryObjectConverter.byteToArr1DI(set.getBytes("used_apps"));
            trs.d = BinaryObjectConverter.byteToArr1DI(set.getBytes("used_dims"));
            trs.u = BinaryObjectConverter.byteToArr1DI(set.getBytes("used_users"));
            trs.w = BinaryObjectConverter.byteToArr2DI(set.getBytes("orders"));
            int trsID = set.getInt("id_transaction");
            trs.paramOfApps = getParamOfAppByTransactionID(trsID, countOfApp);
            transactions[inx] = trs;
            inx++;
        }
        set.close();
        return transactions;
    }

    public BasicInfo getBasicInfoByID(int basicInfoID)
            throws SQLException, IOException, ClassNotFoundException {
        BasicInfo basicInfo = new BasicInfo();
        ResultSet set = statement.executeQuery("SELECT * FROM basic_information " +
                "WHERE id_basic_informatin = '" +basicInfoID +"';");
        if (set.next()) {
            basicInfo.A = set.getInt("apps");
            basicInfo.U = set.getInt("users");
            basicInfo.N = set.getInt("nodes");
            basicInfo.E = set.getInt("transactions");
            basicInfo.D = set.getInt("dimentions");
            basicInfo.intensityOfRun =
                    BinaryObjectConverter.byteToArr2DD(set.getBytes("intensity_of_start"));
            basicInfo.transactions = getTransactionByBasicInfoID(basicInfoID, basicInfo.E, basicInfo.A);
        } else return null;
        set.close();
        return basicInfo;
    }

    public GenomeOfAgent getGenomeOfAgentByModelID(int modelID) {
        
    }

    public static void main(String args[]) {
        try {
            DBCore dbCore = new DBCore(null, "db.dat");
            dbCore.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
