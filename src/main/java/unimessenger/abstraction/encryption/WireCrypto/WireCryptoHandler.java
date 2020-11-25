package unimessenger.abstraction.encryption.WireCrypto;

import com.waz.model.Messages;
import com.wire.bots.cryptobox.CryptoBox;
import com.wire.bots.cryptobox.CryptoException;
import com.wire.bots.cryptobox.PreKey;
import unimessenger.userinteraction.Outputs;

import java.util.Base64;
import java.util.UUID;

public class WireCryptoHandler
{
    static CryptoBox box;//TODO: Get the box

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
        encrypt("UID", "CID", generateLastPrekey(), "ThisTestMessageBeing");
    }

    public static String encrypt(String userID, String clientID, Prekey pk, String msg)
    {
        box = CryptoFactory.getCryptoInstance();
        byte[] content = getByteStreamFromMessage(msg);
        //String.format for safer formating
        String id = String.format("%s_%s", userID, clientID);
        PreKey key = new PreKey(pk.getID(), Base64.getDecoder().decode(pk.getKey()));
        //made null for null checking the return value
        byte[] cypher = null;
        try
        {
            cypher = box.encryptFromPreKeys(id, key, content);
        } catch(CryptoException ignored)
        {
            Outputs.printError("Encrypting message failed");
        }
        System.out.println("Cypher: " + Base64.getEncoder().encodeToString(cypher));
        return Base64.getEncoder().encodeToString(cypher);
    }

    private static byte[] getByteStreamFromMessage(String message)
    {
        //TODO understand Code because i dont ^^ see page 7 hand written doku
        UUID id = UUID.randomUUID();
        Messages.Text.Builder builder = Messages.Text.newBuilder();
        builder.setContent(message);
        byte [] content = Messages.GenericMessage.newBuilder().setMessageId(id.toString()).setText(builder).build().toByteArray();

        return content;
    }

    private static PreKey toCryptoPreKey (Prekey old){
        return new PreKey(old.getID(), Base64.getDecoder().decode(old.getKey()));
    }

    public static void cleanUp ()
    {
        CryptoFactory.closeBox();
    }
}
