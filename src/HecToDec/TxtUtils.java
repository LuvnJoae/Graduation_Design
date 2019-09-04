package HecToDec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TxtUtils {
    public void readFile(String path) {
        try(
                FileReader reader = new FileReader(path);
                BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
