package unimessenger.userinteraction;

import unimessenger.Main;

import java.util.Scanner;

public class Outputs
{
    private static final String RESET = "\u001B[0m";
    private static final String INFO = "\u001B[37m[INFO]";
    private static final String DEBUG = "\u001B[33m[DEBUG]";
    private static final String ERROR = "\u001B[31m[ERROR]";
    private static final Scanner sc = new Scanner(System.in);

    public static void printInfo(String text)
    {
        if(Main.verbose) System.out.println(INFO + text + RESET);
    }
    public static void printDebug(String text)
    {
        if(Main.debug) System.out.println(DEBUG + text + RESET);
    }
    public static void printError(String text)
    {
        if(Main.verbose || Main.debug) System.out.println(ERROR + text + RESET);
    }

    public static int getIntAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.print("Input: ");

        printDebug("Waiting for user-input...");

        //TODO: Validate if input is an int
        return sc.nextInt();
    }
    public static String getStringAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.print("Input: ");

        printDebug("Waiting for user-input...");

        return sc.next();
    }
    public static boolean getBoolAnswerFrom(String question)
    {
        System.out.println(question);
        while(true)
        {
            System.out.print("Input(Yes/No): ");
            printDebug("Waiting for user-input...");

            String answer = sc.next();

            if(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) return true;
            if(answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) return false;

            printDebug("Invalid user-input");
            System.out.println("Invalid input. Options are 'yes' or 'no'");
        }
    }
    public static void cannotHandleUserInput()
    {
        System.out.println("Invalid Option.");
    }
}