import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class NotreServeur implements Runnable{
    private int port = 1666;
    private ServerSocket socServeur;
    private Socket socClient;

    public void run() {
        // Créer le serveur
        creerServeur();
        try {
            boolean fini = false;
            while (!fini) {
                try {
                        // Étape 1 : attente d'une demande d'un client (connexion)
                        socClient = socServeur.accept();
                        // Étape 2: créé thread client
                        Thread tClient = new Thread(new ThreadClient(socClient));
                        tClient.start();

                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                }
            }
            // Étape 3 : fermeture du socket serveur
            socServeur.close();
            System.exit(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private void creerServeur() {
        try {
            // Étape 1 : création du socket socServeur
            socServeur = new ServerSocket(port);
        } catch (SocketException e) {
            System.out.println("Erreur : port utilisé par une autre application");
            System.exit(1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }
}
