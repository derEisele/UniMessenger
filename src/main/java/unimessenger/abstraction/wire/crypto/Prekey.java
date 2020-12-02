package unimessenger.abstraction.wire.crypto;

import com.wire.bots.cryptobox.PreKey;

import java.util.Base64;

public class Prekey
{
    private final int ID;
    private final String key;

    public Prekey(int ID, String key)
    {
        this.ID = ID;
        this.key = key;
    }

    Prekey(PreKey pk)
    {
        this.ID = pk.id;
        this.key = Base64.getEncoder().encodeToString(pk.data);
    }

    public int getID()
    {
        return ID;
    }

    public String getKey()
    {
        return key;
    }
}
