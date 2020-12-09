package unimessenger;

import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.gui.MainWindow;
import unimessenger.userinteraction.tui.CLI;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.userinteraction.tui.menu.MenuDiskCrypto;
import unimessenger.util.Stop;
import unimessenger.util.Updater;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
    public static boolean debug = false;
    public static boolean verbose = false;

    public static Thread cli;
    public static Thread gui;
    public static Thread updt;
    public static Thread stp;

    public static void main(String[] args)
    {
        ArrayList<String> arguments = new ArrayList<>(Arrays.asList(args));

        if(arguments.contains("-d")) debug = true;
        if(arguments.contains("-v")) verbose = true;

        Outputs.create("Uni-Messenger starting...").verbose().INFO().print();
        Outputs.create("Initializing storage...").verbose().INFO().print();
        WireStorage.init();
        Outputs.create("Storage initialized").verbose().INFO().print();

        Outputs.create("Checking Disk encryption...").verbose().INFO().print();
        MenuDiskCrypto.showMenu();
        Outputs.create("Disk is decrypted").verbose().INFO().print();

        Outputs.create("Loading login files...").verbose().INFO().print();
        WireStorage.readDataFromFiles();
        Outputs.create("File-loading finished").verbose().INFO().print();

        Outputs.create("Creating Threads for").verbose().INFO().print();
        Outputs.create("Program end").verbose().INFO().print();
        stp = new Thread(new Stop());
        Outputs.create("Updater").verbose().INFO().print();
        updt = new Thread(new Updater());
        Outputs.create("CLI").verbose().INFO().print();
        cli = new Thread(new CLI());
        Outputs.create("Threads created").verbose().INFO().print();

        Outputs.create("Starting updater thread").verbose().INFO().print();
        updt.start();
        Outputs.create("Updater thread started").verbose().INFO().print();

        Outputs.create("Creating new Thread for CLI...").verbose().INFO().print();
        cli = new Thread(new CLI());
        Outputs.create("CLI thread created").verbose().INFO().print();

        Outputs.create("Creating new Thread for GUI...").verbose().INFO().print();
        gui = new Thread(() -> MainWindow.launch(MainWindow.class, args));
        Outputs.create("GUI thread created").verbose().INFO().print();

        startUI();
        Outputs.create("Uni-Messenger started").verbose().INFO().print();
    }

    private static void startUI()
    {
//        boolean guib = Inputs.getBoolAnswerFrom("Would you like to use a GUI?");
//
//        if(guib)
//        {
//            Outputs.create("GUI starting...").verbose().INFO().print();
//            gui.start();
//            Outputs.create("GUI started").verbose().INFO().print();
//        } else
//        {
        Outputs.create("Starting CLI thread").verbose().INFO().print();
        cli.start();
        Outputs.create("CLI thread started").verbose().INFO().print();
//        }
//
    }
}