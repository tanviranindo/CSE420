package Lab4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodExtractor {

    public static void main(String[] args) {

        String path = "src/Lab/Lab4/input.txt";

        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            System.out.println("Methods: ");
            StringBuilder file = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                file.append(line).append("\n");
            }

            String commentRemove = "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/";
            String spaceRemove = "(?m)^[ \t]*\r?\n";
            String cleanFile = file.toString().replaceAll(commentRemove, "").replaceAll(spaceRemove, "");

//            System.out.println(cleanFile);
            regexMatcher(cleanFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void regexMatcher(String str) {
        String returnRE = "\\b([\\w.]+)\\b(?:|(?:<[?\\w\\[\\]\\s,.&]+>)|(?:<[^<]*<[?\\w\\[\\]\\s,.&]+>[^>]*>)|(?:<[^<]*<[^<]*<[?\\w\\[\\]\\s,.&]+>[^>]*>[^>]*>))((?:\\[\\])*)";
        String methodRE = "\\s+(?!main)[_a-zA-Z][_a-zA-Z0-9]*\\([^)]*\\)(?!;)";
        String regex = returnRE + methodRE;
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher1 = pattern1.matcher(str);
        while (matcher1.find()) {
            String temp1 = matcher1.group().trim();
            Pattern pattern2 = Pattern.compile(methodRE);
            Matcher matcher2 = pattern2.matcher(temp1);
            if (matcher2.find()) {
                String method = matcher2.group().trim();
                System.out.println(method + ", return type: " + temp1.split(methodRE)[0]);
            }
        }
    }
}
