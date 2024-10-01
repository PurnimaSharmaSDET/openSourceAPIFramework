package com.opensourceFramework.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class ReadFile {
    public String readFileAsString(String fileName){
        BufferedReader br=null;
        FileReader fr=null;
        String jsonString="";
        try {
            String sCurrentLine;
            String path1= Paths.get(".").toAbsolutePath().normalize().toString();
            br=new BufferedReader(new FileReader(path1+"/src/main/resources/"+fileName));
            while ((sCurrentLine= br.readLine())!=null){
                jsonString+=sCurrentLine;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(br!=null)
                    br.close();
                if (fr!=null)
                    fr.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return jsonString;
    }
}
