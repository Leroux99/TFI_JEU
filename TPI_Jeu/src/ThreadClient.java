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
    private Socket socClientActuel = null;
    private String IDClient = "";
    private CallableStatement cStat = null;
    private ResultSet resultSet = null;
    private final String SEPARATEUR = " ";
    private String noeudID = "";
    private final int PRIXQUESTION = 2;


    ThreadClient(Socket socClient) {
        socClientActuel = socClient;
    }

    @Override
    public void run() {
        try {
            readerLigneClient = new BufferedReader(new InputStreamReader(socClientActuel.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socClientActuel.getOutputStream(), "UTF-8"), true);
            Integer numQuestion;
            List<Integer> numReponses = new ArrayList<Integer>();

            // Recois le ID de celui qui est sur notre immeuble
            String[] Temp = readerLigneClient.readLine().split(SEPARATEUR);
            IDClient = Temp[0];
            noeudID = Temp[1];

            // Lui envoyer la question et les choix de réponses
            cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.GetQuestionHasard(?,?)}");

            Noeud noeudCible = new Carte().getNoeud(Integer.parseInt(noeudID));

            if (noeudCible.batiment == Noeud.typebatiment.auberge) cStat.setString(1, "F");
            else if (noeudCible.batiment == Noeud.typebatiment.manoir) cStat.setString(1, "M");
            else if (noeudCible.batiment == Noeud.typebatiment.chateau) cStat.setString(1, "D");
            else cStat.setString(1, "D"); //si l'autre équipe donne un mauvais noeud, on va les punir avec une question difficile

            cStat.registerOutParameter(2, OracleTypes.CURSOR);
            cStat.execute();
            resultSet = (ResultSet) cStat.getObject(2);
            resultSet.next();
            numQuestion = resultSet.getInt(1);
            // Envoyer la question
            writer.println(resultSet.getString(2));
            cStat.clearParameters();
            cStat.close();

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
            cStat.clearParameters();
            cStat.close();
            String ligne = readerLigneClient.readLine();
            while(ligne == null) ligne = readerLigneClient.readLine();
            System.out.println(ligne);
            // Verifier si bonne reponse
            cStat = Jeu.CONNEXION.prepareCall("{? = call TP_ORDRAGON.Verifier_Reponse(?)}");
            cStat.registerOutParameter(1, OracleTypes.INTEGER);
            // reponse
            cStat.setInt(2, numReponses.get(Integer.parseInt(ligne)-1));
            cStat.execute();

            // Envoyer si il a bien répondu ou non
            if (cStat.getInt(1) == 1) writer.println("OK");
            else {
                writer.println("ERR");
                updateOr(PRIXQUESTION);
            }
            writer.flush();
            cStat.clearParameters();
            cStat.close();
            // Dire a francois d'unlock le joueur
            Jeu.writerCommandes.println("UNLOCK " + IDClient);
            Jeu.writerCommandes.flush();
            Jeu.readerCommandes.readLine();
            // close
            readerLigneClient.close();
            writer.close();
            socClientActuel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOr(int amount){
        CallableStatement cStat = null;
        try {
            cStat = Jeu.CONNEXION.prepareCall(" {call TP_ORDRAGON.UPDATE_OR(?)}");
            cStat.setInt(1, amount);
            cStat.executeUpdate();
            Jeu.infos.UpdateStats();
            if(cStat != null){
                cStat.clearParameters();
                cStat.close();
            }
        }catch(SQLException e){}
    }
}
