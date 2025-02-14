package fileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileProcessor {

    private List<String> stringsLst = new ArrayList<>();
    private List<Integer> integersLst = new ArrayList<>();
    private List<Double> doublesLst = new ArrayList<>();

    public void fileProcessing(String filePath) throws IOException{
        File file = new File(filePath);
        String line;
        try(Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()){
                line = sc.nextLine().trim();
                if(line.isEmpty()) return;
                try{
                    integersLst.add(Integer.parseInt(line));
                }catch (RuntimeException ex){
                    try{
                        doublesLst.add(Double.parseDouble(line));
                    }catch (RuntimeException exc){
                        stringsLst.add(line);
                    }
                }
            }
        }

    }

    public List<String> getStringsLst() {
        return stringsLst;
    }

    public List<Integer> getIntegersLst() {
        return integersLst;
    }

    public List<Double> getDoublesLst() {
        return doublesLst;
    }
}
