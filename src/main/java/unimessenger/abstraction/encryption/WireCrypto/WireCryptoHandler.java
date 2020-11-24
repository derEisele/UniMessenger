package unimessenger.abstraction.encryption.WireCrypto;

import com.wire.bots.cryptobox.CryptoException;
import com.wire.bots.cryptobox.PreKey;

public class WireCryptoHandler {
    public static Prekey[] generatePreKeys(int start, int count){
        Prekey [] Keys = new Prekey[count];

        try {
            PreKey [] keyTemp = CryptoFactory.getCryptoInstance().newPreKeys(start, count);
            for(int i = 0;i < keyTemp.length; i++){
                Keys[i] = new Prekey(keyTemp[i]);
            }

        } catch (CryptoException e) {
            e.printStackTrace();
            cleanUp();
        }

        return Keys;
    }

    public static Prekey generateLastPrekey(){
        try {
            return new Prekey(CryptoFactory.getCryptoInstance().newLastPreKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void testCase(){

    }

    public static void encrypt(Prekey pk, String content){
        
    }

    public static void cleanUp(){
        CryptoFactory.closeBox();
    }
}
