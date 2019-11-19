package com.lichang.DBbeans;

import java.sql.Timestamp;

public class Machine_fault_data {
    private int id;
    private int num;
    private Timestamp time;
    private String fault_type;
    private int fault_performance;

    public Machine_fault_data(int num, Timestamp time, String fault_type, int fault_performance, int result) {
        this.num = num;
        this.time = time;
        this.fault_type = fault_type;
        this.fault_performance = fault_performance;
        this.result = result;
    }

    public Machine_fault_data() {
    }

    @Override
    public String toString() {
        return "Machine_fault_data{" +
                "id=" + id +
                ", num=" + num +
                ", time=" + time +
                ", fault_type='" + fault_type + '\'' +
                ", fault_performance=" + fault_performance +
                ", result=" + result +
                '}';
    }

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

    public int getFault_performance() {
        return fault_performance;
    }

    public void setFault_performance(int fault_performance) {
        this.fault_performance = fault_performance;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    private int result;
}
