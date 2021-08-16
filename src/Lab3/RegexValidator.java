package Lab3;

/**
 * @author Tanvir Rahman
 * @ID 19101268
 * @Section 05
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexValidator {
    public static void main(String[] args) {
        List<String> regexList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

//        String path = "src/Lab/Lab3/input1.in";
        String path = "src/Lab/Lab3/input2.in";

        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            int regexCount = Integer.parseInt(in.readLine());
            for (int i = 0; i < regexCount; i++) {
                regexList.add(in.readLine());
            }

            int stringCount = Integer.parseInt(in.readLine());
            for (int j = 0; j < stringCount; j++) {
                stringList.add(in.readLine());
            }

            in.close();
            StringBuilder output = new StringBuilder();

            for (int i = 0; i < stringCount; i++) {
                int found = 0;
                for (int j = 0; j < regexCount; j++) {
                    if (Pattern.matches(regexList.get(j), stringList.get(i))) {
                        output.append("YES, ").append(j + 1).append("\n");
                        found++;
                    }
                }
                if (found == 0) output.append("NO, 0").append("\n");
            }

//            BufferedWriter out = new BufferedWriter(new FileWriter("output.text"));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
            out.write(output.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}