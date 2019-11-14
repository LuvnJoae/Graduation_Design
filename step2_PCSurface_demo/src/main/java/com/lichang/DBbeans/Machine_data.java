package com.lichang.DBbeans;

public class Machine_data {
    private int id;
    private int num;
    private int seq;
    private double voltage;
    private double current;
    private double speed;
    private int result;

    public Machine_data() {
    }

    @Override
    public String toString() {
        return "Machine_data{" +
                "id=" + id +
                ", num=" + num +
                ", seq=" + seq +
                ", voltage=" + voltage +
                ", current=" + current +
                ", speed=" + speed +
                ", result=" + result +
                '}';
    }

    public Machine_data(int num, int seq, double voltage, double current, double speed) {
        this.num = num;
        this.seq = seq;
        this.voltage = voltage;
        this.current = current;
        this.speed = speed;
    }

    public Machine_data(int num, int seq, double voltage, double current, double speed, int result) {
        this.num = num;
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
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
