package unimessenger.abstraction.wire.crypto;

import com.wire.bots.cryptobox.CryptoBox;
import com.wire.bots.cryptobox.CryptoException;
import com.wire.bots.cryptobox.PreKey;
import unimessenger.userinteraction.Outputs;

import java.util.Base64;
import java.util.UUID;

public class WireCryptoHandler
{
    static CryptoBox box;

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

        } catch (CryptoException ignored)
        {
            Outputs.create("Crypto Exception while generating PreKeys", "WireCryptoHandler").debug().ERROR().print();
            cleanUp();
        }
        return Keys;
    }

    public static Prekey generateLastPrekey()
    {
        try
        {
            return new Prekey(CryptoFactory.getCryptoInstance().newLastPreKey());
        } catch(Exception ignored)
        {
            Outputs.create("Exception while generating LastKey", "WireCryptoHandler").debug().ERROR().print();
        }
        return null;
    }

    public static String encrypt(String userID, String clientID, Prekey pk, byte[] content)
    {
        if(box == null) box = CryptoFactory.getCryptoInstance();
        String id = generateSessionID(userID, clientID);
        PreKey key = toCryptoPreKey(pk);
        byte[] cypher = null;

        try
        {
            cypher = box.encryptFromPreKeys(id, key, content);
        } catch(CryptoException ignored)
        {
            Outputs.create("Encrypting message failed", "WireCryptoHandler").debug().ERROR().print();
        }
        return Base64.getEncoder().encodeToString(cypher);
    }

    public static byte[] decrypt(UUID from, String sender, String text)
    {
        if(box == null) box = CryptoFactory.getCryptoInstance();

        byte[] dec;
        String sID = generateSessionID(from.toString(), sender);

        try
        {
            dec = box.decrypt(sID, Base64.getDecoder().decode(text));
        } catch(CryptoException ignored)
        {
            Outputs.create("Decrypt CryptoException", "WireCryptoHandler").always().ERROR();
            dec = new byte[1];
        }

        return dec;
    }

    private static PreKey toCryptoPreKey(Prekey old)
    {
        return new PreKey(old.getID(), Base64.getDecoder().decode(old.getKey()));
    }

    private static String generateSessionID(String userID, String clientID)
    {
        return String.format("%s_%s", userID, clientID);
    }

    public static void cleanUp()
    {
        CryptoFactory.closeBox();
    }
}
