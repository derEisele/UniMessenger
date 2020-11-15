package unimessenger;

import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Updater;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
    public static boolean debug = false;
    public static boolean verbose = false;

    public static Thread updt;
    public static Thread cli;

    public static void main(String[] args)
    {
        ArrayList<String> arguments = new ArrayList<>(Arrays.asList(args));

        if(arguments.contains("-d")) debug = true;
        if(arguments.contains("-v")) verbose = true;

        Outputs.printInfo("Uni-Messenger starting...");
        Outputs.printDebug("Loading login files...");
        Outputs.printError("Missing function");//TODO: Load files with login data
        Outputs.printDebug("Login files loaded");

        Outputs.printError("Missing function");//TODO: Either ask user to select a messenger service, or show all conversations of all messenger services in 1 list

        Outputs.printDebug("Loading stored messages...");
        Outputs.printError("Missing function");//TODO: Load files with previously saved messages of selected messenger or all messengers
        Outputs.printDebug("Stored messages loaded");

        Outputs.printDebug("Creating new Thread for Updater");
        updt = new Thread(new Updater());
        Outputs.printDebug("Updater Thread created");
        Outputs.printDebug("Starting Updater Thread");
        updt.start();
        Outputs.printDebug("Updater started");

        Outputs.printDebug("Creating new Thread for CLI...");
        cli = new Thread(new CLI());
        Outputs.printDebug("CLI Thread created");
        Outputs.printDebug("Starting CLI Thread...");
        cli.start();
        Outputs.printDebug("CLI started");

        Outputs.printInfo("Uni-Messenger started");
    }
}