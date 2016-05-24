import java.util.ArrayList;
import java.util.List;

public class Bot implements Runnable {
    public Boolean actif;
    public Boolean isWorking;
    private final Integer NEUTRAL = 0;
    private final Integer MONSTRE = -5;

    public Bot(){
        actif = true;
        isWorking = false;
    }

    @Override
    public void run() {
        try {
            //Thread.sleep(500);
            Noeud lastPosition = Jeu.joueur.Position;
            while (actif) {
                isWorking = true;
                List<Noeud> neutralNoeuds = new ArrayList<Noeud>();
                List<Noeud> maisonNoeuds = new ArrayList<Noeud>();
                Integer bestPTS = -10;
                Noeud bestNoeud = null;
                while (bestPTS <= MONSTRE && maisonNoeuds.size() != Jeu.joueur.Position.Chemins.size()){
                    neutralNoeuds.clear();
                    maisonNoeuds.clear();
                    for (Integer chemin_ID : Jeu.joueur.Position.Chemins) {
                        Noeud noeud = Jeu.carte.getNoeud(chemin_ID);
                        Integer pts = NEUTRAL;
                        if (Jeu.joueur.Position.Chemins.size() == 1) bestNoeud = noeud;
                        if (noeud == lastPosition && Jeu.joueur.Position.Chemins.size() > 1) pts-=2;
                        if (noeud.Chemins.size() == 1 && !noeud.Or && !noeud.Doritos && !noeud.MountainDew) pts-=2;
                        if (noeud.Troll) pts += MONSTRE;
                        if (noeud.Gobelin) pts += MONSTRE;
                        if (noeud.Or) pts += 2;
                        if (noeud.MountainDew) pts++;
                        if (noeud.Doritos) pts++;
                        if (noeud.batiment != Noeud.typebatiment.aucun){
                            int bonus = 0;
                            if(Jeu.joueur.listeBatiments.size() >= 3) bonus = 1;
                            Boolean notreBatiment = false;
                            for(int i = 0; i < Jeu.joueur.listeBatiments.size() && !notreBatiment; i++){
                                if(noeud.ID == Jeu.joueur.listeBatiments.get(i)) notreBatiment = true;
                            }
                            if(notreBatiment) pts+= 1 + bonus;
                            else{
                                pts-=2;
                                maisonNoeuds.add(noeud);
                            }
                        }
                        if(pts == NEUTRAL) neutralNoeuds.add(noeud);
                        if (pts > bestPTS) {
                            bestPTS = pts;
                            bestNoeud = noeud;
                        }
                    }
                }
                if(maisonNoeuds.size() == Jeu.joueur.Position.Chemins.size()) bestNoeud = randomNeutralNoeud(maisonNoeuds);
                else if(bestPTS == NEUTRAL) bestNoeud = randomNeutralNoeud(neutralNoeuds);
                lastPosition = Jeu.joueur.Position;
                Jeu.joueur.seDeplacer(bestNoeud);

                essayerConstruire();
                Thread.sleep(1);
                isWorking = false;
            }
        }catch(InterruptedException ie) {}
    }

    private Noeud randomNeutralNoeud(List<Noeud> liste){
        return liste.get((int) (Math.random() * liste.size()));
    }

    private void essayerConstruire(){
        if(Jeu.actions.getOr() >= Jeu.actions.PRIXBATIMENT && Jeu.joueur.Position.Constructible) {
            if (Jeu.joueur.listeBatiments.size() < 3) Jeu.actions.gererClic(null);
            else {
                Boolean hasBuilt = false;
                for (int i = 0; i < Jeu.joueur.listeBatiments.size() && !hasBuilt; i++)
                    if (Jeu.joueur.Position.ID == Jeu.joueur.listeBatiments.get(i)) {
                        Jeu.actions.gererClic(null);
                        hasBuilt = true;
                    }
            }
        }
    }
}