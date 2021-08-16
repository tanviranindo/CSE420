package Lab.Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class MethodExtractor {

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
            file.append(input.nextLine().trim().replaceAll(" +", " ")); //Extra space handling
            file.append("\n");
        }
        String newFile = removeComments(file.toString());
        input = new Scanner(newFile);
        Scanner input2 = new Scanner(newFile);
        StringBuilder finalResult = new StringBuilder();

        while (input.hasNextLine()) {
            Stack<Character> add = new Stack<>();
            StringBuilder method = new StringBuilder();
            StringBuilder returnType = new StringBuilder();
            String temporary = input.nextLine();
            input2.nextLine();
            if (temporary.isEmpty()) continue;

            boolean isFound = false;
            if (input.hasNext()) isFound = temporary.contains("{") || input2.next().equals("{");


            for (char c : temporary.toCharArray()) {
                add.push(c);
            }

            while (!add.isEmpty()) {
                if (add.peek().equals(';')) {
                    add.pop();
//                    break;
                } else if (isFound && add.peek().equals(')')) {
                    method.append(add.pop()); //Closing parenthesis
                    while (!add.isEmpty() && !add.peek().equals('(')) {
                        method.append(add.pop()); //Arguments
                    }
                    duplicate(add, method);
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
                            duplicate(add, returnType);
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

                    if (!returnType.isEmpty()) {
                        result.append(", return type: ");
                        returnType.reverse();
                        result.append(returnType);
                    } else {
                        //Special case for default constructor
                        if (isConstructor) result.append(", type: constructor"); //There might be corner cases
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

    private static void duplicate(Stack<Character> add, StringBuilder returnType) {
        if (!add.isEmpty()) returnType.append(add.pop()); //Opening parenthesis
        if (!add.isEmpty() && add.peek().equals(' ')) returnType.append(add.pop()); //Space handling for method
        while (!add.isEmpty() && !add.peek().equals(' ')) {
            returnType.append(add.pop()); //Return type for arrays
        }
    }

    public static boolean foundCurly(String line) {

        return true;
    }

    public static boolean constructorChecker(String file, String method) { //How it should be handled (ST)
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
                    if (a.equals("\n")) {
                        state = 0;
                        result.append("\n");
                    }
                    break;
                case 3:
                    if (a.equals("\n")) {
                        state = 2;
                        result.append("\n");
                    }
                case 2:
                    while (a.equals("*") && input.hasNext()) {
                        if (input.next().equals("/")) {
                            state = 0;
                            break;
                        }
                    }
            }
        }
        input.close();
        return result.toString();
    }
}
