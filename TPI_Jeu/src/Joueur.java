import javafx.application.Platform;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Joueur {

    public Noeud Position;
    public int Or = 0;
    public int Doritos =0;
    public int MountainDew = 0;
    Joueur(Noeud noeud){
        Platform.runLater(new ChangerCouleur(noeud));
    }

    public void seDeplacer(Noeud noeud){
        Boolean cheminExists = false;
        for(Integer chemin_ID : Position.Chemins){
            if(noeud.ID == chemin_ID) cheminExists = true;
        }
        if(cheminExists){
            Platform.runLater(new ChangerCouleur(noeud));
            EnvoyerDeplacement();
            //TODO recevoir la réponse du serveur
        }
    }

    public void EnvoyerDeplacement(){
        Socket Serveur_Prof = null;
        PrintWriter write;
        try{
            Serveur_Prof=new Socket(Jeu.ADRESSE_PROF, Jeu.PORT_PROF_COMMANDES);
            write = new PrintWriter(
                    new OutputStreamWriter(Serveur_Prof.getOutputStream()));
            write.println("GOTO " + Position.ID);
            write.flush();
            write.close();
            Serveur_Prof.close();

        }catch(IOException e){
            try{
                Serveur_Prof.close();
            }catch (IOException e1){}
        }

    }

    class ChangerCouleur implements Runnable{
        Noeud nouveauNoeud;

        ChangerCouleur(Noeud noeud){
            nouveauNoeud = noeud;
        }
        @Override
        public void run(){
            if(Position != null) {
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
