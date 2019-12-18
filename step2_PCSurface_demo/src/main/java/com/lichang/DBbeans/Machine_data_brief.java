package com.lichang.DBbeans;

/**
 *  工件数据 简要表 -> machine_data_brief
 */
public class Machine_data_brief {
    private int id;
    private String production_name;
    private int production_num;
    private String result;

    @Override
    public String toString() {
        return "Machine_data_brief{" +
                "id=" + id +
                ", production_name='" + production_name + '\'' +
                ", production_num=" + production_num +
                ", result='" + result + '\'' +
                '}';
    }

    public String getProduction_name() {
        return production_name;
    }

    public void setProduction_name(String production_name) {
        this.production_name = production_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return production_num;
    }

    public void setNum(int production_num) {
        this.production_num = production_num;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Machine_data_brief() {
    }

    public Machine_data_brief(String production_name, int production_num, String result) {
        this.production_name = production_name;
        this.production_num = production_num;
        this.result = result;
    }
}
