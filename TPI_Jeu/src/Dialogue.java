
public class Dialogue {

    public static void Show(Noeud noeud) {
        try {
            if (Jeu.joueur.Position == noeud) {
                new Actions().Show(noeud);
            } else {
                Jeu.joueur.seDeplacer(noeud);
            }
        }catch(Exception e){}
    }

    public static void ShowQuestion(){
        Question.Show();
    }
}
