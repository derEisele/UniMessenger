package unimessenger;

import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Updater;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
    public static boolean debug = false;
    public static boolean verbose = false;

    public static Thread cli;
    public static Thread updt;

    public static void main(String[] args)
    {
        ArrayList<String> arguments = new ArrayList<>(Arrays.asList(args));

        if(arguments.contains("-d")) debug = true;
        if(arguments.contains("-v")) verbose = true;

        Outputs.create("Uni-Messenger starting...").verbose().INFO().print();
        Outputs.create("Initializing storage...").verbose().INFO().print();
        WireStorage.init();
        Outputs.create("Storage initialized").verbose().INFO().print();
        Outputs.create("Loading login files...").verbose().INFO().print();
        WireStorage.readDataFromFiles();
        Outputs.create("File-loading finished").verbose().INFO().print();

        Outputs.create("Creating new thread for updater").verbose().INFO().print();
        updt = new Thread(new Updater());
        Outputs.create("Updater thread created").verbose().INFO().print();
        Outputs.create("Starting updater thread").verbose().INFO().print();
        updt.start();
        Outputs.create("Updater thread started").verbose().INFO().print();

        Outputs.create("Creating new Thread for CLI").verbose().INFO().print();
        cli = new Thread(new CLI());
        Outputs.create("CLI thread created").verbose().INFO().print();
        Outputs.create("Starting CLI thread").verbose().INFO().print();
        cli.start();
        Outputs.create("CLI thread started").verbose().INFO().print();

        Outputs.create("Uni-Messenger started").verbose().INFO().print();
    }
}