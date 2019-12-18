package com.lichang.DBbeans;

/**
 * 全部工件数据表（机器数据表） -> machine_data_all
 */
public class Machine_data_all {
    private int id;
    private String production_name;
    private int production_num;
    private int seq;
    private double voltage;
    private double current;
    private double speed;
    private String result;

    public Machine_data_all() {
    }

    @Override
    public String toString() {
        return "Machine_data_all{" +
                "id=" + id +
                ", production_name='" + production_name + '\'' +
                ", production_num=" + production_num +
                ", seq=" + seq +
                ", voltage=" + voltage +
                ", current=" + current +
                ", speed=" + speed +
                ", result='" + result + '\'' +
                '}';
    }

    public String getProduction_name() {
        return production_name;
    }

    public void setProduction_name(String production_name) {
        this.production_name = production_name;
    }

    public Machine_data_all(String production_name, int production_num, int seq, double voltage, double current, double speed) {
        this.production_name = production_name;
        this.production_num = production_num;
        this.seq = seq;
        this.voltage = voltage;
        this.current = current;
        this.speed = speed;
    }

    public Machine_data_all(String production_name, int production_num, int seq, double voltage, double current, double speed, String result) {
        this.production_name = production_name;
        this.production_num = production_num;
        this.seq = seq;
        this.voltage = voltage;
        this.current = current;
        this.speed = speed;
        this.result = result;
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
