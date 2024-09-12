package com.projects.potla.fcrypt.utils;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

@Service
public class Decryption {

    /*
     * Decrypt all files in a directory from user path input
     * */
    public String decryptFilesInDirectory(Key secretKey, Cipher cipher, Path dirPath) {
        File dir = dirPath.toFile();
        File[] files = dir.listFiles();

        if (files == null) {
            return "Error accessing directory or directory is empty!";
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String result = decryptFilesInDirectory(secretKey, cipher, file.toPath());
                if (!result.equals("Files decrypted!")) {
                    return result;
                }
            } else {
                String result = decryptSingleFile(secretKey, cipher, file.toPath());
                if (!result.equals("File decrypted!")) {
                    return result;
                }
            }
        }

        return "Files decrypted!";
    }

    /*
     * Decrypt a single file from user path input
     * */
    public String decryptSingleFile(Key secretKey, Cipher cipher, Path filePath) {
        if (!filePath.toString().endsWith(".enc")) {
            return "File not encrypted!";
        }

        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(filePath);
        } catch (Exception e) {
            return "File not found or error reading file!";
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(fileBytes);
            Path decryptedFilePath = Paths.get(filePath.toString().replace(".enc", ""));
            Files.write(decryptedFilePath, decryptedBytes);
            Files.delete(filePath);
            return "File decrypted!";
        } catch (Exception _) {
            return "Error decrypting file!";
        }
    }
}
