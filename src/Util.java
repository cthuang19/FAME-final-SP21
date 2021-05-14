import java.util.*;

public class Util {

    public static String convertLanguage(GameEngine.Language lang, String message) {
        Hashtable<String, String> my_dict = new Hashtable<String, String>();
        my_dict.put("next", "suivant");
        my_dict.put("back", "retour");
        my_dict.put("story_english.txt", "story_french.txt");
        my_dict.put("Level ", "Niveau ");
        my_dict.put("LEVEL ", "NIVEAU ");
        if (lang == GameEngine.Language.FRENCH) {
            return my_dict.get(message);
        }
        return message;
    }
}