package unimessenger.userinteraction.tui;

import unimessenger.Main;

public class Outputs
{
    private static final String RESET = "\u001B[0m";
    private static final String WHITE = "\u001B[37m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";

    private String color = "";
    private String creatorClass = null;
    private String text = "";
    private boolean canPrint = false;

    public static Outputs create(String message)
    {
        Outputs o = new Outputs();
        o.text = message;
        return o;
    }
    public static Outputs create(String message, String className)
    {
        Outputs o = create(message);
        o.creatorClass = className;
        return o;
    }

    public Outputs always()
    {
        canPrint = true;
        return this;
    }
    public Outputs debug()
    {
        if(Main.debug) canPrint = true;
        return this;
    }
    public Outputs verbose()
    {
        if(Main.verbose) canPrint = true;
        return this;
    }

    public Outputs INFO()
    {
        color = WHITE;
        return this;
    }
    public Outputs WARNING()
    {
        color = YELLOW;
        return this;
    }
    public Outputs ERROR()
    {
        color = RED;
        return this;
    }
    public Outputs ALERT()
    {
        color = GREEN;
        return this;
    }

    public void print()
    {
        if(canPrint)
        {
            if(creatorClass != null) System.out.print(color + creatorClass + ": ");
            System.out.println(color + text + RESET);
        }
    }
}