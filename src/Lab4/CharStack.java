package Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class CharStack {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/Lab/Lab4/input.txt"));
        StringBuilder add = new StringBuilder();
        while (input.hasNextLine()) {
            add.append(input.nextLine().trim().replaceAll(" +", " ")); //Extra space handling
            add.append("\n");
        }
        String file = removeComments(add.toString());

        System.out.println("Methods: ");
        input = new Scanner(file);
        while (input.hasNextLine()) {
            okay(input.nextLine());
        }
    }

    public static void okay(String line) {
        Stack<Character> add = new Stack<>();
        StringBuilder method = new StringBuilder();
        StringBuilder returnType = new StringBuilder();

        for (char c : line.toCharArray()) {
            add.push(c);
        }

        while (!add.isEmpty()) {
            if (add.peek().equals(';')) break;
            else if (add.peek().equals(')')) {
                method.append(add.pop()); //Closing parenthesis
                while (!add.isEmpty() && !add.peek().equals('(')) {
                    method.append(add.pop()); //Arguments
                }
                if (!add.isEmpty()) method.append(add.pop()); //Opening parenthesis
                if (!add.isEmpty() && add.peek().equals(' ')) method.append(add.pop()); //Space handling for method
                while (!add.isEmpty() && !add.peek().equals(' ')) {
                    method.append(add.pop()); //Method name
                }
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
                        if (!add.isEmpty()) returnType.append(add.pop()); //Opening parenthesis
                        if (add.peek().equals(' ')) returnType.append(add.pop()); //Space handling for method
                        while (!add.isEmpty() && !add.peek().equals(' ')) {
                            returnType.append(add.pop()); //Return type for arrays
                        }
                    } else returnType.append(add.pop()); //Basic return type
                }
            } else if (!add.isEmpty()) add.pop();//Remove others
        }

        StringBuilder result = new StringBuilder();
        if (!method.isEmpty()) { //Solved issue of null or extra "" string
            method.reverse();
            result.append(method);
            String m = method.toString();
            if (!m.contains("main") && !m.contains("if") && !m.contains("for") && !m.contains("while")
                    && !m.contains("try") && !m.contains("do") && !m.contains(".")) {
                if (!returnType.isEmpty()) {
                    result.append(", return type: ");
                    returnType.reverse();
                    result.append(returnType);
                } else {
                    //Special case for default constructor
                    result.append(", type: constructor"); //There might be corner cases
                }
                System.out.println(result);
            }
        }
    }


    public static String removeComments(String file) {
        Scanner input = new Scanner(file).useDelimiter("");
        StringBuilder result = new StringBuilder();
        int state = 0;

        while (input.hasNext()) {
            String a = input.next();
            switch (state) {
                case 0:
                    if (a.equals("/") && input.hasNext()) { //Searching first / with / or *
                        String b = input.next();
                        if (b.equals("/")) state = 1;
                        else if (b.equals("*")) state = 3;
                        else result.append(a).append(b);
                    } else result.append(a);
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
