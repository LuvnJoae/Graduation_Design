import java.util.Arrays;

public class demo {
    public static void main(String[] args) {
        StringBuilder[] rowStrings = new StringBuilder[5];
        for (int i = 0; i < rowStrings.length; i++) {
            rowStrings[i] = new StringBuilder();
        }
        rowStrings[0].append("1111");
        rowStrings[1].append("2222");
        rowStrings[2].append("3333");
        rowStrings[3].append("4444");
        rowStrings[4].append("5555");

        StringBuilder[] rowStrings2 = Arrays.copyOfRange(rowStrings, 0, 3);
        for (StringBuilder stringBuilder : rowStrings2) {
            System.out.println(stringBuilder);
        }
    }
}
