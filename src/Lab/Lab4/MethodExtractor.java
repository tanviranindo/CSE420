package Lab.Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class MethodExtractor {
    //Fix 142
    //LINE 126 + Single line multiple methods, indentation problems (int \n [])

    /*
     * Cases that has been handled not mentioned in the assignment
     * 1. Removed the commented part of code
     * 2. Handled different return type such as : int [], Map< String, Integer >
     * 3. Constructor is also added. If found then type: constructor (Not fully done)
     * 4. Empty abstract method can be found.
     * 5. Duplicate method call handled, such as empty abstract method created and then using it
     */

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/Lab/Lab4/input.txt"));
        StringBuilder file = new StringBuilder();
        while (input.hasNextLine()) {
            file.append(input.nextLine().trim()); //Removes whitespace from both sides
            file.append("\n");
        }


        String newFile = indentationRE(file.toString()); //
//        String newFile = indentationWORE(file.toString());

        /* System.out.println(newFile); */
        input = new Scanner(newFile);
        Scanner input2 = new Scanner(newFile);
        StringBuilder finalResult = new StringBuilder();

        while (input.hasNextLine()) {
            Stack<Character> add = new Stack<>();
            StringBuilder method = new StringBuilder();
            StringBuilder returnType = new StringBuilder();
            String line = input.nextLine();
            if (input2.hasNext()) input2.nextLine(); //Needed for new line curly brace cases
            if (line.isEmpty()) continue;
            boolean isValid = false;
            if (input2.hasNext()) isValid = foundCurly(line, input2.next());

            for (char c : line.toCharArray()) {
                add.push(c);
            }

            while (!add.isEmpty()) {
                if (add.peek().equals(';')) { //Removing ;
                    add.pop(); // break;
                } else if (add.peek().equals(')') && isValid) { //
                    method.append(add.pop()); //Closing parenthesis
                    while (!add.isEmpty() && !add.peek().equals('(')) {
                        method.append(add.pop()); //Arguments
                    }
                    arguments(add, method);
                    if (!add.isEmpty() && add.peek().equals('.')) return;
                    if (!add.isEmpty() && add.peek().equals(' ')) add.pop(); //Space handling for return type
                    while (!add.isEmpty() && !add.peek().equals(' ')) {
                        if (add.peek().equals('>')) {
                            returnType.append(add.pop()); //Closing parenthesis
                            while (!add.isEmpty() && !add.peek().equals('<')) {
                                returnType.append(add.pop()); //If spaces found
                            }
                            if (!add.isEmpty()) returnType.append(add.pop()); //Opening parenthesis
                            if (add.peek().equals(' ')) returnType.append(add.pop()); //Space handling for method
                            while (!add.isEmpty() && !add.peek().equals(' ')) {
                                returnType.append(add.pop()); //Return type for arrays
                            }
                        } else if (add.peek().equals(']')) {
                            returnType.append(add.pop()); //Closing parenthesis
                            while (!add.isEmpty() && !add.peek().equals('[')) {
                                returnType.append(add.pop()); //If spaces found
                            }
                            arguments(add, returnType);
                        } else returnType.append(add.pop()); //Basic return type
                    }
                }
                if (!add.isEmpty()) add.pop();//Remove others
            }

            StringBuilder result = new StringBuilder();
            if (!method.isEmpty()) { //Solved issue of null or extra "" string
                method.reverse();
                result.append(method);
                String m = method.toString();
                if (!m.contains("main") && !m.contains("if") && !m.contains("for") && !m.contains("while")
                        && !m.contains("try") && !m.contains("do") && !m.contains(".")) {

                    boolean isConstructor = constructorChecker(newFile, method.toString());
                    if (isConstructor) result.append(", type: constructor"); //There might be corner cases
                    else {
                        //Special case for default constructor
                        if (!returnType.isEmpty()) {
                            result.append(", return type: ");
                            returnType.reverse();
                            result.append(returnType);
                        }
                    }
                    if (!finalResult.toString().contains(result)) {
                        finalResult.append(result).append("\n");
                    }
                }
            }
        }

        System.out.println("Methods: ");
        System.out.println(finalResult);

    }

    private static void arguments(Stack<Character> add, StringBuilder returnType) {
        if (!add.isEmpty()) returnType.append(add.pop()); //Opening parenthesis
        if (!add.isEmpty() && add.peek().equals(' ')) returnType.append(add.pop()); //Space handling for method
        while (!add.isEmpty() && !add.peek().equals(' ')) {
            returnType.append(add.pop()); //Return type for arrays
        }
    }

    public static boolean foundCurly(String line, String newLine) {
        int idx = line.indexOf(')');
        int idx1 = line.indexOf('{');
        if (idx >= 0) {
            /*
             * Three cases are possible as extra spaces were handled
             * 1. If there is no space between closing parenthesis and opening curly
             * 2. If there is only a space between them
             * 3. If throws has been called if those found
             * 4. 1 and 2 has to be side by side if the case is not 3
             * */
            if (idx < idx1) {
                String temp = line.substring(idx, idx1 + 1);
                return temp.equals("){") || temp.equals(") {") || temp.contains("throws");
            } else if (idx1 == -1) {
                // If closing parenthesis ")" is the last character in the line
                // Or exception has been thrown after ")"
                if (idx == line.length() - 1 || line.substring(idx).contains("throws")) {
                    return newLine.equals("{");
                }
            }
        }
        return false;
    }

    public static boolean constructorChecker(String file, String method) { //How it should be handled (Need to ask ST)
        StringTokenizer input = new StringTokenizer(file);
        String constructor;
        while (input.hasMoreTokens()) {
            constructor = input.nextToken();
            if (constructor.equals("class")) {
                constructor = input.nextToken();
                method = method.split("\\(")[0];
                if (constructor.equals(method)) return true;
            }
        }
        return false;
    }

    //With Regex
    public static String indentationRE(String file) {
        file = file.replaceAll(" +", " ") //Removes extra white spaces
                .replaceAll("//.*|/\\*(.|\\n)*?\\*/", "") //Removes comments
                .replaceAll("\"(.*?)\"", "") //Removes String
                .replaceAll("(?m)^[ \t]*\r?\n", ""); //Removes extra lines
        System.out.println(file);
        return file;
    }

    //Without Regex
    public static String indentationWORE(String file) {
        file = removeExtraSpaces(file);
        file = removeComments(file);
        file = removeExtraLines(file);
        file = removeStringLines(file);

        return file;
    }

    public static String removeExtraSpaces(String file) {
        StringTokenizer st = new StringTokenizer(file, " ");
        StringBuilder sb = new StringBuilder();

        while (st.hasMoreElements()) {
            sb.append(st.nextElement()).append(" ");
        }
        return sb.toString();
    }

    public static String removeExtraLines(String file) {
        StringTokenizer st = new StringTokenizer(file, "\n");
        StringBuilder sb = new StringBuilder();

        while (st.hasMoreElements()) {
            sb.append(st.nextElement()).append("\n");
        }
        return sb.toString();
    }

    public static String removeStringLines(String file) {
        StringBuilder sb = new StringBuilder();
        char[] arr = file.toCharArray();
        int found = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '"' && found == 0) {
                found = 1;
            } else if (arr[i] == '"' && found == 1) {
                found = 0;
            } else if (found == 0) sb.append(arr[i]);
        }

        return sb.toString();
    }

    public static String removeComments(String file) {
        Scanner input = new Scanner(file).useDelimiter("");
        StringBuilder result = new StringBuilder();
        int state = 0;

        while (input.hasNext()) {
            String a = input.next();
            switch (state) {
                case 0:
                    if (a.equals("/") && input.hasNext()) { //Checking first / with / or *
                        String b = input.next();
                        if (b.equals("/")) state = 1; //Found inline comment, updating state
                        else if (b.equals("*")) state = 3; //Found block comment, updating state
                        else result.append(a).append(b);  //Non commented string
                    } else result.append(a); //Non commented string
                    break;
                case 1:
                    if (a.equals("\n")) { //Appending new line and restating
                        state = 0;
                        result.append("\n");
                    }
                    break;
                case 3:
                    if (a.equals("\n")) { //Appending new line and restating
                        state = 2;
                        result.append("\n");
                    }
                case 2:
                    while (a.equals("*") && input.hasNext()) { //Searching for block comment ending state
                        if (input.next().equals("/")) {
                            state = 0;
                            break;
                        }
                    }
            }
        }
        return result.toString();
    }
}
