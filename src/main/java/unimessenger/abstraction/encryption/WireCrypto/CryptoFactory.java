package unimessenger.abstraction.encryption.WireCrypto;

import com.wire.bots.cryptobox.CryptoBox;

public class CryptoFactory {
    private static CryptoBox b;

    public static CryptoBox getCryptoInstance(){
        if (b == null){
            try {
                b = CryptoBox.open("DataStorage/Box");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return b;
    }

    public static void closeBox(){
        if(b!=null && !b.isClosed())
        b.close();
    }
}
