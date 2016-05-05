import javafx.scene.paint.Color;

public class Joueur {

    public Noeud Position;

    Joueur(Noeud noeud){
        ChangerCouleurs(noeud);
        Position.setFill(Color.DARKRED);
    }

    public void seDeplacer(Noeud noeud){
        Boolean cheminExists = false;
        for(Integer chemin_ID : Position.Chemins){
            if(noeud.ID == chemin_ID) cheminExists = true;
        }
        if(cheminExists){
            ChangerCouleurs(noeud);
        }
    }

    private void ChangerCouleurs(Noeud noeud){
        if(Position != null) {
            Position.setFill(Color.BLACK);
            Position.enter = Color.GRAY;
            Position.exit = Color.BLACK;
        }
        Position = noeud;
        Position.setFill(Color.RED);
        Position.enter = Color.RED;
        Position.exit = Color.DARKRED;
    }

}
