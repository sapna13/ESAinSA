package edu.mt.sapna.sentiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import edu.mt.sapna.otdf.OTDFFile;
import edu.mt.sapna.otdf.OTDFXmlReader;
import edu.mt.sapna.utils.PTBTokenizer;
import edu.mt.sapna.utils.TextNormalizer;
import edu.mt.sapna.utils.Token;
import edu.upm.clesa.impl.CLESA;
import eu.monnetproject.clesa.core.lang.Language;
import eu.monnetproject.clesa.core.utils.BasicFileTools;
import eu.monnetproject.clesa.core.utils.Pair;

public class SA {

	private static PTBTokenizer tokenizer = new PTBTokenizer();

	private static Properties config = new Properties();


	private static List<Token> normalize(String text){	
		text = text.replace("\t", " ");
		text = text.toLowerCase();
		text = TextNormalizer.convertToUnicode(text);		
		text = TextNormalizer.removePunctuations(text);		
		text = text.replaceAll("\n", " ").trim();		
		text = TextNormalizer.deAccent(text);
		return tokenizer.tokenize(text);
	}

	public static Set<String> subtractSecondFromFirst(Set<String> set1, Set<String> set2) {
		Set<String> buffer1 = new HashSet<String>(set1);
		Set<String> buffer2 =  new HashSet<String>(set2);
		buffer1.removeAll(buffer2);	
		return buffer1;
	}

	public static String joinString(Set<String> set) {
		int n = 10000;
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for(String string : set) {
			i ++;

			if(i<n)
				buffer.append(string + " ");
			else 
				break;
		}
		return buffer.toString().trim();
	}


	private static void loadConfig(String configFilePath){
		try {
			config.load(new FileInputStream(configFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}	


	public static void main(String[] args) throws IOException {
		String xml = "SentimentData.xml";
		String testSentence = "I am happy and like apples";
		List<Token> testTokens = normalize(testSentence);
		String testText = TextNormalizer.joinTokens(testTokens);
		loadConfig("load/SentiSemeval.properties");
		int howMany = Integer.parseInt(config.getProperty("howMany"));
		CLESA clesa = new CLESA();		
		StringBuffer pos = new StringBuffer();
		StringBuffer neg = new StringBuffer();

		OTDFXmlReader reader = new OTDFXmlReader(xml);
		Iterator<OTDFFile> iterator = reader.getIterator();
		while(iterator.hasNext()) {
			OTDFFile otdfFile = iterator.next();
			String sentiTag = otdfFile.getFeatureValue("SentiTag");
			String text = otdfFile.getFeatureValue("Text");
			if(sentiTag.trim().equalsIgnoreCase(DataCollector.POSITIVE)) 
				pos.append(text + " ");
			if(sentiTag.trim().equalsIgnoreCase(DataCollector.NEGATIVE)) 
				neg.append(text + " ");
		}

		BasicFileTools.writeFile("pos.txt", pos.toString());
		BasicFileTools.writeFile("neg.txt", neg.toString());

		String posText = BasicFileTools.extractText("pos.txt");
		String negText = BasicFileTools.extractText("neg.txt");

		Set<String> posTokens = new HashSet<String>();
		Set<String> negTokens = new HashSet<String>();

		StringTokenizer tokenizer = new StringTokenizer(posText);
		while(tokenizer.hasMoreTokens()) 
			posTokens.add(tokenizer.nextToken());

		tokenizer = new StringTokenizer(negText);
		while(tokenizer.hasMoreTokens()) 
			negTokens.add(tokenizer.nextToken());

		System.out.println(posTokens.size());
		System.out.println(negTokens.size());		

		Set<String> filteredPosTokens = subtractSecondFromFirst(posTokens, negTokens);
		Set<String> filteredNegTokens = subtractSecondFromFirst(negTokens, posTokens);

		System.out.println(filteredPosTokens.size());
		System.out.println(filteredNegTokens.size());		


		//		Set<String> testPosSet = subtractSecondFromFirst(filteredPosTokens, filteredNegTokens);
		//		
		//
		//		Set<String> testNegSet = subtractSecondFromFirst(filteredNegTokens, filteredPosTokens);
		//
		//		System.out.println(testPosSet.size());			
		//
		//		System.out.println(testNegSet.size());			
		//


		posText = joinString(filteredPosTokens);
		negText = joinString(filteredNegTokens);


		Pair<String, Language> posPair = new Pair<String, Language>(posText.trim(), Language.ENGLISH);
		Pair<String, Language> negPair = new Pair<String, Language>(negText.trim(), Language.ENGLISH);			

		Pair<String, Language> testPair = new Pair<String, Language>(testText.trim(), Language.ENGLISH);			

		System.out.println("Sim. between : \"" + "pos" + "\"\n      and" + "\"" + testText + "\"\n is :  " + 
				String.valueOf(clesa.score(posPair, testPair)));

		System.out.println("Sim. between : \"" + "neg" + "\"\n      and" + "\"" + testText + "\"\n is :  " + 
				String.valueOf(clesa.score(negPair, testPair)));


		double total = 0.0;
		double correct = 0.0;


		String semevalData = "Semeval/dataA.tsv";
		Set<ClassificationDocument> data = readSemevalData(semevalData);
		for(ClassificationDocument doc : data) {
			if(total != howMany) {
				System.out.println(data.size());
				testPair = new Pair<String, Language>(doc.getText(), Language.ENGLISH);
				double posScore = clesa.score(posPair, testPair);
				double negScore = clesa.score(negPair, testPair);			
				String actualLabel = doc.getLabel();					
				String proposedLabel =  null;			
				if(posScore >= negScore) 
					proposedLabel = "pos";
				else 
					proposedLabel = "neg";

				if(actualLabel.equalsIgnoreCase(proposedLabel)) 
					correct ++;
				System.out.println(total);
				total ++;
			} else {
				break;
			}
		}
		System.out.println("The accuracy of the system is : " + correct/total);
		System.out.println("Total :  " + total);
		System.out.println("Correct :  " + correct);
	}


	public static Set<ClassificationDocument> readSemevalData(String filePath) throws IOException {
		BufferedReader reader = BasicFileTools.getBufferedReaderFile(filePath);
		String line = null;
		Set<ClassificationDocument> documents = new HashSet<ClassificationDocument>();
		while((line=reader.readLine())!=null) {
			String[] splits = line.split("\t");
			String sentiTag = splits[3];
			String text = splits[4];

			if(sentiTag.equalsIgnoreCase("\"positive\"")) 
				sentiTag = "pos";		
			else if(sentiTag.equalsIgnoreCase("\"negative\"")) 
				sentiTag = "neg";
			else 
				sentiTag = null;

			if(sentiTag!=null) 	{
				ClassificationDocument doc = new ClassificationDocument(text, sentiTag);
				documents.add(doc);
			}		
		}
		return documents;
	}

}
