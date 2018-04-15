package geneticAlgirithmCore;

public class TesterForPackage {
    //test of GenomeOfAgent operation before calculate fitness function
    public static void main(String args[]) {
        BasicInfo basicInfo = new BasicInfo();
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
    }

// тест этого класса, всё работало правильно!
    /*
    public static void main(String args[]){
        Transaction tr1 = new Transaction();
        Transaction tr2 = new Transaction();
        Transaction tr3 = new Transaction();
        Transaction tr4 = new Transaction();
        Transaction tr5 = new Transaction();

        tr1.a = new int[]{1, 0, 1, 0, 0};
        tr2.a = new int[]{0, 1, 0, 0, 0};
        tr3.a = new int[]{1, 0, 0, 0, 1};
        tr4.a = new int[]{1, 0, 0, 0, 0};
        tr5.a = new int[]{0, 0, 0, 1, 0};

        Transaction ts [] = new Transaction[5];
        ts[0] = tr1;
        ts[1] = tr2;
        ts[2] = tr3;
        ts[3] = tr4;
        ts[4] = tr5;

        BasicInfo bi = new BasicInfo();
        bi.A = 5;
        bi.U = 3;
        bi.E = 5;

        bi.transactions = ts;
        bi.intensityOfRun = new double[][]{{0, 0, 1}, {1, 0, 1}, {3, 0, 0}, {0, 0, 1}, {1, 2, 3}};
        double arr[] = bi.calcLoadOfTransaction();
        double arr2[] = bi.calcLoadOFApp();
        for (double i: arr){
            System.out.println(i);
        }

        System.out.println();

        for (double i: arr2){
            System.out.println(i);
        }
    }
     */
}
