package unimessenger;

import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Storage;
import unimessenger.util.Updater;
import unimessenger.util.Variables;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args)
    {
        ArrayList<String> arguments = new ArrayList<>(Arrays.asList(args));

        if(arguments.contains("-d")) Variables.debug = true;
        if(arguments.contains("-v")) Variables.verbose = true;

        Outputs.printInfo("Uni-Messenger starting...");
        Outputs.printDebug("Loading login files...");
        Storage.readDataFromFiles();
        Outputs.printDebug("Login files loaded");

        Outputs.printError("Missing function");//TODO: Either ask user to select a messenger service, or show all conversations of all messenger services in 1 list

        Outputs.printDebug("Loading stored messages...");
        Outputs.printError("Missing function");//TODO: Load files with previously saved messages of selected messenger or all messengers
        Outputs.printDebug("Stored messages loaded");

        Outputs.printDebug("Creating new Thread for Updater");
        Variables.updt = new Thread(new Updater());         //TODO check if the updater works if the bearer token is outdated
        Outputs.printDebug("Updater Thread created");
        Outputs.printDebug("Starting Updater Thread");
        Variables.updt.start();
        Outputs.printDebug("Updater started");

        Outputs.printDebug("Creating new Thread for CLI...");
        Variables.cli = new Thread(new CLI());
        Outputs.printDebug("CLI Thread created");
        Outputs.printDebug("Starting CLI Thread...");
        Variables.cli.start();
        Outputs.printDebug("CLI started");

        Outputs.printInfo("Uni-Messenger started");
    }
}