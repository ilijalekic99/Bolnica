public class Pacijent implements Izleciv,Comparable<Pacijent>{
    private String ime,prezime;
    private int idKnjizice;
    private int duzinaLecenja;
    private boolean zarazen = false;
    private ZarazbaBolest dijagnoza;

    public Pacijent(String ime, String prezime, int idKnjizice, ZarazbaBolest dijagnoza) {
        this.ime = ime;
        this.prezime = prezime;
        this.idKnjizice = idKnjizice;
        this.duzinaLecenja = 0;
        this.zarazen = false;
        this.dijagnoza = dijagnoza;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public int getIdKnjizice() {
        return idKnjizice;
    }

    public int getDuzinaLecenja() {
        return duzinaLecenja;
    }

    public boolean isZarazen() {
        return zarazen;
    }

    public ZarazbaBolest getDijagnoza() {
        return dijagnoza;
    }

    public void setDuzinaLecenja(int duzinaLecenja) {
        this.duzinaLecenja = duzinaLecenja;
    }

    public void setZarazen(boolean zarazen) {
        this.zarazen = zarazen;
    }

    public void setDijagnoza(ZarazbaBolest dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    @Override
    public void leci(int brojDana) {
        duzinaLecenja += brojDana;
    }

    @Override
    public boolean izlecen() {
        return duzinaLecenja >= dijagnoza.getDuzinaBolesti();
    }

    @Override
    public String toString() {
        int vremeDoIzlecenja= (dijagnoza.getDuzinaBolesti() - duzinaLecenja) > 0 ? (dijagnoza.getDuzinaBolesti() - duzinaLecenja) : 0;
        return "Id: " + idKnjizice + " " + ime + " " + prezime + (izlecen() ? "" : "boluje od: " + dijagnoza + "vreme do izlecenja: " +
                vremeDoIzlecenja);
    }

    @Override
    public int compareTo(Pacijent o) {
        if(dijagnoza instanceof Korona && o.getDijagnoza() instanceof Grip)
            return -1;
        else if(dijagnoza instanceof Korona && o.getDijagnoza() instanceof Korona ||
        dijagnoza instanceof Grip && o.getDijagnoza() instanceof Grip){
            return Integer.compare(o.getDijagnoza().getDuzinaBolesti(), dijagnoza.getDuzinaBolesti());

        }
        else if(dijagnoza instanceof Grip && o.getDijagnoza() instanceof Korona)
            return 1;
        else
            return 0;
    }
}
