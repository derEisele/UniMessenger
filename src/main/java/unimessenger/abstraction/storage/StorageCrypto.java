package unimessenger.abstraction.storage;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.UnrecoverableKeyException;

public class StorageCrypto {
    private SecretKey key;
    private Cipher cipher;
    private KeyStore ks;
    private static String passphrase;
    private static final String FILEPATH_KEYSTORE = WireStorage.storageDirectory + "/KeyStore";
    private static final String FILEPATH_DATASTORE = WireStorage.storageDirectory + "/DataStore";
    private static final String ALIAS_KEY_ENRYPTION = "KeyUserCookie";

    public StorageCrypto() throws UnrecoverableKeyException{

        //Trying to load the Keystore from the disc, if the file is not found, its created

        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream(FILEPATH_KEYSTORE), passphrase.toCharArray());
            key =(SecretKey) ks.getKey(ALIAS_KEY_ENRYPTION, passphrase.toCharArray());
        } catch (FileNotFoundException e) {
            //If no KeyVault is found, a new one is generated
            try {
                ks.load(null, passphrase.toCharArray());
                FileOutputStream fos = new FileOutputStream(FILEPATH_KEYSTORE);

                //Generating a key to be stored
                key = KeyGenerator.getInstance("AES").generateKey();

                //Storing the key
                KeyStore.SecretKeyEntry secret
                        = new KeyStore.SecretKeyEntry(key);
                KeyStore.ProtectionParameter pwEnc
                        = new KeyStore.PasswordProtection(passphrase.toCharArray());

                ks.setEntry(ALIAS_KEY_ENRYPTION, secret, pwEnc);

                ks.store(fos, passphrase.toCharArray());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        //Apearently, UnreacoverableKeyExceptions are IOExceptions
        } catch (IOException e) {
            throw
                    new UnrecoverableKeyException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Getting the cipher according to the specified Advanced encryption standard, cipher block chaining, Padding standard
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setPassphrase(String passp){
        if(!passp.equalsIgnoreCase(""))
        passphrase = passp;
        else passphrase = "a";
    }

    public void encrypt(String content) throws InvalidKeyException, IOException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();

        try (FileOutputStream fileOut = new FileOutputStream(FILEPATH_DATASTORE);
             CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
            fileOut.write(iv);
            cipherOut.write(content.getBytes());
        }
    }

    public String decrypt() throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        String content;

        try (FileInputStream fileIn = new FileInputStream(FILEPATH_DATASTORE)) {
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(fileIv));

            try (
                    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                    InputStreamReader inputReader = new InputStreamReader(cipherIn);
                    BufferedReader reader = new BufferedReader(inputReader)
            ) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
            }

        }
        return content;
    }

    public static void removeAll(){
        //Cleaning all written files
        new File(FILEPATH_DATASTORE).delete();
        new File(FILEPATH_KEYSTORE).delete();
    }
}
