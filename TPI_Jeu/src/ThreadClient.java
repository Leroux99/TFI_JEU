import oracle.jdbc.OracleTypes;

import java.io.*;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThreadClient implements Runnable {
    private BufferedReader readerLigneClient;
    private PrintWriter writer;
    private PrintWriter writerFrancois;
    private Socket socClientActuel = null;
    private String IDClient = "";
    private CallableStatement cStat = null;
    private ResultSet resultSet = null;
    private Socket Serveur_Prof;


    ThreadClient(Socket socClient) {
        socClientActuel = socClient;
    }

    @Override
    public void run() {
        try {
            Serveur_Prof = new Socket(Jeu.ADRESSE_PROF, Jeu.PORT_PROF_COMMANDES);
            readerLigneClient = new BufferedReader(new InputStreamReader(socClientActuel.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socClientActuel.getOutputStream(), "UTF-8"), true);
            writerFrancois = new PrintWriter(new OutputStreamWriter(Serveur_Prof.getOutputStream(), "UTF-8"), true);
            Integer numQuestion;
            List<Integer> numReponses = new ArrayList<Integer>();

            // Recois le ID de celui qui est sur notre immeuble
            IDClient = readerLigneClient.readLine();

            // Lui envoyer la question et les choix de réponses
            cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.GetQuestionHasard(?,?)}");
            // TODO check difficulte selon immeuble
            /*
            if (immeuble is auberge)
            {
                cStat.setString(1, "F");
            }
            else if (immeuble is manoir)
            {
                cStat.setString(1, "M");
            }
            else if (immeuble is chateau)
            {
                cStat.setString(1, "N");
            }
            */
            //Pour tester
            cStat.setString(1, "F");
            cStat.registerOutParameter(2, OracleTypes.CURSOR);
            cStat.execute();
            resultSet = (ResultSet) cStat.getObject(2);
            resultSet.next();
            numQuestion = resultSet.getInt(1);
            // Envoyer la question
            writer.println(resultSet.getString(2));

            cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.GetReponses(?,?)}");
            cStat.setInt(1, numQuestion);
            cStat.registerOutParameter(2, OracleTypes.CURSOR);
            cStat.execute();
            resultSet = (ResultSet) cStat.getObject(2);
            Integer num= 1;
            while(resultSet.next()){
                writer.println(num + " --- " + resultSet.getString(2));
                numReponses.add(resultSet.getInt(1));
                num++;
            }
            writer.println();
            String ligne = readerLigneClient.readLine();
            while(ligne == null) ligne = readerLigneClient.readLine();


            // Verifier si bonne reponse
            cStat = Jeu.CONNEXION.prepareCall("{? = call TP_ORDRAGON.Verifier_Reponse(?)}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            // reponse
            cStat.setInt(2, numReponses.get(Integer.parseInt(ligne)-1));
            cStat.execute();

            // Envoyer si il a bien répondu ou non
            if (cStat.getInt(1) == 1) {
                writer.println("OK");
            } else {
                writer.println("ERR");
                // TODO le client dois nous payer
            }
            writer.flush();
            // Dire a francois d'unlock le joueur
            writerFrancois.println("UNLOCK " + IDClient);
            writerFrancois.flush();
            // close
            readerLigneClient.close();
            writer.close();
            writerFrancois.close();
            socClientActuel.close();
            Serveur_Prof.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
