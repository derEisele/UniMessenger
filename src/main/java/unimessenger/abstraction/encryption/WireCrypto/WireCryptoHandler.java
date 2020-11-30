package unimessenger.abstraction.encryption.WireCrypto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.waz.model.Messages;
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

    public static String encrypt(String userID, String clientID, Prekey pk, String msg)
    {
        if(box == null) box = CryptoFactory.getCryptoInstance();

        byte[] content = getByteStreamFromMessage(msg);
        String id = generateSeassionID(userID, clientID);
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

    public static String decrypt(UUID from, String sender, String text)
    {
        if(box == null) box = CryptoFactory.getCryptoInstance();

        String ret = "";
        byte[] dec;
        String sID = generateSeassionID(from.toString(), sender);

        try
        {
            dec = box.decrypt(sID, Base64.getDecoder().decode(text));
        } catch(CryptoException ignored)
        {
            Outputs.create("Decrypt CryptoException", "WireCryptoHandler").always().ERROR();
            dec = new byte[1];
        }
        try
        {
            Messages.GenericMessage m = Messages.GenericMessage.parseFrom(dec);
            ret = m.getText().getContent();
        } catch(InvalidProtocolBufferException ignored)
        {
            Outputs.create("Invalid Protocol Buffer", "WireCryptoHandler").debug().ERROR().print();
        }
        return ret;
    }

    private static byte[] getByteStreamFromMessage(String message)
    {
        //TODO understand Code because i dont ^^ see page 7 hand written doku
        UUID id = UUID.randomUUID();
        Messages.Text.Builder builder = Messages.Text.newBuilder();
        builder.setContent(message);

        return Messages.GenericMessage.newBuilder().setMessageId(id.toString()).setText(builder).build().toByteArray();
    }

    private static PreKey toCryptoPreKey(Prekey old)
    {
        return new PreKey(old.getID(), Base64.getDecoder().decode(old.getKey()));
    }

    private static String generateSeassionID(String userID, String clientID)
    {
        return String.format("%s_%s", userID, clientID);
    }

    public static void cleanUp()
    {
        CryptoFactory.closeBox();
    }
}
