//package Lab.Lab4;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;
//import java.util.StringTokenizer;
//
//
//public class Indent {
//    public static void main(String[] args) throws FileNotFoundException {
//        Scanner input = new Scanner(new File("src/Lab/Lab4/inputFile.txt"));
//        StringBuilder file = new StringBuilder();
//        while (input.hasNextLine()) {
//            file.append(input.nextLine().trim()); //Extra space handling
//            file.append("\n");
//        }
////        System.out.println(indent(file.toString()));
//        System.out.println(inOneMethod(file.toString()));
////        indentation(file.toString());
//    }
//
//    public static String indent(String file) {
//        file = removeExtraSpaces(file);
//        file = removeComments(file);
//        file = removeExtraLines(file);
//        file = removeStringLines(file);
//
//        return file;
//    }
//
//    public static String inOneMethod(String file) {
//        String pathabo;
//        StringTokenizer st = new StringTokenizer(file, " ");
//        StringBuilder sb = new StringBuilder();
//
//        while (st.hasMoreElements()) {
//            sb.append(st.nextElement()).append(" ");
//        }
//
//        pathabo = sb.toString();
//
//        Scanner input = new Scanner(pathabo).useDelimiter("");
//        StringBuilder result = new StringBuilder();
//        int state = 0;
//
//        while (input.hasNext()) {
//            String a = input.next();
//            switch (state) {
//                case 0:
//                    if (a.equals("/") && input.hasNext()) { //Checking first / with / or *
//                        String b = input.next();
//                        if (b.equals("/")) state = 1; //Found inline comment, updating state
//                        else if (b.equals("*")) state = 3; //Found block comment, updating state
//                        else result.append(a).append(b);  //Non commented string
//                    } else result.append(a); //Non commented string
//                    break;
//                case 1:
//                    if (a.equals("\n")) { //Appending new line and restating
//                        state = 0;
//                        result.append("\n");
//                    }
//                    break;
//                case 3:
//                    if (a.equals("\n")) { //Appending new line and restating
//                        state = 2;
//                        result.append("\n");
//                    }
//                case 2:
//                    while (a.equals("*") && input.hasNext()) { //Searching for block comment ending state
//                        if (input.next().equals("/")) {
//                            state = 0;
//                            break;
//                        }
//                    }
//            }
//        }
//
//        pathabo = result.toString();
//
//        StringTokenizer st1 = new StringTokenizer(pathabo, "\n");
//        StringBuilder sb1 = new StringBuilder();
//
//        while (st1.hasMoreElements()) {
//            sb1.append(st1.nextElement()).append("\n");
//        }
//
//        pathabo = sb1.toString();
//
//        StringBuilder sb2 = new StringBuilder();
//        char[] arr = pathabo.toCharArray();
//        int found = 0;
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] == '"' && found == 0) {
//                found = 1;
//            } else if (arr[i] == '"' && found == 1) {
//                found = 0;
//            } else if (found == 0) sb2.append(arr[i]);
//        }
//        pathabo = sb2.toString();
//
//        return pathabo;
//
//    }
//
//    public static String removeExtraSpaces(String file) {
//        StringTokenizer st = new StringTokenizer(file, " ");
//        StringBuilder sb = new StringBuilder();
//
//        while (st.hasMoreElements()) {
//            sb.append(st.nextElement()).append(" ");
//        }
//        return sb.toString();
//    }
//
//    public static String removeExtraLines(String file) {
//        StringTokenizer st = new StringTokenizer(file, "\n");
//        StringBuilder sb = new StringBuilder();
//
//        while (st.hasMoreElements()) {
//            sb.append(st.nextElement()).append("\n");
//        }
//        return sb.toString();
//    }
//
//    public static String removeStringLines(String file) {
//        StringBuilder sb = new StringBuilder();
//        char[] arr = file.toCharArray();
//        int found = 0;
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] == '"' && found == 0) {
//                found = 1;
//            } else if (arr[i] == '"' && found == 1) {
//                found = 0;
//            } else if (found == 0) sb.append(arr[i]);
//        }
//
//        return sb.toString();
//    }
//
//    public static String removeComments(String file) {
//        Scanner input = new Scanner(file).useDelimiter("");
//        StringBuilder result = new StringBuilder();
//        int state = 0;
//
//        while (input.hasNext()) {
//            String a = input.next();
//            switch (state) {
//                case 0:
//                    if (a.equals("/") && input.hasNext()) { //Checking first / with / or *
//                        String b = input.next();
//                        if (b.equals("/")) state = 1; //Found inline comment, updating state
//                        else if (b.equals("*")) state = 3; //Found block comment, updating state
//                        else result.append(a).append(b);  //Non commented string
//                    } else result.append(a); //Non commented string
//                    break;
//                case 1:
//                    if (a.equals("\n")) { //Appending new line and restating
//                        state = 0;
//                        result.append("\n");
//                    }
//                    break;
//                case 3:
//                    if (a.equals("\n")) { //Appending new line and restating
//                        state = 2;
//                        result.append("\n");
//                    }
//                case 2:
//                    while (a.equals("*") && input.hasNext()) { //Searching for block comment ending state
//                        if (input.next().equals("/")) {
//                            state = 0;
//                            break;
//                        }
//                    }
//            }
//        }
//        return result.toString();
//    }
//
////       \/\/.*|\/\*(.|\n)*?\*\/
//
//    public static String indentation(String temp) {
//        temp = temp.replaceAll(" +", " ") //Removes extra white spaces
//                .replaceAll("//.*|/\\*(.|\\n)*?\\*/", "") //Removes comments
//                .replaceAll("\n", "") //Needed the string in one line to remove string
//                .replaceAll("\"(.*?)\"", "") //Removes String
//                .replaceAll("\\{", "\n{\n") //Creates prev and next new line when first curly found
//                .replaceAll("}", "\n}\n") //Creates prev and next new line when second curly found
//                .replaceAll("for.*?;.*?;.*?\\)", "") //Replacing for loop with nothing as not needed here
//                .replaceAll(";", ";\n") //Creates next new line when second curly found
//                .replaceAll("(?m)^[ \t]*\r?\n", ""); //Removes extra lines
//        System.out.println(temp);
//        return null;
//    }
//}