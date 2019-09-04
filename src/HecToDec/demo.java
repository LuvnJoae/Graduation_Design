package HecToDec;

import java.util.ArrayList;

public class demo {
    public static void main(String[] args) {
        // 设置源文件路径 与 目标文件路径
        String sourcePath = "resource/Data/test.txt";
        String targetPath = "resource/Data/target.txt";
        // 加载读取与写入工具类
        IOUtils IOUtils = new IOUtils();
        // 向目标文件中写入 表头 （序号、电压、电流、送丝速度）
        String[] strings = {"Seq\tVoltage\tCurrent\tWire_Feed_Speed"};
        IOUtils.writeTxt(targetPath, strings);
        // 读取源文件 并 转换
        ArrayList<String> sourceList = IOUtils.readTxt(sourcePath);  // 接收读取的txt内容（字符串形式）
        StringBuilder Voltage;  // 存储电压的高位和低位（字符串十六进制）
        StringBuilder Current;  // 存储电流的高位和低位（字符串十六进制）
        StringBuilder Wire_Feed_Speed;  // 存储送丝速度的高位和低位（字符串十六进制）

        for (int i = 0; i < sourceList.size(); i++) {
            String[] strs = sourceList.get(i).split(" ");
            // 转存电压
//            Voltage = strs;

        }
//
//        for (Integer[] integers : TargetList) {
//            for (Integer integer : integers) {
//                System.out.print(integer + " ");
//            }
//            System.out.println();
//        }

    }
}
