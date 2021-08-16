package Lab.Lab1;

/**
 * @author Tanvir Rahman
 * @ID 19101268
 * @Section 05
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private static final List<String> keywords = new ArrayList<>();
    private static final List<String> identifiers = new ArrayList<>();
    private static final List<String> mathOperators = new ArrayList<>();
    private static final List<String> logicalOperators = new ArrayList<>();
    private static final List<String> numericalValues = new ArrayList<>();
    private static final List<String> others = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try {
            String filename = "src/Lab/Lab1/input.txt";
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("\\s");
                for (String s : arr) {
                    analyzer(s.trim());
                }
            }
            print();
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    private static void analyzer(String value) {
        switch (value) {
            case "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                    "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
                    "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
                    "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
                    "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
                    "struct", "typedef", "union", "auto", "extern", "register", "signed", "sizeof", "unsigned", "bool",
                    "true", "false":
                if (!keywords.contains(value))
                    keywords.add(value);
                break;
            case "+", "-", "/", "*", "=":
                if (!mathOperators.contains(value))
                    mathOperators.add(value);
                break;
            case "<", ">", "&&", "||", "<=", ">=", "!=":
                if (!logicalOperators.contains(value))
                    logicalOperators.add(value);
                break;
            default:
                String[] arr = value.split("(?=[,;])");
                for (String character : arr) {
                    if (!numericalValues.contains(character) && character.matches("\\d+(.)\\d+|\\d"))
                        numericalValues.add(character);
                    if (!identifiers.contains(character) && character.matches("[a-zA-Z][a-zA-Z0-9]*"))
                        identifiers.add(character);
                    if (!others.contains(character) && character.matches("\\p{Punct}"))
                        others.add(character);
                }
        }
    }

    private static void print() {
        printCSV("Keywords: ", keywords, ", ");
        printCSV("Identifiers: ", identifiers, ", ");
        printCSV("Math Operators: ", mathOperators, ", ");
        printCSV("Logical Operators: ", logicalOperators, ", ");
        printCSV("Numerical Values: ", numericalValues, ", ");
        printCSV("Others: ", others, " ");
    }

    private static void printCSV(String header, List<String> category, String separator) {
        System.out.print(header);
        for (int i = 0; i < category.size(); i++) {
            System.out.print(category.get(i) + (i + 1 == category.size() ? "" : separator));
        }
        System.out.println();
    }
}
