package com.lichang.DBbeans;

import java.sql.Timestamp;

/**
 * 故障数据表 -> machine_fault_data
 */
public class Machine_fault_data {
    private int id;
    private String production_name;
    private int production_num;
    private Timestamp time;
    private String fault_type;
    private int fault_maxNum;
    private String result;

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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getFault_type() {
        return fault_type;
    }

    public void setFault_type(String fault_type) {
        this.fault_type = fault_type;
    }

    public int getFault_maxNum() {
        return fault_maxNum;
    }

    public void setFault_maxNum(int fault_maxNum) {
        this.fault_maxNum = fault_maxNum;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Machine_fault_data() {
    }

    public Machine_fault_data(int production_num, Timestamp time, String fault_type, int fault_maxNum, String result) {
        this.production_num = production_num;
        this.time = time;
        this.fault_type = fault_type;
        this.fault_maxNum = fault_maxNum;
        this.result = result;
    }
}
