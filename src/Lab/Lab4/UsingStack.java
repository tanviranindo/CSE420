package Lab.Lab4;

import java.io.File;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class UsingStack {

    public static void main(String[] args) {

        try {
//            URL url = LabQuiz01.class.getResource("input.txt");
            File f = new File("src/Lab/Lab4/input.txt");
            //File f = new File("E:\\workspaces\\CSE420-Lab\\src\\CSE420Lab04akaLabQuiz01\\input.txt");
            Scanner sc = new Scanner(f);

            System.out.println("Methods:");

            while (sc.hasNextLine()) {
                Stack<String> x = new Stack<String>();
                StringBuilder str1 = new StringBuilder();
                String str2 = sc.nextLine();

                if (str2.contains(".") || str2.contains(("=")) || str2.contains("if")
                        || str2.contains("else") || str2.contains("for") || str2.contains("while")) {
                    // checking whether it is loop, condition or function
//                    break;

                } else if (str2.contains("(") && str2.contains(")")) {
                    // for checking any parentheses
                    StringTokenizer token = new StringTokenizer(str2, " ()", true);
                    while (token.hasMoreTokens()) {
                        try {
                            x.push(token.nextToken());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                }

                while (!x.empty()) {
                    try {
                        String z = x.pop();
                        if (z.equals("(")) {
                            str1.insert(0, z);
                            while (x.peek().equals(" ")) {
                                x.pop();
                            }
                            str1.insert(0, x.pop());
                            System.out.print(str1.toString().trim() + ", ");
                            //trim() function is used to remove space
                            while (x.peek().equals(" ")) {
                                x.pop();
                            }
                            System.out.println("return type: " + x.pop());
                            break;
                        } else {
                            str1.insert(0, z);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}