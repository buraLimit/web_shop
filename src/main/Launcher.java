package main;

import com.google.gson.Gson;
import model.Data;
import model.Kategorija;
import model.Proizvod;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import static spark.Spark.*;
import static spark.Spark.port;
import static spark.Spark.staticFiles;


public class Launcher {
    public static void main(String[] args) {
        staticFiles.location("/public");
        port(5000);
        String proizvodiPath = "proizvodi.json";
        String kategorijePath = "kategorija.json";

        HashMap<String,Object> polja=new HashMap<>();

       ArrayList<Proizvod> proizvodi = Data.readFromJsonProizvod(proizvodiPath);
       ArrayList<Kategorija> kategorije =  Data.readFromJsonKategorija(kategorijePath);

        for(int i=0;i< proizvodi.size();i++){
            proizvodi.get(i).setId(i+1);
        }
        for (int j=0;j < kategorije.size();j++){
            kategorije.get(j).setId(j+1);
        }

        get("/",(request, response) -> {
            polja.put("kategorije",kategorije);
            polja.put("proizvodi",proizvodi);
            return new ModelAndView(polja,"index.hbs");
        }, new HandlebarsTemplateEngine());


        path("/api",() ->{
            //SORTIRANJE U GLAVNOM MENIJU
            post("/sortiranje",(request, response) -> {
                response.type("application/json");
                String sort = request.queryParams("sort");
                System.out.println(sort);
                ArrayList<Proizvod> proizvodiS =Data.readFromJsonProizvod(proizvodiPath);

                switch (sort){
                    case "nazivAsc":
                        Comparator <Proizvod> komparatorPoNazivu = Comparator.comparing(Proizvod::getNaziv);
                        proizvodiS.sort(komparatorPoNazivu);
                        break;
                    case "nazivDesc":
                        Comparator <Proizvod> komparatorPoNazivuR = Comparator.comparing(Proizvod::getNaziv,Comparator.reverseOrder());
                        proizvodiS.sort(komparatorPoNazivuR);
                        break;
                    case "cenaAsc":
                        Comparator <Proizvod> komparatorPoCeni = Comparator.comparing(Proizvod::getCena);
                        proizvodiS.sort(komparatorPoCeni);
                        break;
                    case "cenaDesc":
                        Comparator <Proizvod> komparatorPoCeniR = Comparator.comparing(Proizvod::getCena,Comparator.reverseOrder());
                        proizvodiS.sort(komparatorPoCeniR);
                        break;
                    default:
                        proizvodiS =  Data.readFromJsonProizvod(proizvodiPath);
                }


                Gson gson =new Gson();
                return gson.toJson(proizvodiS);
            });

            //PRETRAGA U GLAVNOM MENIJU
            post("/pretraga",(request, response) -> {
               response.type("application/json");
               String pojam = request.queryParams("pojam");
      //        ArrayList <Proizvod> proizvodiS = (ArrayList<Proizvod>) polja.get("proizvodi");
               ArrayList <Proizvod> pretrazeni = new ArrayList<>();
                System.out.println(proizvodi);

                for(Proizvod p:proizvodi){
                    if (p.getNaziv().toLowerCase().contains(pojam.toLowerCase()) || p.getOpis().toLowerCase().contains(pojam.toLowerCase()))
                        pretrazeni.add(p);
                }

                Gson gson =new Gson();
                return gson.toJson(pretrazeni);
            });

            //IZMENA PROIZVODA
            post("/promeniProizvod", (request, response) -> {
                response.type("application/json");
                int id = Integer.parseInt(request.queryParams("id"));
                boolean cekirano = Boolean.parseBoolean(request.queryParams("visible"));
                String naziv = request.queryParams("naziv");
                String opis = request.queryParams("opis");
                double cena = Double.parseDouble(request.queryParams("cena"));
                String src = request.queryParams("src");
         //       ArrayList <Proizvod> proizvodi = Data.readFromJsonProizvod(proizvodiPath);

                for (Proizvod p :proizvodi){
                    System.out.println(p.getId() +" " + p.isVidljivost());
                    if(p.getId()==id){
                        p.setSrc(src);
                        p.setNaziv(naziv);
                        p.setOpis(opis);
                        p.setVidljivost(cekirano);
                        p.setCena(cena);
                        break;
                    }
                }

                Data.writeToJsonProizvod(proizvodi,proizvodiPath);
                Gson gson=new Gson();
                return gson.toJson("Snimljenje izmene");
            });

            //IZMENA KATEGORIJE
            post("/promeniKategoriju",(request, response) -> {
                response.type("application/json");
                int id = Integer.parseInt(request.queryParams("id"));
                String naziv = request.queryParams("naziv");
                boolean cekirano = Boolean.parseBoolean(request.queryParams("visible"));

            //    System.out.println(cekirano + " OVO JE ZA KATEGORIJU");

                boolean uUpotrebi = false;
                for (Proizvod p: proizvodi){
                    if (p.getKategorija()==id) {
                        uUpotrebi = true;
                        break;
                    }
                }

                if(cekirano==false && uUpotrebi==true){
                    Gson gson=new Gson();
                    return gson.toJson("Ne mozete ukloniti kategoriju koja je u upotrebi");
                }
                else {
                    for (Kategorija k : kategorije) {
                        if (k.getId() == id) {
                            k.setVidljivost(cekirano);
                            k.setNaziv(naziv);
                            break;
                        }
                    }

                    Data.writeToJsonKategorija(kategorije, kategorijePath);
                    Gson gson = new Gson();
                    return gson.toJson("Snimljenje izmene");

                }
            });

            post("/dodajProizvod",(request, response) -> {
                response.type("application/json");
                String src = request.queryParams("src");
                String naziv = request.queryParams("naziv");
                String opis = request.queryParams("opis");
                double cena = Double.parseDouble(request.queryParams("cena"));
                int kategorija = Integer.parseInt(request.queryParams("kategorija"));
                boolean visible = Boolean.parseBoolean(request.queryParams("visible"));
                int id = proizvodi.size()+1;

                Proizvod p = new Proizvod(naziv,opis,cena,src,kategorija,visible);
                p.setId(id);

                proizvodi.add(p);
                Data.writeToJsonProizvod(proizvodi,proizvodiPath);
                Gson gson=new Gson();
                return gson.toJson("Proizvod uspesno dodat!");
            });

            post("/dodajKategoriju",(request, response) -> {
                String naziv = request.queryParams("naziv");
                boolean visible = Boolean.parseBoolean(request.queryParams("visible"));
                int id = kategorije.size()+1;

                Kategorija k = new Kategorija(naziv,visible);
                k.setId(id);

                kategorije.add(k);
                Data.writeToJsonKategorija(kategorije,kategorijePath);
                Gson gson=new Gson();
                return gson.toJson("Kategorija uspesno dodat!");
            });

        });

        //STRANICA ZA PRIJAVU
        get("/prijaviSe",(request, response) -> {
            if(request.session().attribute("korisnik")=="admin"){
                response.redirect("/adminPanel");

            }
            return new ModelAndView(null,"loginForma.hbs");
        },new HandlebarsTemplateEngine());

        //METODA KOJA SE POZIVA KLIKOM NA POTVRDI U PRIJAVI
        post("/login",(request, response) -> {
            String korisnik = request.queryParams("korisnik");
            String lozinka = request.queryParams("lozinka");


            if (korisnik.equals("admin") && lozinka.equals("admin")){
                request.session().attribute("korisnik","admin");
                response.redirect("/adminPanel");
                return null;
            }

            polja.put("poruka","Neispravno korisnicko ime ili lozinka");
            return new ModelAndView(polja,"loginForma.hbs");
        }, new HandlebarsTemplateEngine());


        //POZIVA SE AKO JE POZOVE PRETHODNA METODA, AKO SU PARAMETRI ODGOVARAJUCI
        get("/adminPanel",(request, response) -> {
            //KADA KLIKNEMO NA LOGOUT POSTAVI SE KORISNIK NA NULL I ODJAVLJUJEMO SE
            if (request.session().attribute("korisnik")==null){
                response.redirect("/prijaviSe");
            }
            polja.put("proizvodi",proizvodi);
            polja.put("kategorije",kategorije);
        //    polja.put("korisnickoIme",request.session().attribute("korisnik").toString());
            return new ModelAndView(polja,"adminPanel.hbs");

        },new HandlebarsTemplateEngine());

        get("/logout",(request, response) -> {
            request.session().removeAttribute("korisnik");
            response.redirect("/prijaviSe");
            return null;
        },new HandlebarsTemplateEngine());







    }
}
















