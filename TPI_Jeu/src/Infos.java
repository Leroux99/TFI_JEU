import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

public class Infos extends Stage {
    public void Show(Noeud noeud){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText(null);
        StringBuffer message = new StringBuffer();
        if(noeud.Troll) message.append("Il y a un Troll. \r\n");
        if(noeud.Gobelin) message.append("Il y a un Gobelin. \r\n");
        if(noeud.Or) message.append("Il y a une pièce d'or. \r\n");
        if(noeud.Doritos) message.append("Il y a des Doritos. \r\n");
        if(noeud.MountainDew) message.append("Il y a du Mountain Dew. \r\n");
        if(noeud.Joueurs > 0) message.append("Il y a " + noeud.Joueurs + " joueurs. \r\n");
        //TODO dire si le noeud appartient à quelqu'un

        if(message.length() == 0) message.append("Il n'y a rien sur le noeud.");

        alert.setContentText(message.toString());

        ButtonType buttonTypeDeplacer = new ButtonType("Se déplacer", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel;

        if(cheminExists(noeud)){
            buttonTypeCancel  = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeCancel, buttonTypeDeplacer);
        }
        else{
            buttonTypeCancel = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeCancel);
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeDeplacer){
            Jeu.joueur.seDeplacer(noeud);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private Boolean cheminExists(Noeud noeud){
        Boolean cheminExists = false;
        for(Integer chemin_ID : Jeu.joueur.Position.Chemins){
            if(noeud.ID == chemin_ID) cheminExists = true;
        }
        if(cheminExists) return true;
        else return false;
    }
}
