package com.projects.potla.fcrypt.utils;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

@Service
public class Encryption {

    /*
     * Encrypt all files in a directory from user path input
     * */
    public String encryptFilesInDirectory(Key secretKey, Cipher cipher, Path dirPath) {
        File dir = dirPath.toFile();
        File[] files = dir.listFiles();

        if (files == null) {
            return "Error accessing directory or directory is empty!";
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String result = encryptFilesInDirectory(secretKey, cipher, file.toPath());
                if (!result.equals("Files encrypted!")) {
                    return result;
                }
            } else {
                String result = encryptSingleFile(secretKey, cipher, file.toPath());
                if (!result.equals("File encrypted!")) {
                    return result;
                }
            }
        }

        return "Files encrypted!";
    }

    /*
     * Encrypt a single file from user path input
     * */
    public String encryptSingleFile(Key secretKey, Cipher cipher, Path filePath) {
        if (filePath.toString().endsWith(".enc")) {
            return "File already encrypted!";
        }

        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(filePath);
        } catch (Exception e) {
            return "File not found or error reading file!";
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(fileBytes);
            Path encryptedFilePath = Paths.get(filePath + ".enc");
            Files.write(encryptedFilePath, encryptedBytes);
            Files.delete(filePath);
            return "File encrypted!";
        } catch (Exception _) {
            return "Error while encrypting file!";
        }
    }

}
