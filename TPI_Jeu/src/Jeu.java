import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Jeu extends Application{
    final static public String ADRESSE_PROF = "149.56.47.97";
    final static public int PORT_PROF_JOUEURS = 51006;
    final static public int PORT_PROF_CARTE = 51005;

    Carte carte = new Carte(); //notre carte
    public static Joueur joueur; //Notre joueur (static pour être accessible partout)
    class ContenuNoeuds implements Runnable{
        final private String SEPARATEUR=" ";

        public ArrayList listeContenu=new ArrayList();
        private Socket Serveur_Prof;
        private BufferedReader read;
        private PrintWriter write;

        public void run() {
            try {
                //connection server
                Serveur_Prof=new Socket(ADRESSE_PROF, PORT_PROF_JOUEURS);
                //traiter les envoit du server.
                read = new BufferedReader(
                        new InputStreamReader(Serveur_Prof.getInputStream()));
                write = new PrintWriter(
                        new OutputStreamWriter(Serveur_Prof.getOutputStream()));

                // Lecture/écriture
                boolean fini = false;
                String ligne = null;
                String[] nbDinfo;
                int numNoeud;
                String nbObj;

                while (!fini) {
                    ligne = read.readLine();
                    write.println();
                    write.flush();
                    if (ligne != null) {
                        if(listeContenu.size()>0)
                            listeContenu.clear();
                        nbDinfo=ligne.split(SEPARATEUR);
                        for(int i=0;i<nbDinfo.length;++i)
                        {
                            listeContenu.add(nbDinfo[i]);
                        }
                    carte.UpdateCarte(listeContenu);
                    }
                }
                read.close();
                write.close();
                Serveur_Prof.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                try {
                    Serveur_Prof.close();
                } catch (IOException e1) { }
            }
        }
    }

    public void getStartpoint(){
        //TODO code
        joueur = new Joueur(carte.getNoeuds().get(1));
    }

    @Override
    public void start(Stage stage) {
        getStartpoint();
        Group groupe = new Group();
        groupe.getChildren().add(new ImageView("http://prog101.com/travaux/dragon/images/carte01.png"));
        groupe.getChildren().addAll(carte.getLigneChemins());
        groupe.getChildren().addAll(carte.getNoeuds());

        Scene scene = new Scene(groupe);
        stage.setScene(scene);
        stage.setWidth(1600);
        stage.setHeight(900);
        stage.setTitle("L'or du dragon");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();

        Thread t = new Thread(new ContenuNoeuds());
        t.setDaemon(true);
        t.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
