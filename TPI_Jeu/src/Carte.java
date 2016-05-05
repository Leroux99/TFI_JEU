import javafx.scene.shape.Line;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Carte {
    List<Noeud> Noeuds = new ArrayList<Noeud>();
    List<Line> LigneChemins = new ArrayList<Line>();

    Carte(){
        AjouterNoeuds();
    }

    private void AjouterNoeuds() {
        String ligne;
        Boolean isNoeuds = true;
        try {
            Socket soc = new Socket(Jeu.ADRESSE_PROF, Jeu.PORT_PROF_CARTE);

            BufferedReader reader
                    = new BufferedReader(
                    new InputStreamReader(soc.getInputStream()));
            do {
                ligne = reader.readLine();
                if (ligne != null && ligne.trim().length() > 0) {
                    String[] params = ligne.split(" ");
                    Boolean Constructible = Integer.parseInt(params[3]) == 1 ? true : false;
                    Noeuds.add(new Noeud(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Constructible));
                }
                else isNoeuds = false;
            } while (isNoeuds);

            do{
                ligne = reader.readLine();
                if (ligne != null && ligne.trim().length() > 0) {
                    String[] params = ligne.split(" ");
                    Noeud Noeud_Depart = getNoeud(Integer.parseInt(params[0]));
                    if(Noeud_Depart != null){
                        for(int i = 1; i < params.length; i++){
                            Noeud Noeud_Fin = getNoeud(Integer.parseInt(params[i]));
                            if(Noeud_Fin !=null) {
                                LigneChemins.add(new Line(Noeud_Depart.getCenterX(), Noeud_Depart.getCenterY(),
                                        Noeud_Fin.getCenterX(), Noeud_Fin.getCenterY()));
                                Noeud_Depart.Chemins.add(Noeud_Fin.getID());
                            }
                        }
                    }
                }
            }while (ligne != null);
            soc.close();
        }catch(IOException e){}
    }

    public List<Noeud> getNoeuds(){return Noeuds;}

    public List<Line> getLigneChemins(){return LigneChemins;}

    public Noeud getNoeud(int ID){
        for(int i = 0; i < Noeuds.size(); i++) {
            if (Noeuds.get(i).getID() == ID) return Noeuds.get(i);
        }
        return null;
    }

    private int getNoeudIndex(int ID){
        for(int i = 0; i < Noeuds.size(); i++) if (Noeuds.get(i).getID() == ID) return i;
        return -1;
    }

    public void UpdateCarte(List<String> Infos){
        for(Noeud n : Noeuds) n.ClearInfos();
        for(int i = 0; i < Infos.size(); i++){
            String[] Temp = Infos.get(i).split(":");
            try {
                if (Temp[1].equals("J")) Noeuds.get(getNoeudIndex(Integer.parseInt(Temp[0]))).Joueurs++;
                if (Temp[1].equals("T")) Noeuds.get(getNoeudIndex(Integer.parseInt(Temp[0]))).Troll = true;
                if (Temp[1].equals("G")) Noeuds.get(getNoeudIndex(Integer.parseInt(Temp[0]))).Gobelin = true;
                if (Temp[1].equals("P")) Noeuds.get(getNoeudIndex(Integer.parseInt(Temp[0]))).Or = true;
                if (Temp[1].equals("M")) Noeuds.get(getNoeudIndex(Integer.parseInt(Temp[0]))).MountainDew = true;
                if (Temp[1].equals("D")) Noeuds.get(getNoeudIndex(Integer.parseInt(Temp[0]))).Doritos = true;
            }
            catch(Exception e){}
        }



    }
}
