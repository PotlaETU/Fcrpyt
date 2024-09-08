package com.projects.potla.fcrypt;

import com.projects.potla.fcrypt.cli.FcryptCLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class FcryptApplicationTests {

    @Autowired
    private FcryptCLI fcryptCLI;

    private Path testFilePath;
    private Path encryptedFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = Path.of("src/test/resources/test.txt");
        encryptedFilePath = Path.of("src/test/resources/test.txt.enc");
    }

    @Test
    void testEncrypt() {
        try {
            String result = fcryptCLI.encryptFile(testFilePath.toString());
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

}
