package dataBaseCore;

import java.io.IOException;
import java.sql.*;

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
                "'array_g' BLOB, 'array_s' BLOB, 'array_h' BLOB, 'comment_result' TEXT);");


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

    public GenomeOfAgent getGenomeOfAgentByModelID(int modelID, int resultModelingID)
            throws SQLException, IOException, ClassNotFoundException {
        GenomeOfAgent agent = new GenomeOfAgent();
        ResultSet set = statement.executeQuery("SELECT * FROM model WHERE id_model ='"
                + resultModelingID + "';");
        if (set.next())
        {
            int basicInfoID = set.getInt("id_basic_information");
            BasicInfo info = getBasicInfoByID(basicInfoID);
            if (info == null) return null;
            agent.setBasicInfo(info);
        } else return null;
        set.close();

        set = statement.executeQuery("SELECT * FROM result_of_modeling WHERE id_of_result ='"
                + resultModelingID + "';");
        if (set.next()){
          int tmp_arr[][] = BinaryObjectConverter.byteToArr2DI(set.getBytes("array_g"));
          agent.setmG(tmp_arr);
          tmp_arr = BinaryObjectConverter.byteToArr2DI(set.getBytes("array_s"));
          agent.setmS(tmp_arr);
          tmp_arr = BinaryObjectConverter.byteToArr2DI(set.getBytes("array_h"));
          agent.setmH(tmp_arr);
        } else return null;
        set.close();
        return agent;
    }

    public void saveToDBParamOfApp(ParamOfApp paramOfApp, int transactionID, int numOfParam) throws SQLException, IOException {
        String sqlQuery = "INSERT INTO param_of_app (id_param_of_app, exchange_app, " +
                "exchange_storage, id_transaction, num_of_param) VALUES (null, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        connection.setAutoCommit(false);
        preparedStatement.setBytes(1, BinaryObjectConverter.arr1DIToByte(paramOfApp.exchangeBetweenApp));
        preparedStatement.setBytes(2, BinaryObjectConverter.arr1DIToByte(paramOfApp.exchangeBetweenStorage));
        preparedStatement.setInt(3, transactionID);
        preparedStatement.setInt(4, numOfParam);
        preparedStatement.execute();
        connection.commit();
        preparedStatement.close();
        connection.setAutoCommit(true);
    }

    public void saveToDBTransaction(Transaction transaction, int basicInfoID)
            throws SQLException, IOException {
        String sqlQuery = "INSERT INTO transaction_b (id_transaction, id_basic_information, used_apps, used_dims, " +
                "used_users, orders) VALUES (null, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        connection.setAutoCommit(false);
        preparedStatement.setInt(1, basicInfoID);
        preparedStatement.setBytes(2, BinaryObjectConverter.arr1DIToByte(transaction.a));
        preparedStatement.setBytes(3, BinaryObjectConverter.arr1DIToByte(transaction.d));
        preparedStatement.setBytes(4, BinaryObjectConverter.arr1DIToByte(transaction.u));
        preparedStatement.setBytes(5, BinaryObjectConverter.arr2DIToByte(transaction.w));
        preparedStatement.execute();
        connection.commit();
        preparedStatement.close();
        connection.setAutoCommit(true);

        ResultSet set = statement.executeQuery("SELECT id_transaction FROM transaction_b " +
                "WHERE id_transaction = last_insert_rowid();");
        if (!set.next()) throw new SQLException("It is very strange");

        int lastTrsID = set.getInt("id_transaction");
        for (int i = 0; i < transaction.paramOfApps.length; i++) {
            if (transaction.a[i] == 1) {
                saveToDBParamOfApp(transaction.paramOfApps[i], lastTrsID, i);
            }
        }
    }

    public void saveToDBModel(BasicInfo info, String about, String dateOfCreating)
            throws SQLException, IOException {
        String sqlQuery = "INSERT INTO basic_information (id_basic_information, apps, users, nodes, transactions, " +
                "dimentions, intensity_of_start) VALUES (null, ?, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        connection.setAutoCommit(false);
        preparedStatement.setInt(1, info.A);
        preparedStatement.setInt(2, info.U);
        preparedStatement.setInt(3, info.N);
        preparedStatement.setInt(4, info.E);
        preparedStatement.setInt(5, info.D);
        preparedStatement.setBytes(6, BinaryObjectConverter.arr2DDToByte(info.intensityOfRun));

        preparedStatement.execute();
        connection.commit();
        preparedStatement.close();
        connection.setAutoCommit(true);

        ResultSet set = statement.executeQuery("SELECT id_basic_information FROM " +
                "basic_information WHERE id_basic_information = last_insert_rowid();");
        if (!set.next()) throw new SQLException("if it not work, it no good");
        int lastID = set.getInt("id_basic_information");
        set.close();

        for (int i = 0; i < info.E; i++) {
            saveToDBTransaction(info.transactions[i], lastID);
        }

        sqlQuery = "INSERT INTO model (id_model, about, date_of_create, " +
                "id_basic_information) VALUES (null, ?, ?, ?);";
        preparedStatement = connection.prepareStatement(sqlQuery);
        connection.setAutoCommit(false);
        preparedStatement.setString(1, about);
        preparedStatement.setString(2, dateOfCreating);
        preparedStatement.setInt(3, lastID);

        preparedStatement.execute();
        connection.commit();
        preparedStatement.close();
        connection.setAutoCommit(true);
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
