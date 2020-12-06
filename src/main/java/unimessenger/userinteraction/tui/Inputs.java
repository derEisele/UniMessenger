package unimessenger.userinteraction.tui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Inputs
{
    private static final Scanner sc = new Scanner(System.in);
    private static final BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));

    public static int getIntAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.print("Input: ");

        int ret = -1;
        try
        {
            String in = sc.next();
            ret = Integer.parseInt(in);
        } catch(Exception ignored)
        {
            Outputs.create("Input was not an integer", "Inputs").debug().ERROR().print();
        }
        return ret;
    }
    public static String getStringAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.print("Input: ");

        String ret = null;
        try
        {
            ret = sc.next();
        } catch(Exception ignored)
        {
        }
        return ret;
    }
    public static boolean getBoolAnswerFrom(String question)
    {
        System.out.println(question);
        while(true)
        {
            System.out.print("Input(Yes/No): ");
            Outputs.create("Waiting for user-input...").verbose().INFO().print();

            String answer = sc.next();

            if(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) return true;
            if(answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) return false;

            Outputs.create("Invalid user-input", "Inputs").debug().verbose().WARNING().print();
            System.out.println("Invalid input. Options are 'yes' or 'no'");
        }
    }
    public static String getTextAnswerFrom(String question)
    {
        System.out.println(question);
        System.out.println("Empty line to finish text input");

        StringBuilder ret = new StringBuilder();
        boolean firstLine = true;
        while(true)
        {
            String line = "";
            try
            {
                line = rd.readLine();
            } catch(Exception ignored)
            {
                Outputs.create("Problem with reading a line", "Inputs").debug().ERROR().print();
            }
            if(line.equals("")) break;
            if(firstLine) firstLine = false;
            else ret.append(System.lineSeparator());
            ret.append(line);
        }

        return ret.toString();
    }
}
