package Lab.Lab4;

import java.util.Scanner;

public class Testy {
    public static class StringBufferExample1 {
        public static void main(String[] args) {
            System.out.println("Enter string1: ");
            Scanner sc = new Scanner(System.in);
            String str1 = sc.next();
            System.out.println("Enter string2: ");
            String str2 = sc.next();
            char[] charArray = new char[str1.length() + str2.length()];
            for (int i = 0; i < str2.length(); i++) {
                charArray[i] = str2.charAt(i);
            }
            for (int i = str2.length(); i < charArray.length; i++) {
                charArray[i] = str1.charAt(i - str2.length());
            }
            String result = new String(charArray);
            System.out.println(result);
        }
    }

    public static class StringBufferExample2 {
        public static void main(String[] args) {
            System.out.println("Enter string1: ");
            Scanner sc = new Scanner(System.in);
            String str1 = sc.next();
            System.out.println("Enter string2: ");
            String str2 = sc.next();
            StringBuffer sb = new StringBuffer();
            sb.append(str2);
            sb.append(str1);
            String result = sb.toString();
            System.out.println(result);
        }
    }
}
