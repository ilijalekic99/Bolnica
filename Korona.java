import java.util.Random;

public class Korona extends ZarazbaBolest{
    private boolean pokazujeSimptome;
    private static Random random = new Random();

    public Korona(int duzinaBolesti, boolean pokazujeSimptome){
        super(duzinaBolesti);
        this.pokazujeSimptome = pokazujeSimptome;
    }

    public Korona(Korona k){
        this(k.getDuzinaBolesti(),k.pokazujeSimptome);
    }

    public boolean isPokazujeSimptome() {
        return pokazujeSimptome;
    }

    public boolean test(){
        double r = random.nextDouble();
        if(pokazujeSimptome){
            return r < 0.8;
        }
        else{
            return r < 0.4;
        }
    }

    @Override
    public String toString() {
        return "Korona traje " + super.getDuzinaBolesti() + " dana " + (pokazujeSimptome ? "sa simptomima" : "bez simptoma");
    }
}
