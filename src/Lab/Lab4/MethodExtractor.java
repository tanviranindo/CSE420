package Lab.Lab4;

/**
 * @author Tanvir Rahman
 * @ID 19101268
 * @Section 05
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodExtractor {

    public static void main(String[] args) {
        String fileName = "src/Lab/Lab4/input.txt";
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            StringBuilder file = new StringBuilder();
            String temp;
            while ((temp = input.readLine()) != null) {
                file.append(temp.trim());
                file.append("\n");
            }

            withoutREGEX(file.toString());  //WITH or WITHOUT REGEX, BOTH WILL WORK
//        withREGEX(file.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /******************************************** WITHOUT REGEX ********************************************/
    public static String indentationWORE(String file) {
        file = removeComments(file);
        file = removeExtraLines(file);
        file = removeStringLines(file);
        file = removeExtraSpaces(file);
//        file = basicSpaceHandle(file);
//        System.out.println(file);
        return file;
    }

    private static void withoutREGEX(String file) {
        String newFile = indentationWORE(file);
        Scanner input = new Scanner(newFile);
        Scanner input2 = new Scanner(newFile);
        StringBuilder finalResult = new StringBuilder();

        while (input.hasNextLine()) {
            Stack<Character> add = new Stack<>();
            StringBuilder method = new StringBuilder();
            StringBuilder returnType = new StringBuilder();
            String line = input.nextLine();
            if (input2.hasNext())
                input2.nextLine(); //Needed for new line curly brace cases (StringTokenizer could be used)
            if (line.isEmpty()) continue;
            boolean isValid = false;
            if (input2.hasNext()) isValid = foundCurly(line, input2.next());

            for (char c : line.toCharArray()) {
                add.push(c);
            }

            while (!add.isEmpty()) {
                if (add.peek().equals(';')) { //Removing ; when found
                    add.pop();
                } else if (add.peek().equals(')') && isValid) { //
                    method.append(add.pop()); //Closing parenthesis
                    while (!add.isEmpty() && !add.peek().equals('(')) {
                        method.append(add.pop()); //Arguments
                    }

                    arguments(add, method); //Opening parenthesis and space handling

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

                            arguments(add, returnType); //Opening parenthesis and space handling

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
                        && !m.contains("try") && !m.contains("do") && !m.contains(".") && !m.contains("switch")) {

                    boolean isConstructor = constructorChecker(newFile, method.toString());
                    //Special case for default constructor
                    if (!returnType.isEmpty()) {
                        result.append(", return type: ");
                        returnType.reverse();
                        result.append(returnType);
                    } else if (isConstructor) {
                        result.append(", type: constructor"); //There might be corner cases
                    }
                    if (!finalResult.toString().contains(result)) {
                        finalResult.append(result).append("\n");
                    }
                }
            }
        }

        System.out.println("Methods:");
        System.out.println(finalResult);

    }

    //Opening parenthesis and space handling
    private static void arguments(Stack<Character> add, StringBuilder type) {
        if (!add.isEmpty()) type.append(add.pop()); //Opening parenthesis
        if (!add.isEmpty() && add.peek().equals(' ')) type.append(add.pop()); //Space handling
        while (!add.isEmpty() && !add.peek().equals(' ')) {
            type.append(add.pop()); //Return type
        }
    }

    private static boolean foundCurly(String line, String newLine) {
//        System.out.println(line + " **************** " + newLine);
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

    private static boolean constructorChecker(String file, String method) { //How it should be handled (Need to ask ST)
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


    private static String removeExtraSpaces(String file) {
        StringTokenizer st = new StringTokenizer(file, " ");
        StringBuilder result = new StringBuilder();

        while (st.hasMoreElements()) {
            result.append(st.nextElement()).append(" ");
        }
        return result.toString();
    }

    private static String removeExtraLines(String file) {
        StringTokenizer st = new StringTokenizer(file, "\n");
        StringBuilder result = new StringBuilder();

        while (st.hasMoreElements()) {
            result.append(st.nextElement()).append("\n");
        }
        return result.toString();
    }

    private static String removeStringLines(String file) {
        StringBuilder result = new StringBuilder();
        char[] arr = file.toCharArray();
        int found = 0;
        for (char c : arr) {
            if (c == '"' && found == 0) {
                found = 1;
            } else if (c == '"') {
                found = 0;
            } else if (found == 0) result.append(c);
        }

        return result.toString();
    }

    private static String removeComments(String file) {
        Scanner input = new Scanner(file).useDelimiter("");
        StringBuilder result = new StringBuilder();
        int state = 0;

        while (input.hasNext()) {
            String a = input.next();
            switch (state) {
                case 0:
                    if (a.equals("/") && input.hasNext()) { //Checking first / with / or *
                        String b = input.next();
                        if (b.equals("/")) state = 1; //Found inline comment, switching state
                        else if (b.equals("*")) state = 3; //Found block comment, switching state
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


    /******************************************** WITH REGEX ********************************************/


    private static String basicSpaceHandle(String file) {
        Scanner input = new Scanner(file);
        StringBuilder result = new StringBuilder();
        while (input.hasNextLine()) {
            char[] str = input.nextLine().toCharArray();
            for (int i = 0; i < str.length; i++) {
                if (str[i] == '"') {
                    result.append(' ').append(str[i]);
                } else if ((i + 1) < str.length && str[i] == ' ' && str[i + 1] == ';') {
                    result.append(str[i]);
                } else if (str[i] == '{') {
                    result.append("\n{\n");
                } else if (str[i] == '}') {
                    result.append("\n}\n");
                } else if (str[i] == ';') {
                    result.append(";\n");
                } else result.append(str[i]);
            }
        }
        return result.toString();
    }

    static String indentationRE(String file) {
        file = file.replaceAll(" +", " ") //Removes extra white spaces
                .replaceAll(" ;", ";") //Removes redundant spaces
                .replaceAll("//.*|/\\*(.|\\n)*?\\*/", "") //Removes comments
                .replaceAll("\"(.*?)\"", "") //Removes String
                .replaceAll("(?m)^[ \t]*\r?\n", ""); //Removes extra lines
        return file;
    }

    private static void withREGEX(String file) {
        file = indentationRE(file);

        HashMap<Integer, String> methods = new HashMap<>();
        HashMap<Integer, String> returnTypes = new HashMap<>();
        String line = "[\\w<>\\[\\],\\s]*[_a-zA-Z][_a-zA-Z0-9]*(\\(.*?\\))[\\s\\w,^.;]*?\\{";
        Pattern pattern = Pattern.compile(line);
        Matcher matcher = pattern.matcher(file);
        int index = 0;
        while (matcher.find()) {
            String temp = matcher.group().trim();
//            System.out.print(temp);
            String method = "[_a-zA-Z][_a-zA-Z0-9]*(\\(.*?\\))";
            Pattern pattern1 = Pattern.compile(method);
            Matcher matcher1 = pattern1.matcher(temp);
            if (matcher1.find()) {
                String temp1 = matcher1.group().trim();
                if (!temp1.contains("main") && !temp1.contains("if") && !temp1.contains("for") && !temp1.contains("while")
                        && !temp1.contains("try") && !temp1.contains("do") && !temp1.contains(".") && !temp1.contains("switch")) {

                    index++;
                    methods.put(index, temp1); //Adding method
                    String prevMethod = "[\\w<>\\[\\],\\s]+[_a-zA-Z][_a-zA-Z0-9]*(\\(.*?\\))";
                    Pattern pattern2 = Pattern.compile(prevMethod);
                    Matcher matcher2 = pattern2.matcher(temp);
                    if (matcher2.find()) {
                        String temp2 = matcher2.group().trim();
//                    System.out.print(temp2);
                        String prevPart = "[\\w<>\\[\\],\\s]+?(?=\\s[_a-zA-Z][_a-zA-Z0-9]*(\\(.*?\\)))";
                        Pattern pattern3 = Pattern.compile(prevPart);
                        Matcher matcher3 = pattern3.matcher(temp2);
                        if (matcher3.find()) {
                            String temp3 = matcher3.group().trim().replaceAll("((public|protected|private|static|\\s) +)", "");
//                        System.out.println(temp3);
                            returnTypes.put(index, temp3);
                        }
                    }
                }
            }
        }

        Set<Integer> methodSet = methods.keySet();
        Set<Integer> returnSet = returnTypes.keySet();

        System.out.println("Methods:");
        for (Integer i : methodSet) {
            boolean condition = true;
            for (Integer j : returnSet) {
                if (Objects.equals(i, j)) {
                    System.out.println(methods.get(i) + ", return type: " + returnTypes.get(i));
                } else {
                    StringTokenizer st = new StringTokenizer(file);
                    String className = "";
                    while (st.hasMoreTokens()) {
                        String temp = st.nextToken();
                        if (temp.equals("class")) {
                            className = st.nextToken();
                            break;
                        }
                    }
                    if (!returnTypes.containsKey(i) && condition) {
                        String methodOnly = methods.get(i);
                        methodOnly = methodOnly.substring(0, methodOnly.indexOf("("));
                        if (methodOnly.equals(className)) {
                            System.out.println(methods.get(i) + ", type: constructor");
                        }
                        condition = false;
                    }
                }
            }
        }
    }
}

/*
 * Cases that has been handled not mentioned in the assignment
 * 1. Commented part of code
 * 2. Different return type such as : int [], HashMap< List, Integer >
 * 3. Constructor is also added. If class == method without return type then type: constructor
 * 4. Inline string code such as "public static void method() \\{ "
 * 5. Extra spaces, lines
 * 6. withREGEX method inline "REGEX" was handled but withoutREGEX method inline "REGEX" was not
 */