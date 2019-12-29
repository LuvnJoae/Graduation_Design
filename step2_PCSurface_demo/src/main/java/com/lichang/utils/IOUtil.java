package com.lichang.utils;

import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * 用于 普通文件的 IO 操作
 */
public class IOUtil {

    private static Logger log = LoggerUtil.getLogger(); // 加载日志管理类

    /**
     * 读文件
     * @param path txt路径
     * @return list（String)
     */
    //给字符流的 路径
    public static ArrayList<String> read(String path) {
        try(
                FileReader reader = new FileReader(path);
                BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            ArrayList<String> list = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                list.add(line);
            }

            return list;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }

    //给字节流
    public static ArrayList<String> read(InputStream is) {
        try(
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
        ) {
            String line;
            ArrayList<String> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                list.add(line);
            }

            return list;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }

    /**
     * 写入txt文件 (选择是否为追加) （重载）
     * @param path 存储路径
     * @param contents 写入String[]形式的数据
     * @param appendFlag 是否追加数据
     */
    public static void write(String path, String[] contents, boolean appendFlag) {
        try (
                FileWriter writer = new FileWriter(path, appendFlag);
                BufferedWriter bw = new BufferedWriter(writer)
        ) {
            for (String content : contents) {
                bw.write(content + "\r\n");
            }
            bw.flush();
        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * 写入txt文件 (默认不追加) （重载）
     * @param path 存储路径
     * @param contents 写入String[]形式的数据
     */
    public static void write(String path, String[] contents) {
        write(path, contents, false);
    }

    /**
     * 用来获取 配置文件在 release 和 debug 环境下的真正 path
      */
    public static String getPath(String debugPath, String releasePath) {
        //用来判断是否读取 release还是 debug 的配置文件
        InputStream is = IOUtil.class.getClassLoader().getResourceAsStream("conf/selectPath.properties");

        ArrayList<String> list = IOUtil.read(is);

        String flag = list.get(1);

        String realPath;
        if (flag.equals("release")) {
            //release版本： 路径为 exe文件同目录下。
            realPath = new File("").getAbsolutePath() + releasePath;
        }else {
            //debug 版本： 路径为 通过类加载器动态获取路径
            URL url =  LoggerUtil.class.getClassLoader().getResource(debugPath);
            if (url == null) {
                log.error("该路径不存在，请检查！");
                realPath = "";
            } else {
                realPath = url.getPath();
            }

        }

        return realPath;
    }


}
