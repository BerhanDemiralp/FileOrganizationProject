package Code;

import java.io.*;

public class Indexing {

    public void process(){
        String passwords = getUnprocessedPasswords();
        File folder = getFolderWithPassword(passwords);
    }

    public String getUnprocessedPasswords() {
        // Klasör yolunu belirtin
        String folderPath = "src/Unprocessed-Passwords";

        // Klasörü temsil eden File nesnesini oluşturun
        File folder = new File(folderPath);

        // Klasördeki dosyaları kontrol edin
        if (folder.isDirectory()) {
            // Klasördeki her dosya için işlem yapın
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            return line.trim();
                        }
                        System.out.println();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Belirtilen yol bir klasörü temsil etmiyor.");
        }
        return null;
    }

    public File getFolderWithPassword(String password){

        String folderPath = "src/Index";

        File folder = new File(folderPath);

        String key = String.valueOf(password.charAt(0));

        String newPath="src/key";

        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().trim().equals(key) && file.isFile()) {
                    return file;
                }
            }
        }
        else {
            return new File(newPath);
        }
        return null;
    }
    public void writePasswordToFile(File folder, String password){

    }
}

