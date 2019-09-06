import HecToDec.IOUtils;

import java.util.Arrays;

public class demo {
    public static void main(String[] args) {

        float a = (    Float.valueOf(Integer.valueOf("FF", 16))           /65535)*100;
        System.out.println(a);
    }
}
