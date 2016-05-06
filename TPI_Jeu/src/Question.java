import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

public class Question extends Stage {
    public static void Show(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Question");
        //TODO Get la question
        alert.setHeaderText("Ici va être affiché la question.");

        StringBuffer reponses = new StringBuffer();
        //TODO Get les réponses
        reponses.append("A: " + "\r\n");
        reponses.append("B: " + "\r\n");
        reponses.append("C: " + "\r\n");
        reponses.append("D: " + "\r\n");

        alert.setContentText(reponses.toString());

        ButtonType buttonTypeA = new ButtonType("A");
        ButtonType buttonTypeB = new ButtonType("B");
        ButtonType buttonTypeC = new ButtonType("C");
        ButtonType buttonTypeD = new ButtonType("D");

        alert.getButtonTypes().setAll(buttonTypeA, buttonTypeB, buttonTypeC, buttonTypeD);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeA){
            //TODO ... user chose "A"
        } else if (result.get() == buttonTypeB) {
            //TODO ... user chose "B"
        } else if (result.get() == buttonTypeC) {
            //TODO ... user chose "C"
        } else if (result.get() == buttonTypeD) {
            //TODO ... user chose "D"
        }
        //TODO Afficher dialogue qui pose une question
    }

}
