import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import oracle.jdbc.OracleTypes;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Actions {
    private Rectangle construire = new Rectangle();
    private Text construire_text = new Text("Construire");
    private Text resultatConstruire = new Text(1050, 828, null);
    private Rectangle bot_button = new Rectangle();
    private Text bot_text = new Text("Bot");
    private final int PAUSE = 3000;
    public final int PRIXBATIMENT = 3;

    public Actions() {

        construire.setX(1398);
        construire.setY(795);
        construire.setHeight(59);
        construire.setWidth(179);
        construire.setStroke(Color.GRAY);
        construire.setFill(Color.LIGHTGRAY);
        construire.setStrokeWidth(2);
        construire.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e));
        construire.setCursor(Cursor.HAND);

        construire_text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        construire_text.setX(1448);
        construire_text.setY(828);
        construire_text.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e));
        construire_text.setCursor(Cursor.HAND);

        resultatConstruire.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        resultatConstruire.setVisible(false);

        bot_button.setX(1198);
        bot_button.setY(795);
        bot_button.setHeight(59);
        bot_button.setWidth(179);
        bot_button.setStroke(Color.GRAY);
        bot_button.setFill(Color.LIGHTGRAY);
        bot_button.setStrokeWidth(2);
        bot_button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> botClic(e));
        bot_button.setCursor(Cursor.HAND);

        bot_text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        bot_text.setX(1272);
        bot_text.setY(828);
        bot_text.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> botClic(e));
        bot_text.setCursor(Cursor.HAND);
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

    public Rectangle getBot_button(){
        return bot_button;
    }

    public Text getBot_text(){
        return bot_text;
    }

    public void gererClic(MouseEvent e) {
        CallableStatement cStat = null;
        String ligne;
        try {
            if (!Jeu.joueur.Position.Constructible){
                resultatConstruire.setText("Noeud non \r\nconstructible");
                new AfficherResultatConstruire().start();
            }
            else if(getOr() >= PRIXBATIMENT) {
                Jeu.writerCommandes.println("BUILD");
                Jeu.writerCommandes.flush();
                ligne = Jeu.readerCommandes.readLine();
                if (ligne.equals("AUB")) {
                    cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Auberge(?)}");
                    cStat.setInt(1, 1);
                    cStat.executeUpdate();
                    resultatConstruire.setText("AUBERGE \r\nAJOUTÉ !");
                    new AfficherResultatConstruire().start();
                } else if (ligne.equals("MAN")) {
                    cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Manoire(?)}");
                    cStat.setInt(1, 1);
                    cStat.executeUpdate();
                    resultatConstruire.setText("MANOIR \r\nAJOUTÉ !");
                    new AfficherResultatConstruire().start();
                } else if (ligne.equals("CHA")) {
                    cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Chateaux(?)}");
                    cStat.setInt(1, 1);
                    cStat.executeUpdate();
                    resultatConstruire.setText("CHATEAU \r\nAJOUTÉ !");
                    new AfficherResultatConstruire().start();
                }
                if(!ligne.equals("ERR")){
                    cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Update_Or(?)}");
                    cStat.setInt(1, -PRIXBATIMENT);
                    cStat.executeUpdate();
                    Jeu.infos.UpdateStats();
                    Boolean isAdded = false;
                    for(Integer ID : Jeu.joueur.listeBatiments) if(Jeu.joueur.Position.ID == ID) isAdded = true;
                    if(!isAdded) Jeu.joueur.listeBatiments.add(Jeu.joueur.Position.ID);
                    if(verifierGagner()) youWin();
                }
                else{
                    resultatConstruire.setText("Impossible \r\nde \r\nconstruire!");
                    new AfficherResultatConstruire().start();
                }
                if (cStat != null) {
                    cStat.clearParameters();
                    cStat.close();
                }
            }
            else{
                resultatConstruire.setText("Or insuffisant!");
                new AfficherResultatConstruire().start();
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void botClic(MouseEvent e){
        if(Jeu.bot == null) Jeu.startBot();
        else{
            if(Jeu.bot.actif == true) Jeu.bot.actif = false;
            else Jeu.startBot();
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

    private Boolean verifierGagner(){
        CallableStatement cStat = null;
        ResultSet rst = null;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.Afficher_Stats(?)}");
            cStat.registerOutParameter(1,OracleTypes.CURSOR);
            cStat.execute();

            rst = (ResultSet) cStat.getObject(1);
            rst.next();

            return (rst.getInt("NbAuberges") >= 1 && rst.getInt("NbManoires") >= 1 && rst.getInt("NbChateaux") >= 1);
        } catch(SQLException e){

        }
        return false;
    }

    private void youWin(){
        if(!Jeu.bot.isWorking) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Yay!");
            alert.setHeaderText("Vous avez gagné!");
            alert.setContentText(null);
            alert.showAndWait();
        } else System.out.println("Vous avez gagné!");

        System.exit(1);
    }

    public Integer getOr(){
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

}
