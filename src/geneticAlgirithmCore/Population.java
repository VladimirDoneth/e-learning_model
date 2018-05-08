package geneticAlgirithmCore;

import java.util.ArrayList;

public class Population {
    public static final int BY_TARGET_VALUE = 1;
    public static final int BY_NUM_OF_GENERATION = 0;

    public static final int MAX_COUNT_OF_MUTATE = 3;

    private ArrayList<GenomeOfAgent> agents;
    private BasicInfo basicInfo;
    private int countOfAgents;
    private int targetValue;
    private int countForMutate, countForCrossing;

    public Population(BasicInfo basicInfo, int countOfAgents, int targetValue,
                      int countForCrossing, int countForMutate) {
        this.basicInfo = basicInfo;
        this.countOfAgents = countOfAgents;
        this.targetValue = targetValue;
        this.countForCrossing = countForCrossing;
        this.countForMutate = countForMutate;
    }

    public void doEvolutionProcess(int method){
        agents = generateRandomPopulation(countOfAgents);
        sortByFitness();

        if (method == BY_NUM_OF_GENERATION) {
            for (int i = 0;  i < targetValue; i++) {
                generateNewPopulation();
                sortByFitness();
            }
        }
        if (method == BY_TARGET_VALUE) {
            //
        }
    }

    private void generateNewPopulation() {
        for (int i = 0; i < countForMutate; i++) {
            int inx = (int) (Math.random() * countOfAgents);
            agents.add(agents.get(inx).mutate(MAX_COUNT_OF_MUTATE));
        }

        for (int i = 0; i < countForCrossing; i++) {
            int inx1 = (int) (Math.random() * countOfAgents);
            int inx2 = (int) (Math.random() * countOfAgents);
            agents.add(agents.get(inx1).crossing(agents.get(inx2)));
        }
        /*
        int otherCount = countOfAgents - (countForCrossing + countForMutate + 1);
        while (otherCount > 0) {
            int inx = (int) (Math.random() * countOfAgents);
            if (!agents.contains(agents.get(inx))){
                agents.add(agents.get(inx));
                otherCount--;
            }
        }
        */
        sortByFitness();
        ArrayList<GenomeOfAgent> newAgents = new ArrayList<>();
        for (int i = 0; i < countOfAgents; i++)
            newAgents.add(agents.get(i));
        agents = newAgents;
    }

    private void sortByFitness() {
        ArrayList<GenomeOfAgent> newAgents = new ArrayList<>();
        int i = getBestInx();
        while (i != -1) {
            newAgents.add(agents.get(i));
            agents.remove(i);
            i = getBestInx();
        }
        agents = newAgents;
    }

    private int getBestInx() {
        if (agents.size() > 0) {
            int res = 0;
            for (int i = 1; i < agents.size(); i++) {
               if (agents.get(res).getFitnessVal() >  agents.get(i).getFitnessVal())
                   res = i;
            }
            return res;
        } else return -1;
    }

    private ArrayList<GenomeOfAgent> generateRandomPopulation(int countOfAgents) {
        ArrayList<GenomeOfAgent> resultPopulation = new ArrayList<>();
        int i = 0;
        while (i < countOfAgents) {
            int [][]mG = generateArr(basicInfo.A, basicInfo.N);
            int [][]mS = generateArr(basicInfo.D, basicInfo.N);
            int [][]mH = generateArr(basicInfo.U, basicInfo.N);
            GenomeOfAgent agent = new GenomeOfAgent(basicInfo, mG, mH, mS);
            resultPopulation.add(agent);
            i++;
        }
        return resultPopulation;
    }

    private int[][] generateArr(int countQ, int countN) {
        int [][]result = new int[countQ][countN];
        for (int i = 0; i < countQ; i++) {
            int j = (int)(Math.random() * countN);
            result[i][j] = 1;
        }
        return result;
    }

    public ArrayList<GenomeOfAgent> getAgents() {
        return agents;
    }

    public static void main(String aaaa[]) {
        BasicInfo basicInfo = new BasicInfo();
        basicInfo.U = 6;
        basicInfo.N = 10;
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
        trs[0].paramOfApps[1].exchangeBetweenStorage = new int[]{0, 0, 0};
        trs[0].paramOfApps[1].exchangeBetweenApp = new int[]{5, 0, 0, 0, 0, 0, 0};

        trs[1].paramOfApps[3] = new ParamOfApp();
        trs[1].paramOfApps[3].exchangeBetweenStorage = new int[]{0, 0, 100};
        trs[1].paramOfApps[3].exchangeBetweenApp = new int[]{0, 0, 0, 0, 1, 0, 0};
        trs[1].paramOfApps[4] = new ParamOfApp();
        trs[1].paramOfApps[4].exchangeBetweenStorage = new int[]{0, 0, 0};
        trs[1].paramOfApps[4].exchangeBetweenApp = new int[]{0, 0, 0, 10, 0, 0, 0};

        trs[2].paramOfApps[2] = new ParamOfApp();
        trs[2].paramOfApps[2].exchangeBetweenStorage = new int[]{50, 0, 50};
        trs[2].paramOfApps[2].exchangeBetweenApp = new int[]{0, 0, 0, 0, 0, 0, 10};
        trs[2].paramOfApps[5] = new ParamOfApp();
        trs[2].paramOfApps[5].exchangeBetweenStorage = new int[]{0, 0, 0};
        trs[2].paramOfApps[5].exchangeBetweenApp = new int[]{0, 0, 20, 0, 0, 0, 0};
        trs[2].paramOfApps[6] = new ParamOfApp();
        trs[2].paramOfApps[6].exchangeBetweenStorage = new int[]{0, 0, 0};
        trs[2].paramOfApps[6].exchangeBetweenApp = new int[]{0, 0, 0, 0, 0, 0, 0};

        basicInfo.intensityOfRun = new double[][]{{10, 0, 0, 0, 0, 0},
                {0, 0, 0, 5, 0, 0},
                {0, 10, 0, 0, 10, 0}};
        Population population = new Population(basicInfo, 20, 30, 5, 5);

        population.doEvolutionProcess(Population.BY_NUM_OF_GENERATION);
    }

}
