package HecToDec;

public class demoMain {
    public static void main(String[] args) {
        // 设置源文件路径 与 目标文件夹
        String sourcePath = "resource/Data/test.txt";
        String targetDirHex = "resource/Data/target_hex/";
        String targetDirDec = "resource/Data/target_dec/";
        String targetDirDecRangeTran = "resource/Data/target_dec_RangeTran/";

        // 开始读与写
        new Read_Write().process(sourcePath, targetDirDecRangeTran, 10, true);
    }
}
