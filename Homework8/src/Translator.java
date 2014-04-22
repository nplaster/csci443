
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Translator {
	private ArrayList<String> dictionaryMap;
	
	public Translator(){
		dictionaryMap = new ArrayList<String>();
	}
	
	public String readFile(String filename) throws FileNotFoundException{
	    File file = new File(filename);
	    String fileContents = "";
	    Scanner scanner = new Scanner(file);
	    fileContents = scanner.nextLine();
	    scanner.close();
	    return fileContents;
	}
	
	public void loadDictionary() throws FileNotFoundException{
		File dictionaryFile = new File("words.txt");
		String currentWord;
		Scanner scanner = new Scanner(dictionaryFile);
		
		while(scanner.hasNextLine()){
			currentWord = scanner.nextLine();
			dictionaryMap.add(currentWord);
		}
		
		scanner.close();
	}
	
	public ArrayList matchWords(String longString){
	    ArrayList<String> res = new ArrayList<String>();
	    ArrayList<Integer> dp = new ArrayList<Integer>();
	    for(int i = longString.length(); i > -1; i--){
	    	dp.add(i);
	    	res.add(" ");
	    }
	    dp.set(0,0);
	    
	    for(int i = 0; i < longString.length(); ++i){
	    	if (dp.get(i) != -1){
	            for(int j = i+1; j <= longString.length(); ++j){
	                int len = j-i;
	                if(len>i){
		                String substr = longString.substring(i, len);
		                if(dictionaryMap.contains(substr)){
		                    res.set(i+len,res.get(i)+" "+substr);
		                    dp.set(i+len,dp.get(i)+1);
		                }
	                }
	            }
	    	}
	    }
	    System.out.println(res.toString());
		return res;
		
	}
	
	public static void main(String args[]) throws FileNotFoundException{
		Translator translator = new Translator();
		String dickens = translator.readFile("dickens.txt");
		String genesis = translator.readFile("genesis.txt");
		String sonnet = translator.readFile("sonnet.txt");
		
		ArrayList dickensArray = translator.matchWords(dickens);
		ArrayList genesisArray = translator.matchWords(genesis);
		ArrayList sonnetArray = translator.matchWords(sonnet);
		
		System.out.println("dickens.txt");
		for(int i = 0; i < dickensArray.size(); i++){
			System.out.println(dickensArray.get(i) + " ");
		}
		
		System.out.println("genesis.txt");
		for(int i = 0; i < genesisArray.size(); i++){
			System.out.println(genesisArray.get(i) + " ");
		}
		
		System.out.println("sonnet.txt");
		for(int i = 0; i < sonnetArray.size(); i++){
			System.out.println(sonnetArray.get(i) + " ");
		}
	}
		
}
