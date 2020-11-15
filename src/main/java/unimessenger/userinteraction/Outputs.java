package unimessenger.userinteraction;

import unimessenger.Main;

import java.util.Scanner;

public class Outputs
{
    private static final String INFO = "[INFO]";
    private static final String DEBUG = "[DEBUG]";
    private static final String ERROR = "[ERROR]";
    private static final Scanner sc = new Scanner(System.in);

    public static void printInfo(String text)
    {
        if(Main.verbose) System.out.println(INFO + text);
    }
    public static void printDebug(String text)
    {
        if(Main.debug) System.out.println(DEBUG + text);
    }
    public static void printError(String text)
    {
        if(Main.verbose || Main.debug) System.out.println(ERROR + text);
    }

    public static int getIntAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.print("Input: ");

        printDebug("Waiting for user-input...");

        return sc.nextInt();
    }
    public static String getStringAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.print("Input: ");

        printDebug("Waiting for user-input...");

        return sc.nextLine();
    }
    public static void cannotHandleUserInput()
    {
        System.out.println("Invalid Option.");
    }
}