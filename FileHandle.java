
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileHandle {

    //Read key words from kywrdsOdd file and return it as a Array List
    public ArrayList<String> fileReader(){
        ArrayList<String> readWords = new ArrayList<String>();
        try {
            File kywrdsOdd = new File("D:\\STUDY\\L4\\EEX4465\\mp\\EEX4465-2020_MP\\kywrdsOdd.txt");
            Scanner fileScan = new Scanner(kywrdsOdd);
            while (fileScan.hasNextLine()){
                String textWord = fileScan.nextLine();
                readWords.add(textWord);
            }
            fileScan.close();
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        return  readWords;
    }
    // Calculate frequency of first letter and last letter of the keywords and return the values as a hashmap
    public HashMap<Character, Integer> letterFrequencyCounter(){
        HashMap<Character, Integer> lettersValue = new HashMap<Character, Integer>();
        ArrayList<String> words = fileReader();
        int count;
        for (int w=0; w<words.size(); w++){
            String word = words.get(w);
            for (int i=0; i<word.length(); i++) {
               if (i==0 || i== word.length()-1){
                   if(!lettersValue.containsKey(word.charAt(i))){
                       lettersValue.put(word.charAt(i),1);
                   }
                   else if(lettersValue.containsKey(word.charAt(i))) {
                       count = lettersValue.get(word.charAt(i));
                       count = count +1;
                       lettersValue.replace(word.charAt(i), count);
                   }
               }
            }
        }
        return lettersValue;
    }

    //Calculate the word value by sum of first letter value and last letter value of each keyword and return it as a hashmap
    public HashMap<String,Integer> wordValueCalculator(){

        HashMap<Character,Integer> characterValues = letterFrequencyCounter();
        ArrayList<String> words = fileReader();
        HashMap<String, Integer> wordValues = new HashMap<String, Integer>();
        int firstLetterValue, lastLetterValue, totalLetterValue;

        for (int w=0; w<words.size(); w++){
            String word = words.get(w);
             firstLetterValue = characterValues.get(word.charAt(0));
             lastLetterValue = characterValues.get(word.charAt(word.length()-1));
             totalLetterValue = firstLetterValue + lastLetterValue;
             wordValues.put(word, totalLetterValue);
        }
        return wordValues;
        //Key -> keyword | value -> sum of first letter value last letter value
    }

    public HashMap<String, Integer> mapSortByValue(){
        // Sort key words values by descending oder and put those values in to linked hash map and return it
        HashMap<String, Integer> unSortedMap = wordValueCalculator();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

    public ArrayList<String> dataStructureConverter(){
        // Put keyword that sorted in to descending oder in to array list
        HashMap<String,Integer> sortedMap = mapSortByValue();
        Set<String> keyValues = sortedMap.keySet();
        ArrayList<String> sortedWords = new ArrayList<>(keyValues);
        return sortedWords;
    }
    public HashMap<Character, Integer> setLetterFrequencyZero(){
        //  Set all character values as zero and return it for hash table calculation
        HashMap<Character, Integer> letterValues = letterFrequencyCounter();
        letterValues.replaceAll((key, value) -> 0);
        return letterValues;
    }
    public Hashtable<Integer, String> hashTableGenerator(){
        // Create Perfect Hash Table
        Hashtable<Integer, String> perfectHashTable = new Hashtable<>();
        ArrayList<String>sortedArrayList = dataStructureConverter();
        String[] wordsArray = new String[sortedArrayList.size()];
        for (String word: sortedArrayList){
            int hValue = hValueFinder(word, setLetterFrequencyZero(),wordsArray, sortedArrayList);
            wordsArray[hValue] = word;
            perfectHashTable.put(hValue, word);
        }
        return perfectHashTable;
    }

    public int hValueFinder(String word, HashMap<Character, Integer>characterValues, String[] wordsArray, ArrayList<String>sortedWordList){
        //Find hash Value in each key word and return it

        int gFirstValue = characterValues.get(word.charAt(0));
        int gLastValue = characterValues.get(word.charAt(word.length()-1));
        int wordLength = word.length();
        int hValue = (wordLength + gFirstValue + gLastValue)%10;

        if (wordsArray[hValue] !=null && gFirstValue<=4){
            gFirstValue++;
            characterValues.replace(word.charAt(0),gFirstValue);
            hValue = hValueFinder(word, characterValues, wordsArray,sortedWordList);
        }
        else if (wordsArray[hValue] != null && gFirstValue>4){
            int previousIndex = sortedWordList.indexOf(word)-1;
            try {
                String previousElement = sortedWordList.get(previousIndex);
                characterValues.put(word.charAt(0), gFirstValue+1);
                hValue = hValueFinder(word, characterValues, wordsArray, sortedWordList);
                if (wordsArray[hValue] != null && gFirstValue<=4){
                    characterValues.put(previousElement.charAt(0),gFirstValue+1);
                }
                else {
                    wordsArray[hValue] = previousElement;
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println(e);
            }
        }
        return hValue;
    }
    public Hashtable<String, Integer> statisticsResults() {
        Hashtable <String, Integer> keyWordCount = new Hashtable<>();
        HashMap<String, Integer> keywords = mapSortByValue();
        int wordCount=0;
        int lineCount = 0;
        try {
            File tstOdd = new File("D:\\STUDY\\L4\\EEX4465\\mp\\EEX4465-2020_MP\\tstOdd.txt");
            Scanner fileScan = new Scanner(tstOdd);
            while (fileScan.hasNextLine()) {
                String textWords = fileScan.nextLine().replaceAll("[(),.=?]","").replaceAll("\\d+","");

                String[] textLine = textWords.split("\\s+");
                if (textWords.length() != 0){
                    lineCount++;
                }
                for (String w: textLine){
                    wordCount++;
                    if (keywords.containsKey(w)){
                        if (!keyWordCount.containsKey(w)){
                            keyWordCount.put(w, 1);
                        }
                        else if (keyWordCount.containsKey(w)){
                            int count = keyWordCount.get(w);
                            count++;
                            keyWordCount.replace(w, count);
                        }
                    }
                }
            }
            fileScan.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
            }
        System.out.println("    Statistics results :  ");
        System.out.println("---------------------------------");
        System.out.println("\tTotal lines Read " +lineCount);
        System.out.println("\tTotal words read " +wordCount);
        System.out.println("\tBreakdown by keyword");
        Set<String> keyCount = keyWordCount.keySet();
        int total = 0;
        for (String key: keyCount){
            System.out.println("\t"+key +" : "+ keyWordCount.get(key) );
            total = total + keyWordCount.get(key);
        }
        System.out.println("\tTotal keywords " + total);


        return keyWordCount;

    }


    public static void main(String[] args) {
        FileHandle fh = new FileHandle();
        long startTime = System.currentTimeMillis();
        fh.fileReader();
        fh.letterFrequencyCounter();
        fh.wordValueCalculator();
        fh.mapSortByValue();
        fh.setLetterFrequencyZero();
        fh.dataStructureConverter();
        fh.hashTableGenerator();
        long finishedTime = System.currentTimeMillis();
        long ExecutionTime = finishedTime - startTime;

        System.out.println(fh.fileReader());
        System.out.println(fh.letterFrequencyCounter());
        System.out.println(fh.wordValueCalculator());
        System.out.println(fh.mapSortByValue());
        System.out.println(fh.setLetterFrequencyZero());
        System.out.println(fh.dataStructureConverter());
        System.out.println(fh.hashTableGenerator());
        fh.statisticsResults();
        System.out.println("\tExecution time: "+ ExecutionTime+" milliseconds");

    }
}
