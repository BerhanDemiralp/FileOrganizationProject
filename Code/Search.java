package Code;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Search {
    Indexing index = new Indexing();

    public void search(String password){

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
        invalidCharMap.put('.', "dot");

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
                String folderName = folder.getName() ;
                boolean passwordExists = false;
                if (folder.getName().trim().equals(key.trim())) {
                    String folderPath = folder.getPath();


                    try (BufferedReader reader = new BufferedReader(new FileReader(folderPath+File.separator+folderName)))
                    {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            if (line.equals(index.generateContent(password))) {
                                passwordExists = true;
                                show(password);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!passwordExists) {
                        System.out.println("don't exist");
                        System.out.println(password +" added in passwordss");
                        String content = index.generateContent(password);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath + File.separator + folderName, true))) {
                            writer.write(content + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
//        else {
//            String newFolderPath = sourceFolder + File.separator + key;
//            File folder =  new File(newFolderPath);
//            //  String newFilePath = folder.getPath() + File.separator + key + ".txt";
//            //  File txtFile = new File(newFilePath);
//            writePasswordToFile(folder,password);
//        }
    }

    public void show(String password){
        System.out.println("exist");
        String content = index.generateContent(password);
        System.out.println("Searched password : "+content);
    }



}
