import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Noeud extends Circle{
    enum typebatiment {aucun, auberge, manoir, chateau}
    int ID;
    public Boolean Constructible;
    public Boolean Troll;
    public Boolean Gobelin;
    public Boolean Or;
    public Boolean MountainDew;
    public Boolean Doritos;
    public typebatiment batiment;
    public int Joueurs;
    public List<Integer> Chemins = new ArrayList<Integer>();
    public Boolean focused = false;
    public Color enter = Color.GRAY;
    public Color exit = Color.BLACK;

    Noeud(int ID, int Position_X, int Position_Y, Boolean Constructible){
        super(Position_X, Position_Y, 10, Color.BLACK);
        this.ID = ID;
        this.Constructible = Constructible;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererDeplacement(e));
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> gererEnter(e));
        this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> gererExit(e));
        this.setCursor(Cursor.HAND);
        Troll = false;
        Gobelin = false;
        Or= false;
        MountainDew = false;
        Doritos = false;
        Joueurs = 0;
        batiment = typebatiment.aucun;
    }

    public void gererDeplacement(MouseEvent e){
        Jeu.joueur.seDeplacer(this);
    }

    public void gererEnter(MouseEvent e){
        focused = true;
        Platform.runLater(new ChangerCouleur());
        Jeu.infos.UpdateInfos(this);
        Platform.runLater(new ChangerInfos());
    }

    public void gererExit(MouseEvent e){
        focused = false;
        Platform.runLater(new ChangerCouleur());
        Platform.runLater(new HideInfos());
    }

    public int getID(){return ID;}

    public void ClearInfos(){
        Troll = false;
        Gobelin = false;
        Or= false;
        MountainDew = false;
        Doritos = false;
        Joueurs = 0;
    }

    class ChangerCouleur implements Runnable{

        @Override
        public void run(){
            if(focused) setFill(enter);
            else setFill(exit);
        }
    }

    class ChangerInfos implements Runnable{
        @Override
        public void run(){
            Jeu.infos.ShowText();
        }
    }

    class HideInfos implements  Runnable {
        @Override
        public void run(){
            Jeu.infos.HideText();
        }
    }
}
