package HecToDec;

import java.util.ArrayList;

public class demo {
    public static void main(String[] args) {
        // 设置源文件路径 与 目标文件路径
        String sourcePath = "resource/Data/test.txt";
        String targetPath = "resource/Data/target.txt";
        // 加载读取与写入工具类
        IOUtils IOUtils = new IOUtils();

        // 读取源文件 并 转换
        ArrayList<String> sourceList = IOUtils.readTxt(sourcePath);  // 接收读取的txt内容（字符串形式）

        StringBuilder[] flag = new StringBuilder[sourceList.size()];
        StringBuilder[] Voltage = new StringBuilder[sourceList.size()];  // 存储电压的高位和低位（字符串十六进制）
        StringBuilder[] Current = new StringBuilder[sourceList.size()];  // 存储电流的高位和低位（字符串十六进制）
        StringBuilder[] Wire_Feed_Speed = new StringBuilder[sourceList.size()];  // 存储送丝速度的高位和低位（字符串十六进制）

        for (int i = 0; i < sourceList.size(); i++) {
            String[] strs = sourceList.get(i).split(" ");
            // 焊机启动标志位
            flag[i] = new StringBuilder(strs[4]);
            // 焊机已启动，存储相关信息
            if (flag[i].toString().equals("2B")){
                // 转存电压、电流、送丝速度
                Voltage[i] = new StringBuilder(strs[11]).append(strs[12]);
                Current[i] = new StringBuilder(strs[13]).append(strs[14]);
                Wire_Feed_Speed[i] = new StringBuilder(strs[19]).append(strs[20]);
            }
        }
        // 向目标文件中写入 表头 （序号、电压、电流、送丝速度）
        String[] strings = {"Seq\tVoltage\tCurrent\tWire_Feed_Speed"};
        IOUtils.writeTxt(targetPath, strings);

        StringBuilder[] rowStrings = new StringBuilder[sourceList.size()];
        int row = 0;
        for (int i = 0; i < sourceList.size(); i++) {
            if (flag[i].toString().equals("2B")){
                rowStrings[row].append(i).append(" ");
                rowStrings[row].append(Voltage[i]).append(" ");
                rowStrings[row].append(Current[i]).append(" ");
                rowStrings[row].append(Wire_Feed_Speed[i]).append(" ");
            }else {
                break;
            }
        }
        IOUtils.writeTxt(targetPath, rowStrings, true);






    }
}
