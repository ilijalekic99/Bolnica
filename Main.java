import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.text.LabelView;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private static Bolnica bolnica = new Bolnica();
    private static boolean ucitano = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox(10);
        root.setSpacing(10);
        root.setPadding(new Insets(10,10,10,10));

        VBox vbLevi = new VBox(10);
        VBox vbDesni = new VBox(10);

        root.getChildren().addAll(vbLevi,vbDesni);

        Button btUcitaj = new Button("Ucitaj pacijente");
        Button btSledeci = new Button("Sledeci");
        Label lbBrojDana = new Label("Broj dana:");
        TextField tfBrojDana = new TextField();
        Button btUbrzajVreme = new Button("Ubrzaj vreme");

        Label lvGreska = new Label("Greska!");
        lvGreska.setVisible(false);
        lvGreska.setTextFill(Color.RED);
        RadioButton rbSve = new RadioButton("Sve");
        RadioButton rbKorona = new RadioButton("Korona");

        ToggleGroup tg = new ToggleGroup();
        rbSve.setToggleGroup(tg);
        rbKorona.setToggleGroup(tg);
        rbSve.setSelected(true);
        Button btPrikaziStatistike = new Button("Prikazi Statistike");
        Button btUnesi = new Button("Unesi");

        vbLevi.getChildren().addAll(btUcitaj,btSledeci,lbBrojDana,tfBrojDana,btUbrzajVreme,lvGreska,rbSve,rbKorona,btPrikaziStatistike,btUnesi);


        Label lbCekaju = new Label("Cekaju");
        TextArea taCekaju = new TextArea();
        taCekaju.setEditable(false);
        Label lbIzolacija = new Label("U izolaciji:");
        TextArea taIzolacija = new TextArea();
        taIzolacija.setEditable(false);
        Label lbZdravi = new Label("Zdravi:");
        TextArea taZdravi = new TextArea();
        taZdravi.setEditable(false);

        btUcitaj.setOnAction(e -> {
            if(ucitano){
                return;
            }
            else {
                bolnica.ucitaj();
                ucitano = true;
                Collections.sort(bolnica.getCekaonica());
                ispisiListu(bolnica.getCekaonica(),taCekaju);
            }
        });

        btSledeci.setOnAction(e -> {
            if(bolnica.getCekaonica().isEmpty()){
                taCekaju.clear();
                taCekaju.appendText("Niko ne ceka");
                return;
            }
            bolnica.sledeci();
            ispisiListu(bolnica.getCekaonica(),taCekaju);
            ispisiListu(bolnica.getIzolacija(),taIzolacija);
            ispisiListu(bolnica.getZdravi(),taZdravi);
        });

        btUbrzajVreme.setOnAction(e->{
            int brDana = Integer.parseInt(tfBrojDana.getText());
            if(brDana < 0){
                lvGreska.setVisible(true);
                tfBrojDana.clear();
                return;
            }
            lvGreska.setVisible(false);
            for(Pacijent p : bolnica.getIzolacija()){
                p.leci(brDana);
            }
            bolnica.getZdravi().addAll(bolnica.getIzolacija().stream().filter(Pacijent::izlecen).collect(Collectors.toList()));
            bolnica.setIzolacija(bolnica.getIzolacija().stream().filter(pacijent -> !pacijent.izlecen()).collect(Collectors.toList()));
            ispisiListu(bolnica.getIzolacija(),taIzolacija);
            ispisiListu(bolnica.getZdravi(),taZdravi);
        });

        btPrikaziStatistike.setOnAction(e -> {
            long brPacijenata,brojZarazenih,broIzlecenih;
            if(rbSve.isSelected()){
                broIzlecenih = bolnica.getZdravi().stream().filter(Pacijent::isZarazen).count();
                brojZarazenih = bolnica.getIzolacija().size();
                brPacijenata = brojZarazenih + bolnica.getZdravi().size();

                taIzolacija.appendText("Procenat zarazenih :" + (100.0 * brojZarazenih / brPacijenata) + "\n");
                taZdravi.appendText("Procenat zdravih: " + (100.0 * broIzlecenih / (brojZarazenih + broIzlecenih)) + "\n");
            }
            else{
                broIzlecenih = bolnica.getZdravi().stream().filter(p -> p.isZarazen() && p.getDijagnoza() instanceof Korona).count();
                brojZarazenih = bolnica.getIzolacija().stream().filter(p -> p.getDijagnoza() instanceof Korona).count();
                brPacijenata = brojZarazenih + bolnica.getZdravi().size();

                taIzolacija.appendText("Procenat zarazenih :" + (100.0 * brojZarazenih / brPacijenata) + "\n");
                taZdravi.appendText("Procenat zdravih: " + (100.0 * broIzlecenih / (brojZarazenih + broIzlecenih)) + "\n");
            }
        });

        btUnesi.setOnAction(e -> {
            bolnica.unesi();
        });

        vbDesni.getChildren().addAll(lbCekaju,taCekaju,lbIzolacija,taIzolacija,lbZdravi,taZdravi);

        Scene scene = new Scene(root,600,500);
        stage.setScene(scene);
        stage.setTitle("Bolnica");
        stage.show();
    }

    private static <T> void ispisiListu(List<T> lista,TextArea ta){
        ta.clear();
        for(T x: lista){
            ta.appendText(x + "\n");
        }
    }
}
