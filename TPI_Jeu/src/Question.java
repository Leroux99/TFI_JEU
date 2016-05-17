import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Question {

    public static void Show(String ipProprietaire) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Question");
        //TODO Get la question
        CallableStatement cStat = null;
        Socket Soc_Proprietaire;
        final int portProprietaire = 1666;
        String reponseChoisie;
        BufferedReader reader;
        PrintWriter writer;
        ArrayList<String> contenu = new ArrayList<>();
        try {
            // se connecter au proprietaire de l'immeuble
            Soc_Proprietaire = new Socket(ipProprietaire, portProprietaire);
            // get les questions/réponses
            reader = new BufferedReader(new InputStreamReader(Soc_Proprietaire.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(Soc_Proprietaire.getOutputStream()));
            writer.println(Jeu.NomEquipe);
            writer.flush();
            Boolean estVide = false;
            String ligne = null;
            while (!estVide) {
                ligne = reader.readLine();
                if(ligne == null) estVide = true;
                else if (!ligne.equals("")) {
                    contenu.add(ligne);
                }
                else estVide = true;
            }

        // La question
        alert.setHeaderText(contenu.get(0));

        StringBuffer reponses = new StringBuffer();
        //TODO Get les réponses
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
            // se connecter au proprietaire de l'immeuble

            if (result.get() == buttonType1) {
                writer.println(1);
            } else if (result.get() == buttonType2) {
                writer.println(2);
            } else if (result.get() == buttonType3) {
                writer.println(3);
            } else if (result.get() == buttonType4) {
                writer.println(4);
            }
            writer.flush();

            ligne = reader.readLine();
            while (ligne == null) ligne = reader.readLine();

            if (ligne.equals("OK")) {
                // ???
            } else if(ligne.equals("ERR")){
                // payer le proprio pour continuer NOM DE PROCEDURE RANDOM!! À VÉRIFIER...
                /*cStat = Jeu.CONNEXION.prepareCall(" { call TP_ORDRAGON.PAYER(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();*/
                System.out.println("PAYER");
            }
            writer.close();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } /*catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}
