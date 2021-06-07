import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Bolnica {
    List <Pacijent> cekaonica = new LinkedList<>();
    List <Pacijent> izolacija = new LinkedList<>();
    List <Pacijent> zdravi = new LinkedList<>();

    public List<Pacijent> getCekaonica() {
        return cekaonica;
    }

    public List<Pacijent> getIzolacija() {
        return izolacija;
    }

    public List<Pacijent> getZdravi() {
        return zdravi;
    }

    public void setCekaonica(List<Pacijent> cekaonica) {
        this.cekaonica = cekaonica;
    }

    public void setIzolacija(List<Pacijent> izolacija) {
        this.izolacija = izolacija;
    }

    public void setZdravi(List<Pacijent> zdravi) {
        this.zdravi = zdravi;
    }

    public void ucitaj(){
        int id = 0;
        try {
            List<String> linije = Files.readAllLines(Paths.get("pacijenti.txt"));
            for(String linija: linije){
                String[] tokeni = linija.split(",");
                String ime = tokeni[0].trim();
                String prezime = tokeni[1].trim();
                String tip = tokeni[2].trim();
                int duzina = Integer.parseInt(tokeni[3].trim());

                ZarazbaBolest bolest;

                if(tip.equals("k")){
                    bolest = new Korona(duzina, tokeni[4].trim().equals("da"));
                }
                else{
                    bolest = new Grip((duzina));
                }
                Pacijent p = new Pacijent(ime,prezime,id++,bolest);
                cekaonica.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sledeci(){
        Pacijent p = cekaonica.remove(0);

        if(p.getDijagnoza() instanceof Korona && ((Korona)p.getDijagnoza()).test() | p.getDijagnoza() instanceof Grip){
            p.setZarazen(false);
            izolacija.add(p);
        }
        else{
            p.setDuzinaLecenja(0);
            p.getDijagnoza().setDuzinaBolesti(0);
            zdravi.add(p);
        }
    }

    public void unesi(){
        List<String> podaci = new LinkedList<>();
        podaci.add("U cekaonici: ");
        for(Pacijent p : cekaonica){
            podaci.add(p.getIdKnjizice() + " " + p.getIme() + " " + p.getPrezime());
        }
        podaci.add("U izolaciji: ");
        for(Pacijent p : izolacija){
            podaci.add(p.getIdKnjizice() + " " + p.getIme() + " " + p.getPrezime());
        }
        podaci.add("Zdravi: ");
        for(Pacijent p : zdravi){
            podaci.add(p.getIdKnjizice() + " " + p.getIme() + " " + p.getPrezime());
        }
        try {
            Files.write(Paths.get("izvestaj.txt"),podaci, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}