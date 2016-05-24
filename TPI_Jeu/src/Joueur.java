import javafx.scene.control.Alert;
import oracle.jdbc.OracleTypes;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Joueur {
    public Noeud Position;
    private final int PRIXMOUNTAINDEW = 1;
    private final int PRIXDORITOS = 1;
    private final int PRIXOR = 3;

    public List<Integer> listeBatiments = new ArrayList<Integer>();

    Joueur(Noeud noeud) {
        Position = noeud;
        Position.UpdateColors();
    }

    public void seDeplacer(Noeud noeud) {
        Boolean cheminExists = false;
        for (Integer chemin_ID : Position.Chemins)
            if (noeud.ID == chemin_ID) cheminExists = true;

        if (cheminExists) EnvoyerDeplacement(noeud);
    }

    public void EnvoyerDeplacement(Noeud noeudCible) {
        CallableStatement cStat = null;
        String ligne;
        String ipProprietaire = "";

        try {
            Jeu.writerCommandes.println("GOTO " + noeudCible.ID);
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
                new Question().Show(ipProprietaire);
            } else if (ligne.equals("T")) {
                payerTroll();
                Jeu.writerCommandes.println("NODE");
                Jeu.writerCommandes.flush();
                noeudCible = new Carte().getNoeud(Integer.parseInt(Jeu.readerCommandes.readLine()));
            } else if (ligne.equals("G")) {
                payerGobelin();
                Jeu.writerCommandes.println("NODE");
                Jeu.writerCommandes.flush();
                noeudCible = new Carte().getNoeud(Integer.parseInt(Jeu.readerCommandes.readLine()));
            }
            Jeu.infos.UpdateStats();
            if (cStat != null) {
                cStat.clearParameters();
                cStat.close();
            }
            setPosition(noeudCible);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setPosition(Noeud noeud){
        Noeud tempNoeud = Position;
        Position = noeud;
        tempNoeud.UpdateColors();
        Position.UpdateColors();
    }

    private void payerTroll(){
        CallableStatement cStat = null;
        try {
            if (getMountainDew() >= PRIXMOUNTAINDEW) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_MOUNTAINDEW(?)}");
                cStat.setInt(1, -PRIXMOUNTAINDEW);
                cStat.executeUpdate();
                Jeu.writerCommandes.println("FREE");
                Jeu.writerCommandes.flush();
                Jeu.readerCommandes.readLine();
            } else if (getGold() >= PRIXOR) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_OR(?)}");
                cStat.setInt(1, -PRIXOR);
                cStat.executeUpdate();
                Jeu.writerCommandes.println("FREE");
                Jeu.writerCommandes.flush();
                Jeu.readerCommandes.readLine();
            } else youLose();
            Jeu.infos.UpdateStats();
            if(cStat != null){
                cStat.clearParameters();
                cStat.close();
            }
        }catch(IOException | SQLException e){}
    }

    private void payerGobelin(){
        CallableStatement cStat = null;
        try {
            if (getDoritos() >= PRIXDORITOS) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_DORITOS(?)}");
                cStat.setInt(1, -PRIXDORITOS);
                cStat.executeUpdate();
                Jeu.writerCommandes.println("FREE");
                Jeu.writerCommandes.flush();
                Jeu.readerCommandes.readLine();
            } else if (getGold() >= PRIXOR) {
                cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_OR(?)}");
                cStat.setInt(1, -PRIXOR);
                cStat.executeUpdate();
                Jeu.writerCommandes.println("FREE");
                Jeu.writerCommandes.flush();
                Jeu.readerCommandes.readLine();
            } else youLose();
            Jeu.infos.UpdateStats();
            if(cStat != null){
                cStat.clearParameters();
                cStat.close();
            }
        }catch(IOException | SQLException e){}
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

    private int getDoritos(){
        CallableStatement cStat;
        int doritos = 0;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_Doritos()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            doritos = cStat.getInt(1);
            cStat.clearParameters();
            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return doritos;
    }

    private int getMountainDew(){
        CallableStatement cStat;
        int mountainDew = 0;
        try{
            cStat = Jeu.CONNEXION.prepareCall(" {? = call TP_ORDRAGON.Afficher_MountainDew()}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            cStat.execute();
            mountainDew = cStat.getInt(1);
            cStat.clearParameters();
            cStat.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return mountainDew;
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
