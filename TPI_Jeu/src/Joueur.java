import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class Joueur {

    public Noeud Position;

    Joueur(Noeud noeud) {
        Position = noeud;
        noeud.UpdateColors();
    }

    public void seDeplacer(Noeud noeud) {
        Boolean cheminExists = false;
        for (Integer chemin_ID : Position.Chemins) {
            if (noeud.ID == chemin_ID) cheminExists = true;
        }
        if (cheminExists) {
            Noeud temp = Position;
            Position = noeud;
            temp.UpdateColors();
            Position.UpdateColors();
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
        String ipProprietaire = "";
        try {
            Jeu.writerCommandes.println("GOTO " + Position.ID);
            Jeu.writerCommandes.flush();
            ligne = Jeu.readerCommandes.readLine();
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
                ipProprietaire = ligne.substring(3);
                Question.Show(ipProprietaire);
            } else if (ligne.equals("T")) {
                //TODO mettre à jour la bd
                System.out.println("troll");
                Jeu.writerCommandes.println("FREE");
                Jeu.writerCommandes.flush();
                Jeu.readerCommandes.readLine();
            } else if (ligne.equals("G")) {
                //TODO mettre à jour la bd
                Jeu.writerCommandes.println("FREE");
                Jeu.writerCommandes.flush();
                Jeu.readerCommandes.readLine();
            }
            Jeu.actions.UpdateStats();
            if(cStat != null){
                cStat.clearParameters();
                cStat.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
