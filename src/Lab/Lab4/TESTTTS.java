package Lab.Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static Lab.Lab4.MethodExtractor.removeComments;

public class TESTTTS {
    public static void main(String[] args) throws FileNotFoundException {
        indentation();
    }

    public static void indentation() throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/Lab/Lab4/inputFile.txt"));
        StringBuilder file = new StringBuilder();
        while (input.hasNextLine()) {
            file.append(input.nextLine().trim().replaceAll(" +", " ")); //Extra space handling
            file.append("\n");
        }
        String newFile = removeComments(file.toString());
        input = new Scanner(newFile);

        StringBuilder finalFile = new StringBuilder();

        while (input.hasNext()) {
            String temp = input.next();
            System.out.println(temp);
        }

        System.out.println(finalFile);
    }
}
