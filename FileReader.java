import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class FileReader {
    private File file;
    public FileReader(String file_name) {
        /*
        try {
            file = new File(file_name);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        */
        file = new File(file_name);
    }
    
    /**
     * helper function.
     * help read string text file
     * @return an arraylist of all the string in the text file
     */
    public ArrayList<String> allLines() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                lines.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return lines;
    }
}