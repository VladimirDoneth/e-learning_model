package geneticAlgirithmCore;

public class BasicInfo {
    int U; // users
    int N; // nodes
    int A; // apps
    int E; // transactions
    int D; // fragments of Storage

    double intensityOfRun[][];
    /**
     * it is array of intensity start transaction by users;
     * U1 U2 U3
     * T1 ?  ?  ?
     * T2 ?  ?  ?
     * T3 ?  ?  ?
     * T4 ?  ?  ?
     */
    Transaction transactions[];


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