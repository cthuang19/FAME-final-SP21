import java.util.*;

public class Util {

    private static Hashtable<String, String> my_dict = new Hashtable<String, String>() {
        {
            put("Next", "Suivant");
            put("Back", "Retour");
            put("Level ", "Niveau ");
            put("LEVEL ", "NIVEAU ");
            put("Credits", "Crédits");
            put("Exit", "Sortir");
            put("Exit ", "Sortir ");
            put("Restart", "Recommencer");
            put("Choose another level", "Choisir un autre niveau");
            put("Congrats! You found a new piece!", "Bravo ! Vous avez trouvé une nouvelle pièce !");
            put("The ghosts got you!", "Les fantômes vous ont eu !");
            put("Ship ", "Vaisseau ");
            put("Cmds", "Cmds");
            put("Ok", "Ok");

            // file names
            put("story_english.txt", "story_french.txt");
            put("ending_english.txt", "ending_french.txt");
            put("explanation_english.txt", "explanation_french.txt");
            put("puzzle_explanation_english.txt", "puzzle_explanation_french.txt");

        }
    };

    public static String convertLanguage(GameEngine.Language lang, String message) {
        if (lang == GameEngine.Language.FRENCH) {
            return my_dict.get(message);
        }
        return message;
    }
}