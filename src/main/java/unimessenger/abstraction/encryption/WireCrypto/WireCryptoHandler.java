package unimessenger.abstraction.encryption.WireCrypto;

import com.wire.bots.cryptobox.CryptoBox;
import com.wire.bots.cryptobox.CryptoException;
import com.wire.bots.cryptobox.PreKey;
import unimessenger.userinteraction.Outputs;

import java.util.Base64;

public class WireCryptoHandler
{
    static CryptoBox box;//TODO: Get the right box

    public static Prekey[] generatePreKeys(int start, int count)
    {
        Prekey[] Keys = new Prekey[count];

        try
        {
            PreKey[] keyTemp = CryptoFactory.getCryptoInstance().newPreKeys(start, count);
            for(int i = 0; i < keyTemp.length; i++)
            {
                Keys[i] = new Prekey(keyTemp[i]);
            }

        } catch(CryptoException e)
        {
            e.printStackTrace();
            cleanUp();
        }

        return Keys;
    }

    public static Prekey generateLastPrekey()
    {
        try
        {
            return new Prekey(CryptoFactory.getCryptoInstance().newLastPreKey());
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void testCase()
    {

    }

    public static String encrypt(String userID, String clientID, Prekey pk, String msg)
    {
        byte[] content = getByteStreamFromMessage(msg);
        String id = userID + "_" + clientID;
        PreKey key = new PreKey(pk.getID(), Base64.getDecoder().decode(pk.getKey()));
        byte[] cypher = new byte[0];
        try
        {
            cypher = box.encryptFromPreKeys(id, key, content);
        } catch(CryptoException ignored)
        {
            Outputs.printError("Encrypting message failed");
        }
        return Base64.getEncoder().encodeToString(cypher);
    }

    private static byte[] getByteStreamFromMessage(String message)
    {
        //TODO: Transform the message String to an IGeneric message from xenon and return it as byte[]
        return null;
    }

    public static void cleanUp()
    {
        CryptoFactory.closeBox();
    }
}
