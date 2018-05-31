package guiByFX.model;

import geneticAlgirithmCore.GenomeOfAgent;

public class TableViewModel {
    public String name;
    public String result;

    public TableViewModel (GenomeOfAgent genomeOfAgent, String nameColum) {
        this.result = genomeOfAgent.getFitnessVal() + "";
        this.name = nameColum;
    }

    public String getNameColum() {
        return name;
    }

    public String getResultColum() {
        return result;
    }
}
