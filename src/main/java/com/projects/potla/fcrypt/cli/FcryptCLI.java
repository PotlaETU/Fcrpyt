package com.projects.potla.fcrypt.cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

@ShellComponent
public class FcryptCLI {

    private final Cipher cipher;
    private final Key secretKey;

    public FcryptCLI() throws Exception {
        this.cipher = Cipher.getInstance("AES");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        this.secretKey = keyGenerator.generateKey();
    }

    @ShellMethod(value = "Encrypt a file", key = "fcrypt-encrypt")
    public String encryptFile(@ShellOption String filePath) {
        byte[] file;
        Path path = Paths.get(filePath);
        try{
            file = Files.readAllBytes(path);
        } catch (Exception _) {
            return "File not found !";
        }
        try{
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encyrptedBytes = cipher.doFinal(file);
            Files.write(Paths.get(filePath + ".enc"), encyrptedBytes);
            Files.delete(path);
            return "File encrypted !";
        } catch (Exception _) {
            return "Error while encrypting file";
        }
    }

    @ShellMethod(value = "Decrypt a file", key = "fcrypt-decrypt")
    public String decryptFile(@ShellOption String filePathEncrypted) {
        if (!filePathEncrypted.endsWith(".enc")){
            return "File not encrypted !";
        }
        byte[] encryptedFile;
        Path path = Paths.get(filePathEncrypted);
        try{
            encryptedFile = Files.readAllBytes(path);
        } catch (Exception _) {
            return "File not found !";
        }
        try{
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedFile);
            Files.write(Paths.get(filePathEncrypted.replace(".enc", "")), decryptedBytes);
            Files.delete(path);
            return "File encrypted !";
        } catch (Exception _) {
            return "Error while encrypting file";
        }
    }

}
