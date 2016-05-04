import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Noeud extends Circle{
    int ID;
    Boolean Constructible;
    public Boolean Troll;
    public Boolean Gobelin;
    public Boolean Or;
    public Boolean MountainDew;
    public Boolean Doritos;
    public int Joueurs;

    Noeud(int ID, int Position_X, int Position_Y, Boolean Constructible){
        super(Position_X, Position_Y, 10, Color.BLACK);
        this.ID = ID;
        this.Constructible = Constructible;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererDeplacement(e));
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> gererEnter(e));
        this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> gererExit(e));
        Troll = false;
        Gobelin = false;
        Or= false;
        MountainDew = false;
        Doritos = false;
        Joueurs = 0;
    }

    public void gererDeplacement(MouseEvent e){
        /*
        TODO
        Va afficher un menu qui va présenter les variables à l'utilisateur et lui laisser le choix entre
        se déplacer sur le noeud ou fermer le menu
        */
    }

    public void gererEnter(MouseEvent e){
        this.setCursor(Cursor.HAND);
        this.setFill(Color.GRAY);

    }

    public void gererExit(MouseEvent e){
        this.setCursor(Cursor.DEFAULT);
        this.setFill(Color.BLACK);
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
}
