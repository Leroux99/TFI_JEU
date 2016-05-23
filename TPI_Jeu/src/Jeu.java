import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Jeu extends Application {
    final static public String ADRESSE_PROF = "149.56.47.97";
    final static public int PORT_PROF_COMMANDES = 51007;
    final static public int PORT_PROF_JOUEURS = 51006;
    final static public int PORT_PROF_CARTE = 51005;
    Carte carte = new Carte(); //notre carte
    public static Joueur joueur; //Notre joueur (static pour être accessible partout)
    public static Information infos;
    public static Actions actions;
    public static Connection CONNEXION = null;
    public static String NomEquipe = "zLeslicornesroses";
    public static Socket Serveur_Prof_Commandes;
    public static PrintWriter writerCommandes = null;
    public static BufferedReader readerCommandes = null;

    class ContenuNoeuds implements Runnable {
        final private String SEPARATEUR = " ";
        public ArrayList listeContenu = new ArrayList();
        private Socket Serveur_Prof = null;
        private BufferedReader read;
        private PrintWriter write;

        public void run() {
            try {
                //connection server
                Serveur_Prof = new Socket(ADRESSE_PROF, PORT_PROF_JOUEURS);
                //traiter les envoit du server.
                read = new BufferedReader(new InputStreamReader(Serveur_Prof.getInputStream()));
                write = new PrintWriter(new OutputStreamWriter(Serveur_Prof.getOutputStream()));

                // Lecture/écriture
                boolean fini = false;
                String ligne = null;
                String[] nbDinfo;

                while (!fini) {
                    ligne = read.readLine();
                    write.println();
                    write.flush();
                    if (ligne != null) {
                        if (listeContenu.size() > 0)
                            listeContenu.clear();
                        nbDinfo = ligne.split(SEPARATEUR);
                        for (int i = 0; i < nbDinfo.length; ++i) {
                            listeContenu.add(nbDinfo[i]);
                        }
                        carte.UpdateCarte(listeContenu);
                    }
                }
                read.close();
                write.close();
                Serveur_Prof.close();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Serveur_Prof.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public void getStartpoint() {
        try {
            // Lecture/écriture
            writerCommandes.println("NODE");
            writerCommandes.flush();
            joueur = new Joueur(carte.getNoeuds().get(Integer.parseInt(readerCommandes.readLine())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chargerLePilote() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("Pilote chargé");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.exit(0);
        }
    }

    public boolean ouvrirConnection() {
        String bd = "jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl";
        String username = "leslicornesroses";
        String password = "Barbies123";
        boolean ok = false;
        try {
            CONNEXION = DriverManager.getConnection(bd, username, password);
            System.out.println("Connexion ouverte");
            ok = true;
        } catch (SQLException ex) {
            System.out.println("Username ou mot de passe incorrect.");
        }
        return ok;
    }

    public void fermerConnection() {
        try {
            if (CONNEXION != null) {
                CONNEXION.close();
                System.out.println("Connexion fermée");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void Identification() {
        try {
            writerCommandes.println("HELLO " + NomEquipe + " " + Inet4Address.getLocalHost().getHostAddress());
            writerCommandes.flush();
            readerCommandes.readLine();
        } catch (IOException e) {
        }
    }

    @Override
    public void start(Stage stage) {
        //Lorsque l'application ferme, appelle la méthode Quitter
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Quitter();
            }
        }));

        chargerLePilote();
        initCommandes();
        if (ouvrirConnection()) {
            Identification();
            getStartpoint();
            infos = new Information();
            actions = new Actions();

            Group groupe = new Group();
            groupe.getChildren().add(new ImageView("http://prog101.com/travaux/dragon/images/nowhereland.png"));
            groupe.getChildren().addAll(carte.getLigneChemins());
            groupe.getChildren().addAll(carte.getNoeuds());
            groupe.getChildren().addAll(infos.getInfos());
            groupe.getChildren().addAll(actions.getInfos());
            groupe.getChildren().addAll(actions.getConstruire(), actions.getConstruire_text(), actions.getResultatConstruire());

            Scene scene = new Scene(groupe);

            stage.setScene(scene);
            stage.setWidth(1600);
            stage.setHeight(900);
            stage.setTitle("L'or du dragon");
            stage.resizableProperty().setValue(Boolean.FALSE);
            //Quand on quitte le jeu, on envoie "QUIT" au serveur de jeu pour avertir.
            stage.show();

            Thread t = new Thread(new ContenuNoeuds());
            t.setDaemon(true);
            t.start();

            Thread t2 = new Thread(new NotreServeur());
            t2.setDaemon(true);
            t2.start();
        }
    }

    public void Quitter() {
        ResetQuestions();
        ResetStats();
        fermerConnection();
        try {
            writerCommandes.println("QUIT");
            writerCommandes.flush();
            writerCommandes.close();
            readerCommandes.close();
            Serveur_Prof_Commandes.close();

        } catch (IOException e) {
        }
        System.out.println("Quitter");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initCommandes(){
        try {
            Serveur_Prof_Commandes = new Socket(ADRESSE_PROF, PORT_PROF_COMMANDES);
            readerCommandes = new BufferedReader(new InputStreamReader(Serveur_Prof_Commandes.getInputStream()));
            writerCommandes = new PrintWriter(new OutputStreamWriter(Serveur_Prof_Commandes.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ResetQuestions(){
        CallableStatement cStat = null;
        try{
            cStat = CONNEXION.prepareCall(" {call TP_ORDRAGON.Reset_Questions()}");
            cStat.execute();
            cStat.clearParameters();
            cStat.close();
            System.out.println("Reset Questions");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void ResetStats(){
        CallableStatement cStat = null;
        try{
            cStat = CONNEXION.prepareCall(" {call TP_ORDRAGON.Reset_Stats()}");
            cStat.execute();
            cStat.clearParameters();
            cStat.close();
            System.out.println("Reset Stats");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
