import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

import java.io.*;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Actions {
    private List<Text> infos = new ArrayList<Text>();

    private Text or = new Text(1021, 822, null);
    private Text doritos = new Text(1021, 852, null);
    private Text mountaindew = new Text(1021, 882, null);

    private Rectangle construire = new Rectangle();
    private Text construire_text = new Text("Construire");

    private Text resultatConstruire = new Text(1221, 822, null);

    private final int PAUSE = 3000;

    public Actions() {
        UpdateStats();

        infos.add(or);
        infos.add(doritos);
        infos.add(mountaindew);

        for (Text t : infos) {
            t.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        }

        construire.setX(1408);
        construire.setY(821);
        construire.setHeight(59);
        construire.setWidth(179);
        construire.setStroke(Color.GRAY);
        construire.setFill(Color.LIGHTGRAY);
        construire.setStrokeWidth(2);
        construire.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e));
        construire.setCursor(Cursor.HAND);

        construire_text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        construire_text.setX(1458);
        construire_text.setY(854);
        construire_text.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e));
        construire_text.setCursor(Cursor.HAND);

        resultatConstruire.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        resultatConstruire.setVisible(false);

    }

    public List<Text> getInfos() {
        return infos;
    }

    public Rectangle getConstruire() {
        return construire;
    }

    public Text getConstruire_text() {
        return construire_text;
    }

    public Text getResultatConstruire() {
        return resultatConstruire;
    }

    public void gererClic(MouseEvent e) {
        CallableStatement cStat = null;
        String ligne;
        try {
            Jeu.writerCommandes.println("BUILD");
            Jeu.writerCommandes.flush();
            ligne = Jeu.readerCommandes.readLine();
            if (ligne.equals("AUB")) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Auberge(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();
                resultatConstruire.setText("AUBERGE AJOUTÉ !");
                new AfficherResultatConstruire().start();
            } else if (ligne.equals("MAN")) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Manoire(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();
                resultatConstruire.setText("MANOIR AJOUTÉ !");
                new AfficherResultatConstruire().start();
            } else if (ligne.equals("CHA")) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Chateaux(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();
                resultatConstruire.setText("CHATEAU AJOUTÉ !");
                new AfficherResultatConstruire().start();
            }
            if(cStat != null) {
                cStat.clearParameters();
                cStat.close();
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void UpdateStats(){
        CallableStatement cStat;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_Or()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            or.setText("Or: " + cStat.getInt(1));
            cStat.clearParameters();

            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_MountainDew()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            mountaindew.setText("Mountain Dew: " + cStat.getInt(1));
            cStat.clearParameters();

            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_Doritos()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            doritos.setText("Doritos: " + cStat.getInt(1));
            cStat.clearParameters();

            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    class AfficherResultatConstruire extends Thread{

        @Override
        public void run(){
            resultatConstruire.setVisible(true);
            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultatConstruire.setVisible(false);
        }
    }
}
