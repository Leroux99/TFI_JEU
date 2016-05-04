import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/*********
 * Fichier: Chemin
 * Auteur: Jérémie Leroux
 * Date: 2016-05-04
 *********/
public class Chemin extends Line {
    public int ID;

    Chemin(int ID, double X_Debut, double Y_Debut, double X_Fin, double Y_Fin){
        super(X_Debut, Y_Debut, X_Fin, Y_Fin);
        this.setFill(Color.BLACK);
        this.ID = ID;
    }
}
