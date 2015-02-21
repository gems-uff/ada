/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.service.mining;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wallace
 */
public class TemporaryFileManager {

    private HashMap<String, String> hashMap;
    private int fileNumber;
    private static final String path = System.getProperty("user.home") + "/.archd/temp_mining_files/";
    private static final String pathMapToMethods = System.getProperty("user.home") + "/.archd/methods_to_number.txt";

    TemporaryFileManager() {
        hashMap = new HashMap();
        fileNumber = 0;
        deleteFiles();

    }

    private void deleteFiles() {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        } else {
            file.mkdirs();
        }
    }

    public String[] getFilePath(String methodSignature) {
        String[] str = new String[2];
        String fileNumberStr = hashMap.get(methodSignature);
        if (fileNumberStr == null) {
            fileNumberStr = String.valueOf(fileNumber);
            fileNumber++;
            try {
                FileWriter fw = new FileWriter(pathMapToMethods, true); //the true will append the new data
                fw.write(fileNumberStr+" "+methodSignature + "\n");//appends the string to the file
                fw.close();
                hashMap.put(methodSignature, fileNumberStr);
            } catch (Exception e) {
            }

        }
        str[0] = path + "sequence_file_" + fileNumberStr + ".txt";
        str[1] = path + "sequence_file_revision_" + fileNumberStr + ".txt";;
        return str;
    }

    public List<String> getPathFiles() {
        List<String> files = new LinkedList();

        for (int i = 0; i < fileNumber; i++) {
            String filePath = path + "sequence_file_" + i + ".txt";
            files.add(filePath);
            //File file = new File(filePath);
            //file.m
        }

        return files;
    }

    public int getNumberOfFiles() {
        return fileNumber;
    }
}
