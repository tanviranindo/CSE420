package Lab2;

/**
 * @author Tanvir Rahman
 * @ID 19101268
 * @Section 05
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MWDetector {
    public static void main(String[] args) {
        String path = "src/Lab/Lab2/input.txt";
//         String path = "src/Lab/Lab2/input.txt";
        int i = 0;
        try {
            BufferedReader input = new BufferedReader(new FileReader(path));
            int length = Integer.parseInt(input.readLine());
            for (i = 1; i <= length; i++) {
                String address = input.readLine().toLowerCase();
                checker(address, i);
            }
        } catch (IOException e) {
            System.out.println("Error, file not found!");
        } catch (NullPointerException e) {
            System.out.println("Error, no input found after line number " + i + "!");
        }
    }

    public static void checker(String address, int lineNumber) {
        String webDomain = address.substring(address.indexOf(".") + 1);
        String mailDomain = address.substring(address.indexOf("@") + 1);

        boolean mail = (atTheRateSign(address) &&
                !isProtocol(address) &&
                noSpace(address) &&
                noConsecutive(address) &&
                noSpecialCharacter(address) &&
                singleAddressSign(address) &&
                notEmptyLocal(address) &&
                startsWithNumber(address) &&
                canNotContainInMail(address) &&
                validDash(mailDomain) &&
                localLength(address) &&
                domainLength(mailDomain) &&
                domainCheck(mailDomain));

        boolean web = (!atTheRateSign(address) &&
                isProtocol(address) &&
                noSpace(address) &&
                noConsecutive(address) &&
                noSpecialCharacter(webDomain) &&
                validDash(webDomain) &&
                domainLength(webDomain) &&
                domainCheck(webDomain));

        if (mail) System.out.println("Email, " + lineNumber);
        else if (web) System.out.println("Web, " + lineNumber);
        else System.out.println("Invalid, " + lineNumber);
    }

    public static boolean atTheRateSign(String address) {
        return address.contains("@");
    }

    public static boolean noSpace(String address) {
        return !address.contains(" ");
    }

    public static boolean isProtocol(String address) {
        return address.startsWith("http://") || address.startsWith("http://www.") ||
                address.startsWith("https://") || address.startsWith("https://www.") ||
                address.startsWith("www.");
    }

    public static boolean noConsecutive(String address) {
        char[] arr = address.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] == '.') && (i + 1 < arr.length) && (arr[i + 1] == '.')) {
                return false;
            }
            if ((arr[i] == '-') && (i + 1 < arr.length) && (arr[i + 1] == '-')) {
                return false;
            }
            if ((arr[i] == '_') && (i + 1 < arr.length) && (arr[i + 1] == '_')) {
                return false;
            }
        }
        return true;
    }

    public static boolean noSpecialCharacter(String address) {
        char[] arr = address.toCharArray();

        for (char c : arr) {
            //ASCII Value a = 97 to z = 122 || 0 = 48 to 9 = 57 || @ = 64 || - = 45 || . = 46 || _ = 95
            if (!((c >= 97 && c <= 122) || (c >= 48 && c <= 57) || (c == 64 || c == 45 || c == 46 || c == 95))) {
                return false;
            }
        }
        return true;
    }

    public static boolean domainCheck(String domain) {
        if (domain.contains(".") && domain.length() > 3 && domain.indexOf(".") < 64) { // @abc //a.c //length >= 64
            int tldIndex = domain.indexOf('.');
            String tld = domain.substring(tldIndex + 1);
            String tldWithDot = domain.substring(tldIndex);
            int count = 0, tldLength = 0;
            ArrayList<Integer> letter = new ArrayList<>();
            for (char c : tldWithDot.toCharArray()) {
                if (c == 46) {
                    letter.add(tldLength);
                    count++;
                    tldLength = 0;
                } else {
                    tldLength++;
                }
            }
            letter.add((tldLength));

            if (count >= 1 && count <= 3 && letter.get(count) >= 2) { //Last TLD has to be >=2
                char[] arr = tld.toCharArray();
                for (char c : arr) {
                    if (!(Character.isLetter(c) || c == 46)) { // . = 46
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean startsWithNumber(String address) {
        char c = address.toCharArray()[0];
        return !(c >= 48 && c <= 57); // 0 = 48 to 9 = 57
    }


    public static boolean canNotContainInMail(String address) {
        int index = address.indexOf('@');
        String local = address.substring(0, index);
        String domain = address.substring(index + 1);
        int tldIndex = domain.indexOf('.');
        String tld = domain.substring(tldIndex + 1);
        return !(local.startsWith(".") || local.endsWith(".") ||
                local.startsWith("-") || local.endsWith("-") ||
                local.startsWith("_") || local.endsWith("_"));
    }

    public static boolean validDash(String address) {
        int count = 0;
        char[] arr = address.toCharArray();
        for (int i = 0; i < address.length(); i++) {
            if (arr[i] == 46) {
                count++;
            }
        }
        String tld = "";
        String domain = "";
        if (address.contains(".")) {
            if (count == 3) {
                String partOne = address.substring(0, address.indexOf(".") + 1);
                String partTwo = address.substring(address.indexOf(".") + 1);

                tld = partTwo.substring(partTwo.indexOf(".") + 1);

                partTwo = partTwo.substring(0, partTwo.indexOf("."));
                domain = partOne + partTwo;
            } else {
                tld = address.substring(address.indexOf(".") + 1);
                domain = address.substring(0, address.indexOf("."));
            }
        }

        return !(domain.contains("_") || domain.startsWith("-") || domain.endsWith("-") ||
                tld.contains("_") || tld.contains("-") || tld.endsWith("."));
    }

    public static boolean singleAddressSign(String address) {
        int count = 0;
        char[] arr = address.toCharArray();
        for (char c : arr) {
            if (c == 64) { //@ = 64
                count++;
            }
        }
        return count == 1;
    }

    public static boolean localLength(String address) {
        return address.indexOf('@') <= 64;
    }

    public static boolean domainLength(String domain) {
        return domain.length() <= 253;
    }

    public static boolean notEmptyLocal(String address) {
        int index = address.indexOf('@');
        String local = address.substring(0, index);
        return local.length() > 0;
    }
}
