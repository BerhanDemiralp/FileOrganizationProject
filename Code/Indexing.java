package Code;

import java.io.*;

public class Indexing {

    public void process(){

    }

    public void getUnprocessedPasswords() {
        // Klasör yolunu belirtin
        String sourceFolderPath = "src/Unprocessed-Passwords";

        // Klasörü temsil eden File nesnesini oluşturun
        File sourceFolder = new File(sourceFolderPath);

        // Klasördeki dosyaları kontrol edin
        if (sourceFolder.isDirectory()) {
            // Klasördeki her dosya için işlem yapın
            File[] files = sourceFolder.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            getFileWithPassword(line.trim());
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
    }

    public void getFileWithPassword(String password){

        String sourceFolderPath = "src/Index";

        File sourceFolder = new File(sourceFolderPath);

        String key = String.valueOf(password.charAt(0));

        if(sourceFolder.isDirectory()){
            File[] folders = sourceFolder.listFiles();
            for (File folder : folders) {
                if (folder.getName().trim().equals(key) && folder.isFile()) {
                    String newFilePath = folder.getPath() + File.separator + key + ".txt";
                    File txtFile = new File(newFilePath);
                    writePasswordToFile(txtFile,password);
                }
            }
        }
        else {
            String newFolderPath = sourceFolder + File.separator + key;
            File folder =  new File(newFolderPath);
            String newFilePath = folder.getPath() + File.separator + key + ".txt";
            File txtFile = new File(newFilePath);
            writePasswordToFile(txtFile,password);
        }
    }
    public void writePasswordToFile(File file, String password){
        String filePath = file.getPath();
        String content = password; // Password|MD5Hash|Sha128|Sha256|source_file_name

        try (FileWriter writer = new FileWriter(filePath,true)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

