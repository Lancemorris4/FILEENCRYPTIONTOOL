import java.io.File;
import javax.crypto.SecretKey;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            JFileChooser fileChooser = new JFileChooser();

            // ===== ENCRYPTION =====
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();
                File encryptedFile = new File(inputFile.getAbsolutePath() + ".enc");
                File keyFile = new File("aes.key");

                // Generate + save key
                SecretKey key = AESUtil.generateKey();
                AESUtil.saveKey(key, keyFile);

                // Encrypt file
                AESUtil.encryptFile(inputFile, encryptedFile, key);

                System.out.println("Encryption complete! Encrypted File: " + encryptedFile.getAbsolutePath());
                System.out.println("AES key saved to: " + keyFile.getAbsolutePath());
                System.out.println();
            } else {
                System.out.println("No file selected for encryption.");
                return; // Stop if no file to encrypt
            }

            // ===== DECRYPTION =====
            System.out.println("Select the encrypted file to decrypt:");
            int decryptResult = fileChooser.showOpenDialog(null);

            if (decryptResult == JFileChooser.APPROVE_OPTION) {
                File encryptedFileToDecrypt = fileChooser.getSelectedFile();
                File decryptedFile = new File(encryptedFileToDecrypt.getAbsolutePath().replace(".enc", "_decrypted.txt"));

                // Load saved key - make sure this path is correct
                File keyFileForDecryption = new File("aes.key");
                // Debug output
                System.out.println("Looking for key file at: " + keyFileForDecryption.getAbsolutePath());
                if (!keyFileForDecryption.exists()) {
                    System.err.println("Key file not found!");
                    return;
                }
                SecretKey loadedKey = AESUtil.loadKey(keyFileForDecryption);

                // Decrypt file
                AESUtil.decryptFile(encryptedFileToDecrypt, decryptedFile, loadedKey);

                System.out.println("Decryption complete! Decrypted File: " + decryptedFile.getAbsolutePath());
            } else {
                System.out.println("No encrypted file selected for decryption.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred during file encryption/decryption:");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

