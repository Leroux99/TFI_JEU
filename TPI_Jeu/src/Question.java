import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import oracle.jdbc.OracleTypes;
import java.io.*;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Question {
    private final int PRIXOR = 2;

    public void Show(String ipProprietaire) {
        CallableStatement cStat = null;
        Socket Soc_Proprietaire;
        final int portProprietaire = 1666;
        BufferedReader reader;
        PrintWriter writer;
        ArrayList<String> contenu = new ArrayList<>();
        try {
            // se connecter au proprietaire de l'immeuble
            Soc_Proprietaire = new Socket(ipProprietaire, portProprietaire);
            // get les questions/réponses
            reader = new BufferedReader(new InputStreamReader(Soc_Proprietaire.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(Soc_Proprietaire.getOutputStream()));
            writer.println(Jeu.NomEquipe + " " + Jeu.joueur.Position.ID);
            writer.flush();
            Boolean estVide = false;
            String ligne = null;
            while (!estVide) {
                ligne = reader.readLine();
                if(ligne == null) estVide = true;
                else if (!ligne.equals("")) contenu.add(ligne);
                else estVide = true;
            }
            if(!Jeu.bot.isWorking) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Question");
                // La question
                alert.setHeaderText(contenu.get(0));

                StringBuffer reponses = new StringBuffer();
                reponses.append(contenu.get(1) + "\r\n");
                reponses.append(contenu.get(2) + "\r\n");
                reponses.append(contenu.get(3) + "\r\n");
                reponses.append(contenu.get(4) + "\r\n");

                alert.setContentText(reponses.toString());

                ButtonType buttonType1 = new ButtonType("1");
                ButtonType buttonType2 = new ButtonType("2");
                ButtonType buttonType3 = new ButtonType("3");
                ButtonType buttonType4 = new ButtonType("4");

                alert.getButtonTypes().setAll(buttonType1, buttonType2, buttonType3, buttonType4);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonType1) {
                    writer.println(1);
                } else if (result.get() == buttonType2) {
                    writer.println(2);
                } else if (result.get() == buttonType3) {
                    writer.println(3);
                } else if (result.get() == buttonType4) {
                    writer.println(4);
                }
            }
            else writer.println((int) (Math.random() * 4) + 1);
            writer.flush();

            ligne = reader.readLine();
            while (ligne == null) ligne = reader.readLine();

            if(ligne.equals("ERR"))
                updateOr(-PRIXOR);

            writer.close();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateOr(int amount){
        CallableStatement cStat = null;
        try {
            if (getGold() >= PRIXOR) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_OR(?)}");
                cStat.setInt(1, amount);
                cStat.executeUpdate();
            } else youLose();

            Jeu.infos.UpdateStats();
            if(cStat != null){
                cStat.clearParameters();
                cStat.close();
            }
        }catch(SQLException e){}
    }

    private int getGold(){
        CallableStatement cStat;
        int or = 0;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_Or()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            or = cStat.getInt(1);
            cStat.clearParameters();
            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return or;
    }

    private void youLose(){
        if(!Jeu.bot.isWorking) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oh non!");
            alert.setHeaderText("Vous avez perdu!");
            alert.setContentText("Décevant..");
            alert.showAndWait();
        } else System.out.println("Vous avez perdu!");
        System.exit(1);
    }
}
