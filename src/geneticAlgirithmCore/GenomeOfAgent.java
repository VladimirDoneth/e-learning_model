package geneticAlgirithmCore;

public class GenomeOfAgent {
    private BasicInfo basicInfo;
    private double fitnessVal;
    private boolean isChanged = true;


    // start Of genome fields
    private int mG[][]; //A, N
    /** app place
     *    N1 N2 N3
     * A1
     * A2          */
    private int mH[][];  //U, N
    /** user place
     *   N1 N2 N3
     * U1
     * U2         */
    private int mS[][];  //D, N
    /** storage place
     *   N1 N2 N3
     * D1
     * D2
     * D3         */
    //end of genome fields

    //хреновый конструктор, никуда не годиться...
    public GenomeOfAgent(BasicInfo basicInfo, int mG[][], int mH[][], int mS[][]){
        this.basicInfo = basicInfo;
        this.mG = mG;
        this.mH = mH;
        this.mS = mS;
    }

    public GenomeOfAgent(BasicInfo basicInfo){
        this.basicInfo = basicInfo;
    }

    public GenomeOfAgent(){}

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public void setmG(int[][] mG) {
        isChanged = true;
        this.mG = mG;
    }

    public void setmH(int[][] mH) {
        isChanged = true;
        this.mH = mH;
    }

    public void setmS(int[][] mS) {
        isChanged = true;
        this.mS = mS;
    }

    public int[][] getmG() {
        return mG;
    }

    public int[][] getmS() {
        return mS;
    }

    public int[][] getmH() {
        return mH;
    }

    /**Получаем значение фитнес функции*/
    public double getFitnessVal(){
        if(isChanged){
            fitnessVal = fitness();
            isChanged = false;
        }
        return  fitnessVal;
    }

    public GenomeOfAgent mutate(int maxCountOfMutate){
        GenomeOfAgent agent = new GenomeOfAgent(basicInfo);
        int [][] mmG = mG.clone();
        int [][] mmS = mS.clone();
        int [][] mmH = mH.clone();

        int countOfMutation = (int)(Math.random() * maxCountOfMutate);

        for (int i = 0; i < countOfMutation; i++) {
            int inxA = (int)(Math.random() * basicInfo.A);
            for (int j = 0; j < basicInfo.N; j++)
                mmG[inxA][j] = 0;
            int inxN = (int)(Math.random() * basicInfo.N);
            mmG[inxA][inxN] = 1;
        }

        countOfMutation = (int)(Math.random() * maxCountOfMutate);

        for (int i = 0; i < countOfMutation; i++) {
            int inxD = (int)(Math.random() * basicInfo.D);
            for (int j = 0; j < basicInfo.N; j++)
                mmS[inxD][j] = 0;
            int inxN = (int)(Math.random() * basicInfo.N);
            mmS[inxD][inxN] = 1;
        }

        agent.setmG(mmG);
        agent.setmS(mmS);
        agent.setmH(mmH);

        return agent;
    }

    public GenomeOfAgent crossing(GenomeOfAgent father) {
        GenomeOfAgent agent = new GenomeOfAgent(basicInfo);
        int [][] mmG = mG.clone();
        int [][] mmS = mS.clone();
        int [][] mmH = mH.clone();

        int start = (int)(Math.random() * basicInfo.A);
        int end = (int)(Math.random() * basicInfo.A);
        if (end < start) {
            int tmp = start;
            start = end;
            end = tmp;
        }

        for (int i = start; i < end + 1; i++) {
            for (int j = 0; j < basicInfo.N; j++)
                mmG[i][j] = father.getmG()[i][j];
        }

        start = (int)(Math.random() * basicInfo.D);
        end = (int)(Math.random() * basicInfo.D);
        if (end < start) {
            int tmp = start;
            start = end;
            end = tmp;
        }

        for (int i = start; i < end + 1; i++) {
            for (int j = 0; j < basicInfo.N; j++)
                mmS[i][j] = father.getmS()[i][j];
        }
        agent.setmG(mmG);
        agent.setmS(mmS);
        agent.setmH(mmH);
        return agent;
    }

    /**
     * Рассмотрим функционирование транзакции j информационных узлах n и m
     * опрелим объемы данных передаваемых между приложениями*/
    public int[][][] workOfApp(){
        int res[][][] = new int[basicInfo.E][basicInfo.N][basicInfo.N];
        for (int j = 0; j < basicInfo.E; j++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    int tempSum = 0;
                    for (int xi = 0; xi < basicInfo.A; xi++) {
                        int tempSum2 = 0;
                        for (int eto = 0; eto < basicInfo.A; eto++) {
                            if (basicInfo.transactions[j].a[eto] != 0)
                                tempSum2 += basicInfo.transactions[j].a[eto] * mG[eto][m]
                                        * basicInfo.transactions[j].paramOfApps[eto].exchangeBetweenApp[xi];
                        }
                        tempSum += tempSum2 * basicInfo.transactions[j].a[xi] * mG[xi][n];
                    }
                    res[j][n][m] = tempSum;
                }
            }
        }
        return res;
    }

    /**
     * Рассмотрим функционирование транзакции j информациооных узлах n и m
     * определим объемы данных передаваемых из хранилища данных*/
    public int[][][] workOfStorage(){  //или я такой тупой или чего эта штука не работает???
        int res[][][] = new int[basicInfo.E][basicInfo.N][basicInfo.N];
        for (int j = 0; j < basicInfo.E; j++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    int tempSum = 0;
                    for (int xi = 0; xi < basicInfo.A; xi++) {
                        if (basicInfo.transactions[j].a[xi] != 0) {
                            int tempSum2 = basicInfo.transactions[j].a[xi] * mG[xi][n];
                            int tempSum3 = 0;
                            for (int eto = 0; eto < basicInfo.D; eto++) {
                                tempSum3 += basicInfo.transactions[j].d[eto] * mS[eto][m] * basicInfo.transactions[j].paramOfApps[xi].exchangeBetweenStorage[eto];
                            }
                            tempSum += tempSum2 * tempSum3;
                        }
                    }
                    res[j][n][m] = tempSum;
                }
            }
        }
        return res;
    }

    /**
     * Объемы обмена данных в сети определяются так:*/
    public int [][][] dataInNetwork(){
        int res[][][] = new int[basicInfo.E][basicInfo.N][basicInfo.N];
        int inApp[][][] = workOfApp();
        int inStorage[][][] = workOfStorage();
        for (int j = 0; j < basicInfo.E; j++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    res[j][n][m] = inApp[j][n][m] + inStorage[j][n][m];
                }
            }
        }
        return res;
    }

    /**
     * Постоим матрицу интенсивностей обмена между информационными узами сети
     * при выполении транзакции j*/
    public double [][][] loadOfNodesByTransactions() {
        double res[][][] = new double[basicInfo.E][basicInfo.N][basicInfo.N];
        double wj[] = basicInfo.calcLoadOfTransaction();
        int dataInNetwork[][][] = dataInNetwork();

        for (int j = 0; j < basicInfo.E; j++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    res[j][n][m] = wj[j] * dataInNetwork[j][n][m];
                }
            }
        }

        return res;
    }

    /**
     * Матрица нагрузки на информационные узлы,
     * так же это закгрузка каналов связи и сетевого оборудования*/
    public double [][] loadOfNetwork() {
        double res[][] = new double[basicInfo.N][basicInfo.N];
        double loadOfNodesByTransactions[][][] = loadOfNodesByTransactions();

        for (int n = 0; n < basicInfo.N; n++)
            for (int m = 0; m < basicInfo.N; m++)
                for (int j = 0; j < basicInfo.E; j++)
                    res[n][m] += loadOfNodesByTransactions[j][n][m];
        return res;
    }



    /**
     * Определяем среднюю нагрузку одного узла сети*/
    public double averageLoad() {
        double averageLoad = 0;
        double loadOfNetwork[][] = loadOfNetwork();

        for (int n = 0; n < basicInfo.N; n++) {
            for (int m = 0; m < basicInfo.N; m++){
                averageLoad += loadOfNetwork[n][m];
            }
        }

        return averageLoad  /(basicInfo.N * basicInfo.N); // скореее всего среднее значение необходимо делить на что-то ????
                            // вопрос на что именно???
    }

    /**
     * Показатель - сумма отклонений нагрузки от средней
     * Задача найти такое G при котором fitness --> min*/
    public double fitness(){
        double fitness = 0;
        double averageLoad = averageLoad();
        double loadOfNetwork[][] = loadOfNetwork();

        for (int n = 0; n < basicInfo.N; n++) {
            for (int m = 0; m < basicInfo.N; m++) {
                fitness += Math.abs(loadOfNetwork[n][m] - averageLoad);
            }
        }
        return fitness;
    }

    /**
     * Рассчитываем загрузку узлов сети
     * Интенсивность потока запросов на запуск приложения i, установленого на узле n*/
    public double[][] intensityOfStartApp(){
        double res[][] = new double[basicInfo.A][basicInfo.N];
        double q[] = basicInfo.calcLoadOFApp();
        for (int a = 0; a < basicInfo.A; a++) {
            for (int n = 0; n < basicInfo.N; n++){
                res[a][n] = q[a] * mG[a][n];
            }
        }
        return res;
    }

    /**
     * Загрузка узлов с элементами хранилища данных танзакция j,
     * узел n, форгмент d*/
    public double[][][] loadNodesStorage() {
        double res[][][] = new double[basicInfo.E][basicInfo.D][basicInfo.N];
        double intensityTransaction[] = basicInfo.calcLoadOfTransaction();
        for (int e = 0; e < basicInfo.E; e++) {
            for (int d = 0; d < basicInfo.D; d++) {
                for (int n = 0; n < basicInfo.N; n++) {
                    res[e][d][n] = intensityTransaction[e] * mS[d][n] * basicInfo.transactions[e].d[d];
                }
            }
        }
        return res;
    }


    /**К фрагменту d на узле n интенсивность запросов равна:*/
    public double[][] intensityStorage() {
        double res[][] = new double[basicInfo.D][basicInfo.N];
        double loadNodesStorage[][][] = loadNodesStorage();
        for (int d = 0; d < basicInfo.D; d++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int e = 0; e < basicInfo.E; e++) {
                    res[d][n] += loadNodesStorage[e][d][n];
                }
            }
        }
        return res;
    }
}
