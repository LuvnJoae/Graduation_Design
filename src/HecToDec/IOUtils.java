package HecToDec;

import java.io.*;
import java.util.ArrayList;

public class IOUtils {
    /**
     * 读取txt文件，并以Arraylist<String>形式返回
     * @param path txt路径
     * @return list（String）
     */
    public ArrayList<String> readTxt(String path) {
        try(
                FileReader reader = new FileReader(path);
                BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            ArrayList<String> list = new ArrayList<>();

            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                list.add(line);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 写入txt文件 (选择是否为追加) （重载）
     * @param path 存储路径
     * @param contents 写入String[]形式的数据
     * @param appendFlag 是否追加数据
     */
    public void writeTxt(String path, String[] contents, boolean appendFlag) {
        try (
                FileWriter writer = new FileWriter(path, appendFlag);
                BufferedWriter bw = new BufferedWriter(writer)
        ) {
            for (String content : contents) {
                bw.write(content + "\r\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入txt文件 (默认不追加) （重载）
     * @param path 存储路径
     * @param contents 写入String[]形式的数据
     */
    public void writeTxt(String path, String[] contents) {
        try (
                FileWriter writer = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(writer)
        ) {
            for (String content : contents) {
                bw.write(content + "\r\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入txt文件 (选择是否为追加) （重载）
     * @param path 存储路径
     * @param contents 写入StringBuilder[]形式的数据
     * @param appendFlag 是否追加数据
     */
    public void writeTxt(String path, StringBuilder[] contents, boolean appendFlag) {
        try (
                FileWriter writer = new FileWriter(path, appendFlag);
                BufferedWriter bw = new BufferedWriter(writer)
        ) {
            for (StringBuilder content : contents) {
                bw.write(content + "\r\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入txt文件 (默认不追加) （重载）
     * @param path 存储路径
     * @param contents 写入StringBuilder[]形式的数据
     */
    public void writeTxt(String path, StringBuilder[] contents) {
        try (
                FileWriter writer = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(writer)
        ) {
            for (StringBuilder content : contents) {
                bw.write(content + "\r\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJson(String path){
        //暂时不需要
    }

    public void writeJson(String path){
        //暂时不需要
    }
}
