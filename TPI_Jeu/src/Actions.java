import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

public class Actions extends Stage {
    public void Show(Noeud noeud){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Actions");
        alert.setHeaderText(null);

        ButtonType buttonTypeAuberge = new ButtonType("Auberge");
        ButtonType buttonTypeManoir = new ButtonType("Manoir");
        ButtonType buttonTypeChateau = new ButtonType("Château");
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);

        StringBuffer message = new StringBuffer();
        message.append("Or: " + "\r\n");
        message.append("Doritos: " + "\r\n");
        message.append("Mountain Dew: " + "\r\n");
        if(noeud.Constructible){
            message.append("Construire? \r\n");
            //message.append("Le noeud appartient à: " + "\r\n");
        } else{
            message.append("Le noeud n'est pas constructible. " + "\r\n");
        }
        alert.setContentText(message.toString());

        //Choisir quel bouton afficher si le noeud est constructible ou non ou s'il appartient à quelqu'un
        if(noeud.Constructible){
            alert.getButtonTypes().setAll(buttonTypeAuberge, buttonTypeManoir, buttonTypeChateau, buttonTypeOk);
        } else{
            alert.getButtonTypes().setAll(buttonTypeOk);
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeAuberge){
            // ... user chose "Auberge"
        } else if (result.get() == buttonTypeManoir) {
            // ... user chose "Manoir"
        } else if (result.get() == buttonTypeChateau) {
            // ... user chose "Chateau"
        } else {
            // ... user chose OK or closed the dialog
        }
        //TODO Afficher l'or du joueur et lui donner le choix de construire si le noeud le permet
    }
}
