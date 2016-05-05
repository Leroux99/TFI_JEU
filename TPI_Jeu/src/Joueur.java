import javafx.application.Platform;
import javafx.scene.paint.Color;

public class Joueur {

    public Noeud Position;

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
            //TODO envoyer le changement de position au serveur
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
