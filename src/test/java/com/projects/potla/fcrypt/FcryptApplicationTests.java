package com.projects.potla.fcrypt;

import com.projects.potla.fcrypt.cli.FcryptCLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FcryptApplicationTests {

    @Autowired
    private FcryptCLI fcryptCLI;

    private Path testFilePath;
    private Path encryptedFilePath;
    private Path testDirectoryPath;

    @BeforeEach
    void setUp() {
        testFilePath = Path.of("src/test/resources/legoat.jpg");
        encryptedFilePath = Path.of("src/test/resources/legoat.jpg.enc");
        testDirectoryPath = Path.of("src/test/resources/testFolder/");
    }

    @Test
    void testEncrypt() {
        try {
            String result = fcryptCLI.encryption(testFilePath.toString());
            System.out.println(result);

            assertTrue(Files.exists(encryptedFilePath), "Encrypted file should be created.");

        } catch (Exception e) {
            fail("Exception occurred during encryption test: " + e.getMessage());
        }
    }

    @Test
    void testDecrypt() {
        try {
            String result = fcryptCLI.decryptFile(encryptedFilePath.toString());
            System.out.println(result);

            assertTrue(Files.exists(testFilePath), "Decrypted file should be created.");

        } catch (Exception e) {
            fail("Exception occurred during decryption test: " + e.getMessage());
        }
    }

    @Test
    void testEncryptDirectory() {
        try {
            fcryptCLI.encryption(testDirectoryPath.toString());
            Files.walk(testDirectoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> assertTrue(file.getFileName().toString().endsWith(".enc"),
                            "Encrypted files should be created: " + file)
                    );
        } catch (Exception e) {
            fail("Exception occurred during directory encryption test: " + e.getMessage());
        }
    }

    @Test
    void testDecryptDirectory() {
        try {
            fcryptCLI.decryptFile(testDirectoryPath.toString());

            Files.walk(testDirectoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> assertFalse(file.getFileName().toString().endsWith(".enc"),
                            "Decrypted files should be created: " + file)
                    );

        } catch (Exception e) {
            fail("Exception occurred during directory decryption test: " + e.getMessage());
        }
    }

}
