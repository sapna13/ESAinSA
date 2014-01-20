package edu.mt.sapna.sentiment;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
public class DataFile {


	String testPart;
	String trainPart;
	String currentLanguage;
	int noOfCharacters;


	public DataFile() {	
	}

	public File[] listFiles(String dataDirectory) {
		File dataFiles = null;
		try {
			dataFiles = new File(dataDirectory);
		} catch(Exception e) {
			System.out.println("Problem in Opening the directory");
		}
		return dataFiles.listFiles();		
	}

	public String getTotalTextContent(File directory) {
		int i = 0;
		StringBuffer collectingTotalText = new StringBuffer();
		File[] dataFiles = directory.listFiles();
		int length = dataFiles.length;		
		while(i < length) {
			collectingTotalText.append(extractText(dataFiles[i]));			            
			collectingTotalText.append(" ");		
			i++;			
		}		
		return collectingTotalText.toString();
	}

	public String extractText(String filePath) {
		File file = new File(filePath);
		return (extractText(file));
	}
	public static String extractText(File file) {
		FileInputStream inputFile = null;
		InputStreamReader streamRead = null;
		BufferedReader read = null;		
		StringBuffer collectingText = new StringBuffer();
		String line;
		try {		
			inputFile = new FileInputStream(file.getAbsolutePath());
			streamRead = new InputStreamReader(inputFile);
			read = new BufferedReader(streamRead);
		} catch(Exception e) {
			System.out.println("Error in Opening a Data File");
		}
		
		try {
			while ((line = read.readLine())!= null) {
				collectingText.append(line + "\n");
			} 
		} catch(Exception e) {
			System.out.println("Problem in reading a file");
		}
		return collectingText.toString();
	}

	public HashMap<String,Integer> getTokenFrequencyMap(File file) {
		String text = extractText(file);
		return getTokenFrequencyMap(text);
	}		

	public HashMap<String,Integer> getTokenFrequencyMap(String toTokenMap) {
		HashMap<String, Integer> tokenFrequencyMap = new HashMap<String, Integer>();
		return updateTokenFrequencyMap(tokenFrequencyMap, toTokenMap);
	}	

	//with stemmer
	/*public HashMap<String,Integer> updateTokenFrequencyMap(HashMap<String, Integer> tokenFrequencyMap, String toTokenMap) {		
		String text = toTokenMap;
		text = text.toLowerCase();
		StringTokenizer tokens = new StringTokenizer(text);
		//MyStemmer stemIt = new MyStemmer();
		int noOfTokens = tokens.countTokens();
		int j = 0;
		while (j<noOfTokens) {
			String nextToken = tokens.nextToken();
			//nextToken = stemIt.stemIt(nextToken);			
			Integer tokenFrequency = tokenFrequencyMap.get(nextToken); 
			if(tokenFrequency == null) {
				tokenFrequency = 0;
			} 
			tokenFrequencyMap.put(nextToken, tokenFrequency+1);
			j++;
		}
		return tokenFrequencyMap;
	}*/	

	
	

	public HashMap<String,Integer> updateTokenFrequencyMap(HashMap<String, Integer> tokenFrequencyMap, String toTokenMap) {		
		String text = toTokenMap;
		StringTokenizer tokens = new StringTokenizer(text);
		int noOfTokens = tokens.countTokens();
		int j = 0;
		while (j<noOfTokens) {
			String nextToken = tokens.nextToken();
			Integer tokenFrequency = tokenFrequencyMap.get(nextToken); 
			if(tokenFrequency == null) {
				tokenFrequency = 0;
			} 
			tokenFrequencyMap.put(nextToken, tokenFrequency+1);
			j++;
		}
		return tokenFrequencyMap;
	}	
	

	//with stemmer
	public HashMap<String,Integer> updateBinaryTokenFrequencyMap(HashMap<String, Integer> tokenFrequencyMap, String toTokenMap) {		
		//MyStemmer stemIt = new MyStemmer();
		String text = toTokenMap;
		text = text.toLowerCase();
		StringTokenizer tokens = new StringTokenizer(text);
		StringTokenizer tokens1 = new StringTokenizer(text);
		if(tokens1.hasMoreTokens()) {
			tokens1.nextToken();
		}
		while (tokens1.hasMoreTokens()) {
			String nextToken = tokens.nextToken();
		//	nextToken = stemIt.stemIt(nextToken);
			String nextnextToken = tokens1.nextToken();
			//nextnextToken = stemIt.stemIt(nextnextToken);			
			//String binaryToken = nextToken + " " + nextnextToken;
			String binaryToken = nextToken + "_" + nextnextToken;
			Integer tokenFrequency = tokenFrequencyMap.get(binaryToken); 
			if(tokenFrequency == null) {
				tokenFrequency = 0;
			} 
			tokenFrequencyMap.put(binaryToken, tokenFrequency+1);
		}
		String last = null;
		
		if(tokens.hasMoreTokens()) {
			last = tokens.nextToken();
		}
		Integer lastFreq = tokenFrequencyMap.get(last); 
		if(lastFreq == null) {
			lastFreq = 0;
		}
		tokenFrequencyMap.put(last + " Integer", lastFreq+1);
		return tokenFrequencyMap;	
	}	


	
	public HashMap<String,Integer> updateBinaryTokenFrequencyMap(HashMap<String, Integer> tokenFrequencyMap, File file) {		
		String text = extractText(file);
		return updateBinaryTokenFrequencyMap(tokenFrequencyMap, text);	
	}	

	
	
	//with stemmer
	public HashMap<String,Integer> updateTertiaryTokenFrequencyMap(HashMap<String, Integer> tokenFrequencyMap, String toTokenMap) {		
		//MyStemmer stemIt = new MyStemmer();

		String text = toTokenMap;
		StringTokenizer tokens = new StringTokenizer(text);
		StringTokenizer tokens1 = new StringTokenizer(text);
		StringTokenizer tokens2 = new StringTokenizer(text);
		
		tokens1.nextToken();
		tokens2.nextToken();
		tokens2.nextToken();
		
		while (tokens2.hasMoreTokens()) {
			String nextToken = tokens.nextToken();
			//nextToken = stemIt.stemIt(nextToken);
			String nextnextToken = tokens1.nextToken();
			//nextnextToken = stemIt.stemIt(nextnextToken);
			String nextnextnextToken = tokens2.nextToken();
			//nextnextnextToken = stemIt.stemIt(nextnextnextToken);
			
			String tertiaryToken = nextToken + " " + nextnextToken+ " " + nextnextnextToken;
			Integer tokenFrequency = tokenFrequencyMap.get(tertiaryToken); 
			if(tokenFrequency == null) {
				tokenFrequency = 0;
			} 
			tokenFrequencyMap.put(tertiaryToken, tokenFrequency+1);
		}
		
		
		String secondLast = tokens.nextToken();
		String last = tokens.nextToken();
		String lastTertiary = secondLast + " " + last;
		Integer lastFreq = tokenFrequencyMap.get(lastTertiary); 
		if(lastFreq == null) {
			lastFreq = 0;
		}
		tokenFrequencyMap.put(lastTertiary + " null", lastFreq+1);
		return tokenFrequencyMap;	
	}	

	
	
	//with stemmer etc.
	public HashMap<String,Integer> getBinaryTokenFrequencyMap(String toTokenMap) {
		HashMap<String, Integer> tokenFrequencyMap = new HashMap<String, Integer>();
		return updateBinaryTokenFrequencyMap(tokenFrequencyMap, toTokenMap);
	}		
	
	//with stemmer etc.
	public HashMap<String,Integer> getBinaryTokenFrequencyMap(File file) {
		HashMap<String, Integer> tokenFrequencyMap = new HashMap<String, Integer>();
		String toTokenMap = extractText(file);
		return updateBinaryTokenFrequencyMap(tokenFrequencyMap, toTokenMap);
	}		

	
	
	/*public HashMap<String,Integer> getBinaryTokenFrequencyMap(String toTokenMap) {
		HashMap<String, Integer> tokenFrequencyMap = new HashMap<String, Integer>();  
		String text = toTokenMap;
		StringTokenizer tokens = new StringTokenizer(text);
		StringTokenizer tokens1 = new StringTokenizer(text);
		tokens1.nextToken();
		while (tokens1.hasMoreTokens()) {
			String nextToken = tokens.nextToken();
			String nextnextToken = tokens1.nextToken();		
			String binaryToken = nextToken + " " + nextnextToken;
			Integer tokenFrequency = tokenFrequencyMap.get(binaryToken); 
			if(tokenFrequency == null) {
				tokenFrequency = 0;
			} 
			tokenFrequencyMap.put(binaryToken, tokenFrequency+1);
		}
		String last = tokens.nextToken();
		Integer lastFreq = tokenFrequencyMap.get(last); 
		if(lastFreq == null) {
			lastFreq = 0;
		}
		tokenFrequencyMap.put(last + " null", lastFreq+1);
		return tokenFrequencyMap;
	}*/		
	
	
	public HashMap<String,Integer>[] getTokenFrequencyMaps(File[] directories) {
		int noOfDirectories = directories.length;
		int i = 0;
		HashMap<String, Integer>[] maps = new HashMap[noOfDirectories];		
		String textInDirectory = "";			
		while(i != noOfDirectories) {
			textInDirectory = getTotalTextContent(directories[i]);
			maps[i] = new HashMap<String, Integer>();
			maps[i] = getTokenFrequencyMap(textInDirectory);
			i++;
		}		
		return maps;
	}	
	
	public HashMap<String,Integer>[] getBinaryTokenFrequencyMaps(File[] directories) {
		int noOfDirectories = directories.length;
		int i = 0;
		HashMap<String, Integer>[] maps = new HashMap[noOfDirectories];		
		String textInDirectory = "";			
		while(i != noOfDirectories) {
			textInDirectory = getTotalTextContent(directories[i]);
			maps[i] = new HashMap<String, Integer>();
			maps[i] = getBinaryTokenFrequencyMap(textInDirectory);
			i++;
		}		
		return maps;
	}
	

	public int countDistinctTokenInAllMaps(HashMap<String,Integer>[] map) {		
		HashMap<String, Integer> totalTokenMap = new HashMap<String,Integer>();
		int noOfMaps = map.length;
		int i = 0;
		while(i<noOfMaps) {
			totalTokenMap.putAll(map[i++]);
		}
		return totalTokenMap.size();
	}	

	public int countDistinctTokenInAllMaps(ArrayList <HashMap<String,Integer>> map) {		
		HashMap<String, Integer> totalTokenMap = new HashMap<String,Integer>();
		int noOfMaps = map.size();
		int i = 0;
		while(i<noOfMaps) {
			totalTokenMap.putAll(map.get(i++));
		}
		return totalTokenMap.size();
	}	
	
	public int[] countTotalTokensInMaps(HashMap<String,Integer>[] map) {
		int noOfMaps = map.length;
		int i = 0;
		int[] count = new int[noOfMaps];
		while(i<noOfMaps) {
			count[i] = countTotalTokensInMap(map[i]) + count[i];
			i++;
		}
		return count;			
	}

	public int countTotalTokensInMap(HashMap<String,Integer> map) {
		Integer count = 0;
		Collection<Integer> values = map.values();		
		Iterator<Integer> iterate = values.iterator();
		while (iterate.hasNext()) {
			count = (Integer) iterate.next() + count;
		}
		return count.intValue();
	}	


	public void wordAppears(String word, String directoryPath) {
		File directory = new File(directoryPath);
		File[] files = directory.listFiles(); 
		int i = 0;
		while(i<files.length) {
			String text = extractText(files[i]);
			if(text.contains(word)) {
				System.out.println("****" + text);
			}
			i++;
		}		
		System.out.println();
	}


	public DataFile[] breakUpCorpus(File corpusDirectory, double testingPercentage)	{

		File[] languageFiles = null;
		DataFile[] langFileData = null;
		int noOfFiles;
		int start = 0;		
		languageFiles = corpusDirectory.listFiles();		
		noOfFiles = languageFiles.length;
		langFileData = new DataFile[noOfFiles];		

		for (int i=0;i<noOfFiles;i++) {						
			currentLanguage = languageFiles[i].getName();
			System.out.println(currentLanguage);
			langFileData[i] = breakUpFile(languageFiles[i],testingPercentage, start);
			langFileData[i].currentLanguage = currentLanguage;			
		}		
		return langFileData;
	}	


	public DataFile breakUpFile(File languageCorpusFile,double testingPercentage, int startIndexOfTest) {

		DataFile langFile = new DataFile();
		String fileContents = "";
		int testTextLength;		
		FileInputStream inputFile = null; 
		InputStreamReader input = null;
		BufferedReader readFile = null;		
		try {
			inputFile = new FileInputStream(languageCorpusFile.getAbsolutePath());
			input = new InputStreamReader(inputFile, "UTF-8");			
			readFile = new BufferedReader(input);
		} catch(Exception e) {
			System.out.print("Problem in Opening the File");
		}
		StringBuffer buffer = new StringBuffer();
		try {
			while((fileContents = readFile.readLine())!=null) {
				buffer.append(fileContents);
			}
		} catch(Exception e) {
			System.out.println("Problem in Reading the File");
		}

		testTextLength = (int) (testingPercentage * buffer.length());
		String stringBuffer = buffer.toString();
		langFile.testPart = getTestTextPart(stringBuffer,startIndexOfTest, testTextLength);				
		langFile.trainPart = getTrainTextPart(stringBuffer, startIndexOfTest, startIndexOfTest+testTextLength);
		langFile.noOfCharacters = noOfCharacters(stringBuffer);
		return langFile;
	}	


	public int noOfCharacters (String fileContents) {
		HashMap<Character, Integer> charCount = new HashMap<Character, Integer>();
		for (char character : fileContents.toCharArray()) {
			Integer charCounts = charCount.get(character);
			if (charCounts == null) {
				charCounts = 0;
			}
			charCount.put(character, charCounts + 1);
		}		
		return charCount.size();	
	}

	public String getTestTextPart(String fileContents, int start, int testTextLength) {
		String testText = fileContents.substring(start, start + testTextLength);
		return testText;				
	}

	public String getTrainTextPart(String fileContents, int firstEnd, int secondStart) {
		String trainText = fileContents.substring(0, firstEnd) + fileContents.substring(secondStart);
		return trainText;				
	}

	public HashMap<String, Integer>  pickTopValues(HashMap<String, Integer> map, int noOfValues) {
		HashMap<String, Integer> sortedMap = new HashMap<String, Integer>();
		int i = 0;
		ArrayList<String> keys = new ArrayList<String>(map.keySet());
		ArrayList<Integer> values = new ArrayList<Integer>(map.values());
		Collections.sort(values);
		Collections.reverse(values);
		Iterator<Integer> valueIterator = values.iterator();
		while(valueIterator.hasNext() && i++ != noOfValues) {
			Integer value = valueIterator.next();
			Iterator<String> keyIterator = keys.iterator();
			while(keyIterator.hasNext()) {
				String key = keyIterator.next();
				if(map.get(key)== value) {
					sortedMap.put(key, value);
					keys.remove(key);
					keyIterator = keys.iterator();
				}
			}
		}		
		return sortedMap;
	}
	
	/*public String removePunctuations(String toRemoveFrom) {
		char[] punctuationMarks = {',', '"','.','?','&','!'};				
		
	}*/	
	
	public  String removeStopWordTokens(String toRemoveFrom) {
		String stopWordsString = "the I ' , ? & !)";
		StringBuffer newStringBuffer = new StringBuffer();
		StringTokenizer tokens = new StringTokenizer(toRemoveFrom);
		while(tokens.hasMoreElements()) {
			int i = 1;
			String nextToken = tokens.nextToken();
			StringTokenizer stopWordsTokenizer = new StringTokenizer(stopWordsString);
			while(stopWordsTokenizer.hasMoreElements()) {
				String stopWord = stopWordsTokenizer.nextToken();				
				if(nextToken.equalsIgnoreCase(stopWord)) {
					i = 0;
				}				 
			}
			if(i == 1) {
				newStringBuffer.append(nextToken + " ");
			}
		}
		return newStringBuffer.toString();
	}


	
	/*public  StringTokenizer removeStopWordTokens(String toRemoveFrom) {
		StringTokenizer token = new StringTokenizer(toRemoveFrom);			
		
	}*/
	
}
