package com.lichang.DBbeans;

/**
 *  工件数据 简要表 -> machine_data_brief
 */
public class Machine_data_brief {
    private int id;
    private int num;
    private String result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Machine_data_brief() {
    }

    public Machine_data_brief(int num, String result) {
        this.num = num;
        this.result = result;
    }
}
