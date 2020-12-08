import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class PIF {
    private String codes;
    private String filename;

    public PIF(String filename) {
        this.filename = filename;
        codes = "";
        readFromFile();
        System.out.println(codes);
    }

    private void readFromFile() {
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                ArrayList<String> words = Arrays.stream(data.split(" : "))
                        .map(String::trim)
                        .collect(Collectors.toCollection(ArrayList::new));
                if(codes.length()==0)
                    codes = words.get(0);
                else
                    codes+=" "+words.get(0);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getCodes() {
        return codes;
    }
}
