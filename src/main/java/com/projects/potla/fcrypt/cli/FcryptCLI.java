package com.projects.potla.fcrypt.cli;

import com.projects.potla.fcrypt.utils.Decryption;
import com.projects.potla.fcrypt.utils.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

@ShellComponent
public class FcryptCLI {

    private static final String KEY_FILE = "secret.key";
    private final Encryption encryption;
    private final Decryption decryption;
    private final Cipher cipher;
    private final Key secretKey;

    @Autowired
    public FcryptCLI(Encryption encryption, Decryption decryption) throws Exception {
        this.encryption = encryption;
        this.decryption = decryption;
        this.cipher = Cipher.getInstance("AES");
        this.secretKey = loadOrGenerateKey();
    }

    private Key loadOrGenerateKey() throws Exception {
        Path keyPath = Paths.get(KEY_FILE);
        if (Files.exists(keyPath)) {
            byte[] keyBytes = Files.readAllBytes(keyPath);
            return new SecretKeySpec(keyBytes, "AES");
        } else {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey newKey = keyGenerator.generateKey();
            Files.write(keyPath, newKey.getEncoded());
            return newKey;
        }
    }

    @ShellMethod(value = "Encrypt a file", key = "enc")
    public String encryption(@ShellOption String filePath) {
        Path path = Paths.get(filePath);
        if (Files.isDirectory(path)) {
            return encryption.encryptFilesInDirectory(secretKey, cipher, path);
        } else {
            return encryption.encryptSingleFile(secretKey, cipher, path);
        }
    }

    @ShellMethod(value = "Decrypt a file", key = "dec")
    public String decryptFile(@ShellOption String filePathEncrypted) {
        Path path = Paths.get(filePathEncrypted);
        if (Files.isDirectory(path)) {
            return decryption.decryptFilesInDirectory(secretKey, cipher, path);
        } else {
            return decryption.decryptSingleFile(secretKey, cipher, path);
        }
    }

}
