package unimessenger.abstraction.encryption.WireCrypto;

import com.wire.bots.cryptobox.PreKey;

import java.util.Base64;

public class Prekey {
    private int ID;
    private String key;

    Prekey (int ID, String key){
        this.ID = ID;
        this.key = key;
    }
    //Generate the Prekey in our format from a com.wire.bots.cryptobox.PreKey
    Prekey(PreKey pk){
        this.ID = pk.id;
        this.key = Base64.getEncoder().encodeToString(pk.data);
    }

    public void setKey(String key){
        this.key = key;
    }

    public int getID(){
        return  ID;
    }

    public String getKey(){
        return key;
    }


}
