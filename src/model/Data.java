package model;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;

public class Data {
    public static boolean writeToJsonProizvod(ArrayList<Proizvod> list,String path){
        try {
            Writer writer=new FileWriter(path);
            Gson gson = new Gson();
            gson.toJson(list,writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeToJsonKategorija(ArrayList<Kategorija> list,String path){
        try {
            Writer writer=new FileWriter(path);
            Gson gson = new Gson();
            gson.toJson(list,writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Proizvod> readFromJsonProizvod(String path){
        if(!new File(path).exists()){
            System.out.println("Ne postoji.");
            return new ArrayList<>();
        }
        try {
            JsonReader reader=new JsonReader(new FileReader(path));
            Gson gson = new Gson();
            return gson.fromJson(reader,new TypeToken<ArrayList<Proizvod>>(){}.getType());
        } catch (FileNotFoundException e) {
            System.out.println("Prazno");
            e.printStackTrace();
            return new ArrayList<Proizvod>();
        }
    }

    public static ArrayList<Kategorija> readFromJsonKategorija(String path){
        if(!new File(path).exists()){
            System.out.println("Ne postoji.");
            return new ArrayList<>();
        }
        try {
            JsonReader reader=new JsonReader(new FileReader(path));
            Gson gson = new Gson();
            return gson.fromJson(reader,new TypeToken<ArrayList<Kategorija>>(){}.getType());
        } catch (FileNotFoundException e) {
            System.out.println("Prazno");
            e.printStackTrace();
            return new ArrayList<Kategorija>();
        }
    }
}
