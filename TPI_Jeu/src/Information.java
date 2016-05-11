import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class Information {
    List<Text> infos = new ArrayList<Text>();

    Text or = new Text(10,20, "Or: ");
    Text doritos = new Text(10,50, "Doritos: ");
    Text mountaindew = new Text(10, 80, "Mountain Dew: ");
    Text gobelin = new Text(210, 20, "Gobelin: ");
    Text troll = new Text(210, 50, "Troll: ");
    Text joueurs = new Text(210, 80, "Joueurs: ");
    Text constructible = new Text(410, 20, "Constructible: ");
    Text proprietaire = new Text(410,50,"Propriétaire: ");
    Text batiment = new Text(410, 80, "Batiment: ");

    public Information(){
        infos.add(or);
        infos.add(doritos);
        infos.add(mountaindew);
        infos.add(gobelin);
        infos.add(troll);
        infos.add(joueurs);
        infos.add(constructible);
        infos.add(proprietaire);
        infos.add(batiment);

        for(Text t : infos){
            t.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        }

        HideText();
    }

    public List<Text> getInfos(){
        return infos;
    }

    public void UpdateInfos(Noeud noeud){
        if(noeud.Or) or.setText("Or: Oui");
        else or.setText("Or: Non");
        if(noeud.Troll) troll.setText("Troll: Oui");
        else troll.setText("Troll: Non");
        if(noeud.Gobelin) gobelin.setText("Gobelin: Oui");
        else gobelin.setText("Gobelin: Non");
        if(noeud.Doritos) doritos.setText("Doritos: Oui");
        else doritos.setText("Doritos: Non");
        if(noeud.MountainDew) mountaindew.setText("Mountain Dew: Oui");
        else mountaindew.setText("Mountain Dew: Non");
        if(noeud.Constructible) constructible.setText("Constructible: Oui");
        else or.setText("Constructible: Non");
        joueurs.setText("Joueurs: " + noeud.Joueurs);
        batiment.setText("Batiment: " + noeud.batiment);
    }

    public void HideText(){
        for(Text t : infos){
            t.setVisible(false);
        }
    }

    public void ShowText(){
        for(Text t : infos){
            t.setVisible(true);
        }
    }

}
