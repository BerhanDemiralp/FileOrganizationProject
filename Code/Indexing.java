package Code;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
        Map<Character, String> invalidCharMap = new HashMap<>();
        invalidCharMap.put('\\', "backslash");
        invalidCharMap.put('/', "slash");
        invalidCharMap.put(':', "colon");
        invalidCharMap.put('*', "asterisk");
        invalidCharMap.put('?', "question mark");
        invalidCharMap.put('"', "quotation mark");
        invalidCharMap.put('<', "less than");
        invalidCharMap.put('>', "greater than");
        invalidCharMap.put('|', "pipe");

        String sourceFolderPath = "src/Index";

        File sourceFolder = new File(sourceFolderPath);
        String key;
        char _key = password.charAt(0);
        if(invalidCharMap.containsKey(_key)){
            key = invalidCharMap.get(_key);
        }
        else {
            key = String.valueOf(_key);
        }

        if(sourceFolder.isDirectory()){
            File[] folders = sourceFolder.listFiles();
            for (File folder : folders) {
                String foldername = folder.getName() ;
                System.out.println(foldername);
                Boolean folderbool = folder.getName().trim().equals(key);
                if (folder.getName().trim().equals(key) && folder.isFile()) {
                    writePasswordToFile(folder,password);
                }
            }
            String newFolderPath = sourceFolder + File.separator + key;
            File _folder =  new File(newFolderPath);
            writePasswordToFile(_folder,password);
        }
        else {
            String newFolderPath = sourceFolder + File.separator + key;
            File folder =  new File(newFolderPath);
          //  String newFilePath = folder.getPath() + File.separator + key + ".txt";
          //  File txtFile = new File(newFilePath);
            writePasswordToFile(folder,password);
        }
    }
    public void writePasswordToFile(File file, String password){
        String filePath = file.getPath();
        new File(filePath).mkdirs();
        String content = password; // Password|MD5Hash|Sha128|Sha256|source_file_name
        String fileName = file.getName(); // Dosya adını belirtin

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+File.separator+fileName,true))) {
            writer.write(content+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

