package geneticAlgirithmCore;

public class BasicInfo {
    public int U; // users
    public int N; // nodes
    public int A; // apps
    public int E; // transactions
    public int D; // fragments of Storage

    public double intensityOfRun[][];
    /**
     * it is array of intensity start transaction by users;
     * U1 U2 U3
     * T1 ?  ?  ?
     * T2 ?  ?  ?
     * T3 ?  ?  ?
     * T4 ?  ?  ?
     */
    public Transaction transactions[];


    /**
     * суммарная интенсивность запросов на запуск
     * j-ой транзакции
     */
    public double[] calcLoadOfTransaction() {
        double res[] = new double[E];
        for (int i = 0; i < E; i++) {
            for (int j = 0; j < U; j++) {
                res[i] += intensityOfRun[i][j];
            }
        }
        return res;
    }

    /**
     * суммарная интенсивность запросов на запуск
     * k-ого приложения
     */
    public double[] calcLoadOFApp() {
        double res[] = new double[A];
        double loadTrans[] = calcLoadOfTransaction();
        for (int i = 0; i < A; i++) {
            for (int j = 0; j < E; j++) {
                res[i] += loadTrans[j] * transactions[j].a[i];
            }
        }
        return res;
    }
}