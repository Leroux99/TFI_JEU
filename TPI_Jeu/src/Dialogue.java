
public class Dialogue {

    public static void Show(Noeud noeud) {
        try {
            if (Jeu.joueur.Position == noeud) {
                new Actions().Show(noeud);
            } else {
                new Infos().Show(noeud);
            }
        }catch(Exception e){}
    }

    public static void ShowQuestion(){
        Question.Show();
    }
}
