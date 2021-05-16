import java.util.*;

public class Util {

    private static Hashtable<String, String> my_dict = new Hashtable<String, String>() {
        {
            put("next", "suivant");
            put("back", "retour");
            put("story_english.txt", "story_french.txt");
            put("Level ", "Niveau ");
            put("LEVEL ", "NIVEAU ");
            put("ending_english.txt", "ending_french.txt");
            put("exit", "sortie");

            //TODO: restart in french
            put("restart", "restart_french");
        } 
    };

    public static String convertLanguage(GameEngine.Language lang, String message) {
        if (lang == GameEngine.Language.FRENCH) {
            return my_dict.get(message);
        }
        return message;
    }
}