package HecToDec;

import java.util.ArrayList;
import java.util.Arrays;

public class demo {
    public static void main(String[] args) {
        // 设置源文件路径 与 目标文件路径
        String sourcePath = "resource/Data/test.txt";
        String targetDir = "resource/Data/target_hex/";
        String targetFileName = "1";
        String targetFileType = ".txt";
        // 加载读取与写入工具类
        IOUtils IOUtils = new IOUtils();
        // 读取源文件
        ArrayList<String> sourceList = IOUtils.readTxt(sourcePath);  // 接收读取的txt内容（字符串形式）
        // 从源文件中提取有用数据（对象初始化）
        StringBuilder[] flag = new StringBuilder[sourceList.size()];  // 存储焊机是否准备好的标志位（字符串十六进制）
        StringBuilder[] Voltage = new StringBuilder[sourceList.size()];  // 存储电压的高位和低位（字符串十六进制）
        StringBuilder[] Current = new StringBuilder[sourceList.size()];  // 存储电流的高位和低位（字符串十六进制）
        StringBuilder[] Wire_Feed_Speed = new StringBuilder[sourceList.size()];  // 存储送丝速度的高位和低位（字符串十六进制）
        // 从源文件中提取有用数据（数据转存）
        for (int i = 0; i < sourceList.size(); i++) {
            // 将每个字节分割
            String[] splitStrs = sourceList.get(i).split(" ");
            // 转存 焊机启动标志位
            flag[i] = new StringBuilder(splitStrs[4]);
            // 转存数据 判断焊机已启动，存储相关信息
            if (flag[i].toString().equals("2B")){
                // 转存电压、电流、送丝速度
                Voltage[i] = new StringBuilder(splitStrs[11]).append(splitStrs[12]);
                Current[i] = new StringBuilder(splitStrs[13]).append(splitStrs[14]);
                Wire_Feed_Speed[i] = new StringBuilder(splitStrs[19]).append(splitStrs[20]);
            }
        }
        // 创建 用于存储 有用数据 的字符串数组 （for循环是针对StringBuilder[]的元素初始化）
        StringBuilder[] rowStrings = new StringBuilder[sourceList.size()];
        for (int i = 0; i < rowStrings.length; i++) {
            rowStrings[i] = new StringBuilder();
        }

        int targetFileNum = 1;  // 文件名称序号
        int row = 0;  // 数据行数 ，每行包括 seq、电压、电流、送丝速度 四个数据
        int seq = 1;  // seq 序号
        int beforeRow = 0;  // 用于分割每个部件的数据。（一个部件的数据用一个txt存储）

        // 向第一个目标文件中写入 表头 （序号、电压、电流、送丝速度）
        String[] strings = {"Seq\tVoltage\tCurrent\tWire_Feed_Speed"};
        IOUtils.writeTxt(targetDir + targetFileName + targetFileType, strings);

        // 开始 根据 部件序号 写入文件 （当全部数据读完后，跳出循环）
        while (row < rowStrings.length){
            // 判断，如果焊机已准备，将四个数据写成一个字符串（StringBuilder）
            if (flag[row].toString().equals("2B")){
                rowStrings[row].append(seq).append("\t");  // 序号
                rowStrings[row].append(Voltage[row]).append("\t");  // 电压
                rowStrings[row].append(Current[row]).append("\t");  // 电流
                rowStrings[row].append(Wire_Feed_Speed[row]);  // 送丝速度
                row++;  // 每写完一行，行号增加
                seq++;  // seq增加
            }else if(flag[row].toString().equals("22")){  // 读到flag == ‘22’时，说明一个部件已转存加载完成，开始写入
                StringBuilder[] targetString = Arrays.copyOfRange(rowStrings, beforeRow, row);  // 分割单一部件
                targetFileName = targetFileNum + "";  // 将文件名称转为字符串形式

                // 向第二个及以后的目标文件中写入 表头 （序号、电压、电流、送丝速度）
                String[] headerStrings = {"Seq\tVoltage\tCurrent\tWire_Feed_Speed"};
                IOUtils.writeTxt(targetDir + targetFileName + targetFileType, strings);
                // 向目标文件中写入 四个数据
                IOUtils.writeTxt(targetDir + targetFileName + targetFileType, targetString, true);

                targetFileNum++; // 文件名称序号增加
                row++; // 行号增加
                seq = 1;  // 序号归一
                beforeRow = row;  // 记录上一部件的结束行号
            }
        }







    }
}
