package guiByFX.model;

import geneticAlgirithmCore.BasicInfo;

public class DataModel {
    private String aboutModel;
    private String dateCreate;
    private BasicInfo basicInfo;

    public DataModel() {
        basicInfo = new BasicInfo();
    }

    public DataModel(String aboutModel, String dateCreate, BasicInfo basicInfo) {
        this.aboutModel = aboutModel;
        this.dateCreate = dateCreate;
        this.basicInfo = basicInfo;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public String getAboutModel() {
        return aboutModel;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setAboutModel(String aboutModel) {
        this.aboutModel = aboutModel;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }
}
