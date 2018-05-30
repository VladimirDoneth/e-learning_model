package dataBaseCore;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import geneticAlgirithmCore.*;

public class DBCore {
    public static final String DB_NAME = "db.dat";
    private Connection connection;
    private Statement statement;

    /**This is constructor for class DBCore
     * filePath - it is full or part path to file of data base
     * fileName - it is only name of file*/
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

    /**This method need for closing session of work with database
     * this method must be call, because if it don't after big time
     * we can have errors working data base*/
    public void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

    /**
     * this method get all param`s of this transaction
     */
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

    /**
     * Method get all transaction fulled all parameters related with this basic_information
     */
    public Transaction [] getTransactionByBasicInfoID(int basicInfoID, int countOfTransaction, int countOfApp)
            throws SQLException, IOException, ClassNotFoundException {
        Transaction [] transactions = new Transaction[countOfTransaction];
        int []idxs = new int [countOfTransaction];

        int inx = 0;
        ResultSet set = statement.executeQuery("SELECT * FROM transaction_b WHERE id_basic_information ='"
                + basicInfoID + "';");
        while (set.next()) {
            Transaction trs = new Transaction();
            trs.a = BinaryObjectConverter.byteToArr1DI(set.getBytes("used_apps"));
            trs.d = BinaryObjectConverter.byteToArr1DI(set.getBytes("used_dim"));
            trs.u = BinaryObjectConverter.byteToArr1DI(set.getBytes("used_users"));
            trs.w = BinaryObjectConverter.byteToArr2DI(set.getBytes("orders"));
            int trsID = set.getInt("id_transaction");
            transactions[inx] = trs;
            idxs[inx] = trsID;
            inx++;
        }

        for (int i = 0; i < countOfTransaction; i++) {
            transactions[i].paramOfApps = getParamOfAppByTransactionID(idxs[i], countOfApp);
        }
        set.close();
        return transactions;
    }

    /**
     * Method get selected basic_information with fulled transactions*/
    public BasicInfo getBasicInfoByID(int basicInfoID)
            throws SQLException, IOException, ClassNotFoundException {
        BasicInfo basicInfo = new BasicInfo();
        ResultSet set = statement.executeQuery("SELECT * FROM basic_information " +
                "WHERE id_basic_information = '" +basicInfoID +"';");
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


    public ArrayList<Integer> getIdRelultsByModel(int idModel) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        String sqlQuery = "SELECT id_of_result FROM result_of_modeling WHERE id_model = '" + idModel + "';";
        ResultSet set = statement.executeQuery(sqlQuery);
        while (set.next()) {
            ids.add(set.getInt("id_of_result"));
        }
        return ids;
    }

    /**
     * Method return done resolution*/
    public GenomeOfAgent getGenomeOfAgentByModelID(int modelID, int resultModelingID)
            throws SQLException, IOException, ClassNotFoundException {
        GenomeOfAgent agent = new GenomeOfAgent();
        ResultSet set = statement.executeQuery("SELECT * FROM model WHERE id_model ='"
                + modelID + "';");
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

    /**
     * Method save to database param of app related with transaction and place in transaction this param*/
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

    /**
     * Method save to database transaction related with basic_info*/
    public void saveToDBTransaction(Transaction transaction, int basicInfoID)
            throws SQLException, IOException {
        String sqlQuery = "INSERT INTO transaction_b (id_transaction, id_basic_information, used_apps, used_dim, " +
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

    /**
     * Method save to database result_of_modeling*/
    public void saveToDBResult(int idModel, GenomeOfAgent agent, String comment) throws SQLException, IOException {
        String sqlQuery = "INSERT INTO result_of_modeling (id_result, id_model, array_g, array_s, array_h, " +
                "comment_result) VALUES (null, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        connection.setAutoCommit(false);
        preparedStatement.setInt(1, idModel);
        preparedStatement.setBytes(2, BinaryObjectConverter.arr2DIToByte(agent.getmG()));
        preparedStatement.setBytes(3, BinaryObjectConverter.arr2DIToByte(agent.getmS()));
        preparedStatement.setBytes(4, BinaryObjectConverter.arr2DIToByte(agent.getmH()));
        preparedStatement.setString(5, comment);
        preparedStatement.execute();
        connection.commit();
        preparedStatement.close();
        connection.setAutoCommit(true);
    }

    /**
     * Method delete from database record of result of modeling
     * by id of result*/
    public void deleteResultOfModelingByID(int idResult) throws SQLException {
        String sqlQuery = "DELETE FROM result_of_modeling WHERE " +
                "id_result = '" + idResult + "';";
        statement.execute(sqlQuery);
    }

    /**
     * Method delete from database record of result of modeling
     * by id of model*/
    public void deleteResultOfModelingByModelID(int idModel) throws SQLException {
        String sqlQuery = "DELETE FROM result_of_modeling WHERE " +
                "id_model = '" + idModel + "';";
        statement.execute(sqlQuery);
    }


    /**
     * Method delete from database model and related with it data from database*/
    public void deleteModelByID(int idModel) throws SQLException {
        //get id_basic_info.. for next step of deleting
        String sqlQuery = "SELECT id_basic_information, transactions FROM model WHERE id_model = '" + idModel + "';";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        int idBasicInfo = -1;
        int countOfTrs = -1;
        if (resultSet.next()) {
            idBasicInfo = resultSet.getInt("id_basic_information");
            countOfTrs = resultSet.getInt("transactions");
        } else throw new SQLException("Record in table model with id = " + idModel + " is not exist");

        //delete record in table model
        sqlQuery = "DELETE FROM model WHERE id_model = '" + idModel + "';";
        statement.execute(sqlQuery);

        //delete record in table basic_information
        sqlQuery = "DELETE FROM basic_information WHERE id_basic_infromation ='" + idBasicInfo + "';";
        statement.execute(sqlQuery);

        //get id_transaction for next_step of deleting
        int trsIDs[] = new int[countOfTrs];
        sqlQuery = "SELECT id_transaction FROM transaction_b WHERE id_basic_information = '" + idBasicInfo + "';";
        int inx = 0;
        resultSet = statement.executeQuery(sqlQuery);
        while (resultSet.next()) {
            trsIDs[inx] = resultSet.getInt("id_transaction");
            inx++;
        }

        //delete records in table transaction_b by id_basic_info..
        sqlQuery = "DELETE FROM transaction_b WHERE id_basic_information = '" + idBasicInfo + "';";
        statement.execute(sqlQuery);

        //delete records in table param_of_app by id_transaction
        for (int i = 0; i < countOfTrs; i++) {
            sqlQuery = "DELETE FROM param_of_app WHERE id_transaction = '" + trsIDs[i] +"';";
            statement.execute(sqlQuery);
        }
    }
    /**
     * Method save to data base model and related with it basic_information*/
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
        if (!set.next()) throw new SQLException("if it isn't work, it no good");
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

    public ArrayList<SimpleModel> getSimpleModels() throws SQLException {
        ArrayList<SimpleModel> models = new ArrayList<>();
        String sqlQuery = "SELECT * FROM 'model';";
        ResultSet set = statement.executeQuery(sqlQuery);
        while (set.next()) {
            SimpleModel simpleModel = new SimpleModel();
            simpleModel.idModel = set.getInt("id_model");
            simpleModel.idBasicInfo = set.getInt("id_basic_information");
            simpleModel.about = set.getString("about");
            simpleModel.dateOfCreate = set.getString("date_of_create");

            models.add(simpleModel);
        }

        return models;
    }

    public class SimpleModel {
        public String dateOfCreate;
        public String about;
        public int idModel;
        public int idBasicInfo;

        public String gerateStringForComboBox(){
            return  dateOfCreate + " | " + about;
        }
    }

    // test unit
    public static void main(String args[]) {
        try {
            DBCore dbCore = new DBCore(null, "db.dat");
            BasicInfo basicInfo = dbCore.getBasicInfoByID(1);

            Population population = new Population(basicInfo, 20,
                    10000, 5, 5);
            population.doEvolutionProcess(Population.BY_NUM_OF_GENERATION);
            GenomeOfAgent goa = population.getAgents().get(0);

            System.out.println("G:");
            for (int i = 0; i < basicInfo.A; i++){
                for (int j = 0; j < basicInfo.N; j++) System.out.print(goa.getmG()[i][j] + " ");
                System.out.println();
            }

            System.out.println("S:");
            for (int i = 0; i < basicInfo.D; i++){
                for (int j = 0; j < basicInfo.N; j++) System.out.print(goa.getmS()[i][j] + " ");
                System.out.println();
            }

            System.out.println("сумманрая интенсивность запуска транзакции j:");
            double res1d[] = basicInfo.calcLoadOfTransaction();
            for (double x: res1d) {
                System.out.print(x + " ");
            }
            System.out.println("\n\nсуммарная интенсивность запуска приложения k:");
            res1d = basicInfo.calcLoadOFApp();
            for (double x: res1d) {
                System.out.print(x + " ");
            }
            System.out.println("\n\nрассчитаем работу функционирования" +
                    "\nтранзакции j на информационных узлах n и m");
            int[][][] res3i = goa.workOfApp();
            for (int e = 0; e < basicInfo.E; e++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    for (int m = 0; m < basicInfo.N; m++) {
                        System.out.print(res3i[e][n][m] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
            System.out.println("\nопределим объемы данных передаваемых из хранилища данных:");
            res3i = goa.workOfStorage();
            for (int e = 0; e < basicInfo.E; e++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    for (int m = 0; m < basicInfo.N; m++) {
                        System.out.print(res3i[e][n][m] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            System.out.println("\nОбъемы данных в сети пределяются так:");
            res3i = goa.dataInNetwork();
            for (int e = 0; e < basicInfo.E; e++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    for (int m = 0; m < basicInfo.N; m++) {
                        System.out.print(res3i[e][n][m] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            System.out.println("\nтеперь можно посторить матрицу интенсивносетй обмена между" +
                    "\nинфо узлами сети при выполнении транзакции j");
            double res3d[][][] = goa.loadOfNodesByTransactions();
            for (int e = 0; e < basicInfo.E; e++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    for (int m = 0; m < basicInfo.N; m++) {
                        int ttt = (int) res3d[e][n][m];
                        System.out.print(ttt + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            System.out.println("\nсоответственно, матрица нагрузки на" +
                    "\nинформационные узлы определяется так");
            double res2d[][] = goa.loadOfNetwork();
            for (int e = 0; e < basicInfo.N; e++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    int ttt = (int) res2d[e][m];
                    System.out.print(ttt + " ");
                }
                System.out.println();
            }

            System.out.println("\nопределим среднюю нагрузку одного узла сети");
            double resd = goa.averageLoad();
            System.out.println(resd);

            System.out.println("\nсумма отклонений нагрузки от средней");
            resd = goa.fitness();
            System.out.println(resd);

            System.out.println("\n/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/\n");
            System.out.println("интенсивность потока запросов на запуск приложения i" +
                    "\nустановленного на узле n");
            res2d = goa.intensityOfStartApp();
            for (int a = 0; a < basicInfo.A; a++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    int ttt = (int) res2d[a][n];
                    System.out.print(ttt + " ");
                }
                System.out.println();
            }

            System.out.println("\nзагрузка узлов с элементами хранилища даннхы: транзакия j" +
                    "\nузел n, фрагмент d");
            res3d = goa.loadNodesStorage();
            for (int j = 0; j < basicInfo.E; j++) {
                for (int d = 0; d < basicInfo.D; d++){
                    for (int n = 0; n < basicInfo.N; n++) {
                        int ttt = (int) res3d[j][d][n];
                        System.out.print(ttt + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            System.out.println("интенсивность закпросов к хранилищу");
            res2d = goa.intensityStorage();
            for (int d = 0; d < basicInfo.D; d++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    int ttt = (int) res2d[d][n];
                    System.out.print(ttt + " ");
                }
                System.out.println();
            }
            /*BasicInfo basicInfo = new BasicInfo();
            basicInfo.U = 6;
            basicInfo.N = 15;
            basicInfo.A = 7;
            basicInfo.E = 3;
            basicInfo.D = 3;

            Transaction trs[] = new Transaction[3];
            trs[0] = new Transaction();
            trs[1] = new Transaction();
            trs[2] = new Transaction();
            basicInfo.transactions = trs;

            trs[0].a = new int[]{1, 1, 0, 0, 0, 0, 0};
            trs[0].d = new int[]{0, 1, 0};
            //trs[0].u = new int[]{1, 0, 0, 0, 0, 0};

            trs[1].a = new int[]{0, 0, 0, 1, 1, 0, 0};
            trs[1].d = new int[]{0, 0, 1};
            //trs[1].u = new int[]{0, 0, 0, 1, 0, 0};

            trs[2].a = new int[]{0, 0, 1, 0, 0, 1, 1};
            trs[2].d = new int[]{1, 0, 1};
            //trs[2].u = new int[]{0, 1, 0, 0, 1, 0};

            trs[0].paramOfApps = new ParamOfApp[7];
            trs[1].paramOfApps = new ParamOfApp[7];
            trs[2].paramOfApps = new ParamOfApp[7];

            trs[0].paramOfApps[0] = new ParamOfApp();
            trs[0].paramOfApps[0].exchangeBetweenStorage = new int[]{0, 100, 0};
            trs[0].paramOfApps[0].exchangeBetweenApp = new int[]{0, 10, 0, 0, 0, 0, 0};
            trs[0].paramOfApps[1] = new ParamOfApp();
            trs[0].paramOfApps[1].exchangeBetweenStorage = new int[]{0, 50, 0};
            trs[0].paramOfApps[1].exchangeBetweenApp = new int[]{5, 0, 0, 0, 0, 0, 0};

            trs[1].paramOfApps[3] = new ParamOfApp();
            trs[1].paramOfApps[3].exchangeBetweenStorage = new int[]{0, 0, 100};
            trs[1].paramOfApps[3].exchangeBetweenApp = new int[]{0, 0, 0, 0, 1, 0, 0};
            trs[1].paramOfApps[4] = new ParamOfApp();
            trs[1].paramOfApps[4].exchangeBetweenStorage = new int[]{0, 0, 75};
            trs[1].paramOfApps[4].exchangeBetweenApp = new int[]{0, 0, 0, 10, 0, 0, 0};

            trs[2].paramOfApps[2] = new ParamOfApp();
            trs[2].paramOfApps[2].exchangeBetweenStorage = new int[]{50, 0, 25};
            trs[2].paramOfApps[2].exchangeBetweenApp = new int[]{0, 0, 0, 0, 0, 0, 100};
            trs[2].paramOfApps[5] = new ParamOfApp();
            trs[2].paramOfApps[5].exchangeBetweenStorage = new int[]{0, 0, 0};
            trs[2].paramOfApps[5].exchangeBetweenApp = new int[]{0, 0, 50, 0, 0, 0, 0};
            trs[2].paramOfApps[6] = new ParamOfApp();
            trs[2].paramOfApps[6].exchangeBetweenStorage = new int[]{0, 0, 100};
            trs[2].paramOfApps[6].exchangeBetweenApp = new int[]{0, 0, 30, 0, 0, 0, 0};

            basicInfo.intensityOfRun = new double[][]{
                    {10, 0, 0, 0, 0, 0},
                    {0, 0, 0, 5, 0, 0},
                    {0, 10, 0, 0, 10, 0}};

            dbCore.saveToDBModel(basicInfo,"alalla", "03.05.2018");
            */
            dbCore.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
