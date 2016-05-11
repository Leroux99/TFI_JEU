import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class Joueur {

    public Noeud Position;

    Joueur(Noeud noeud) {
        Platform.runLater(new ChangerCouleur(noeud));
    }

    public void seDeplacer(Noeud noeud) {
        Boolean cheminExists = false;
        for (Integer chemin_ID : Position.Chemins) {
            if (noeud.ID == chemin_ID) cheminExists = true;
        }
        if (cheminExists) {
            Platform.runLater(new ChangerCouleur(noeud));
            EnvoyerDeplacement();
            //TODO recevoir la réponse du serveur
        }
    }

    public void EnvoyerDeplacement() {
        CallableStatement cStat = null;
        Socket Serveur_Prof;
        PrintWriter write;
        BufferedReader reader;
        String ligne;
        try {
            Serveur_Prof = new Socket(Jeu.ADRESSE_PROF, Jeu.PORT_PROF_COMMANDES);
            write = new PrintWriter(new OutputStreamWriter(Serveur_Prof.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(Serveur_Prof.getInputStream()));
            write.println("GOTO " + Position.ID);
            write.flush();
            ligne = reader.readLine();

            if (ligne.equals("P")) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_OR(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();
            } else if (ligne.equals("M")) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_MOUNTAINDEW(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();
            } else if (ligne.equals("D")) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_DORITOS(?)}");
                cStat.setInt(1, 1);
                cStat.executeUpdate();
            } else if (ligne.contains("IP")) {

            } else if (ligne.equals("T")) {

            } else if (ligne.equals("G")) {

            }
            Jeu.actions.UpdateStats();
            reader.close();
            write.close();
            if(cStat != null) cStat.close();
            Serveur_Prof.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class ChangerCouleur implements Runnable {
        Noeud nouveauNoeud;

        ChangerCouleur(Noeud noeud) {
            nouveauNoeud = noeud;
        }

        @Override
        public void run() {
            if (Position != null) {
                Position.setFill(Color.BLACK);
                Position.enter = Color.GRAY;
                Position.exit = Color.BLACK;
            }
            Position = nouveauNoeud;
            Position.setFill(Color.RED);
            Position.enter = Color.RED;
            Position.exit = Color.DARKRED;
        }
    }
}
