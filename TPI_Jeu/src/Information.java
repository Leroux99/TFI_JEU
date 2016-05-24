import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Information {
    List<Text> infos = new ArrayList<Text>();
    List<Text> stats = new ArrayList<Text>();
    Text or = new Text(10,20, "Or: ");
    Text doritos = new Text(10,50, "Doritos: ");
    Text mountaindew = new Text(10, 80, "Mountain Dew: ");
    Text constructible = new Text(210, 50, "Constructible: ");
    Text batiment = new Text(210, 20, "Batiment: ");

    public Information(){
        UpdateStats();
        stats.add(or);
        stats.add(doritos);
        stats.add(mountaindew);

        infos.add(constructible);
        infos.add(batiment);

        for(Text t : stats) t.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        for(Text t : infos) t.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        HideText();
    }

    public List<Text> getInfos(){
        return infos;
    }

    public List<Text> getStats() {
        return stats;
    }

    public void UpdateInfos(Noeud noeud){
        if(noeud.Constructible) constructible.setText("Constructible: Oui");
        else constructible.setText("Constructible: Non");
        batiment.setText("Batiment: " + noeud.batiment);
    }

    public void HideText(){
        for(Text t : infos) t.setVisible(false);
    }

    public void ShowText(){
        for(Text t : infos)t.setVisible(true);
    }

    public void UpdateStats(){
        or.setText("Or: " + getOr());
        mountaindew.setText("Mountain Dew: " + getMountainDew());
        doritos.setText("Doritos: " + getDoritos());
    }

    private Integer getOr(){
        CallableStatement cStat;
        Integer orAmount = 0;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_Or()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            orAmount = cStat.getInt(1);
            cStat.clearParameters();
            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return orAmount;
    }

    private Integer getDoritos(){
        CallableStatement cStat;
        Integer doritosAmount = 0;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_Doritos()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            doritosAmount = cStat.getInt(1);
            cStat.clearParameters();
            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return doritosAmount;
    }

    private Integer getMountainDew(){
        CallableStatement cStat;
        Integer mountaindewAmount = 0;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_MountainDew()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            mountaindewAmount = cStat.getInt(1);
            cStat.clearParameters();
            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return mountaindewAmount;
    }
}
