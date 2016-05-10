import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Actions {
    private List<Text> infos = new ArrayList<Text>();

    private Text or = new Text(1021,822, "Or: " + Jeu.joueur.Or);
    private Text doritos = new Text(1021,852, "Doritos: " + Jeu.joueur.Doritos);
    private Text mountaindew = new Text(1021, 882, "Mountain Dew: " + Jeu.joueur.MountainDew);

    private Rectangle construire = new Rectangle();
    private Text construire_text = new Text("Construire");

    public Actions(){
        infos.add(or);
        infos.add(doritos);
        infos.add(mountaindew);

        for(Text t : infos){
            t.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        }

        construire.setX(1408);
        construire.setY(821);
        construire.setHeight(59);
        construire.setWidth(179);
        construire.setStroke(Color.GRAY);
        construire.setFill(Color.LIGHTGRAY);
        construire.setStrokeWidth(2);
        construire.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e));
        construire.setCursor(Cursor.HAND);

        construire_text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        construire_text.setX(1458);
        construire_text.setY(854);
        construire_text.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> gererClic(e));
        construire_text.setCursor(Cursor.HAND);

    }

    public List<Text> getInfos() {
        return infos;
    }

    public Rectangle getConstruire() {
        return construire;
    }

    public Text getConstruire_text() {
        return construire_text;
    }

    public void gererClic(MouseEvent e) {
            //TODO CONSTRUIRE
        }
}
