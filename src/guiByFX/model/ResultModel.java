package guiByFX.model;

import geneticAlgirithmCore.GenomeOfAgent;

public class ResultModel {
    private int idModel; //??? I don`t now, I need this method or no
    private String commentResult;
    private GenomeOfAgent agent;

    public ResultModel(int idModel) {
        this.idModel = idModel;
    }

    public ResultModel() {

    }

    public ResultModel(int idModel, String commentResult, GenomeOfAgent agent) {
        this.idModel = idModel;
        this.commentResult = commentResult;
        this.agent = agent;
    }

    public void setAgent(GenomeOfAgent agent) {
        this.agent = agent;
    }

    public void setCommentResult(String commentResult) {
        this.commentResult = commentResult;
    }

    public void setIdModel(int idModel) {
        this.idModel = idModel;
    }

    public GenomeOfAgent getAgent() {
        return agent;
    }

    public int getIdModel() {
        return idModel;
    }

    public String getCommentResult() {
        return commentResult;
    }
}
