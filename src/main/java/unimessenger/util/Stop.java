package unimessenger.util;

import unimessenger.Main;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.crypto.CryptoFactory;
import unimessenger.userinteraction.Outputs;

public class Stop implements Runnable
{
    @Override
    public void run()
    {
        Outputs.create("Stopping update thread...").verbose().INFO().print();
        Main.updt.interrupt();
        Outputs.create("Update thread stopped").verbose().INFO().print();

        Outputs.create("Stopping CLI thread...").verbose().INFO().print();
        Main.cli.interrupt();
        Outputs.create("CLI thread stopped").verbose().INFO().print();

        Outputs.create("Writing data to file...").verbose().INFO().print();
        WireStorage.saveDataInFile();
        Outputs.create("Storage written to file").verbose().INFO().print();

        Outputs.create("Cleaning the Box").verbose().INFO().print();
        CryptoFactory.closeBox();
        Outputs.create("Box Clean").verbose().INFO().print();

        Outputs.create("Exiting program...").verbose().INFO().print();
        System.exit(0);
    }
}