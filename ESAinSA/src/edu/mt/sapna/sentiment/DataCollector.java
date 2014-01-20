package edu.mt.sapna.sentiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.mt.sapna.otdf.OTDFFile;
import edu.mt.sapna.otdf.OTDFXml;
import edu.mt.sapna.otdf.OTDFXmlReader;
import edu.mt.sapna.otdf.StringFeature;
import edu.mt.sapna.utils.BasicFileTools;
import edu.mt.sapna.utils.PTBTokenizer;
import edu.mt.sapna.utils.TextNormalizer;
import edu.mt.sapna.utils.Token;

public class DataCollector {

	public static String NEGATIVE = "neg";
	public static String POSITIVE = "pos";
	private PTBTokenizer tokenizer = new PTBTokenizer();

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataCollector test = new DataCollector();
		String dataXml = "SentiData/CollectiveXML/SentimentData.xml";	
		Set<ClassificationDocument> data = new HashSet<ClassificationDocument>();
		data.addAll(test.readUMICHData());
		data.addAll(test.readReviewData());
		data.addAll(test.readRTData());
		OTDFXml sentiData = new OTDFXml(dataXml);
		for(ClassificationDocument document : data) {
			OTDFFile otdfFile = new OTDFFile();
			otdfFile.addFeature(new StringFeature("SentiTag", document.getLabel()));
			otdfFile.addFeature(new StringFeature("Text", document.getText()));			
			sentiData.addOTDF(otdfFile);
		}
		sentiData.close();
	}

	private Set<ClassificationDocument> readDataXML(String xml) throws IOException{
		Set<ClassificationDocument> docs = new HashSet<ClassificationDocument>();
		OTDFXmlReader reader = new OTDFXmlReader(xml);
		Iterator<OTDFFile> iterator = reader.getIterator();
		while(iterator.hasNext()) {
			OTDFFile otdfFile = iterator.next();
			String sentiTag = otdfFile.getFeatureValue("SentiTag");
			String text = otdfFile.getFeatureValue("Text");
			ClassificationDocument doc = new ClassificationDocument(text, sentiTag);
			docs.add(doc);
		}
		return docs;
	}

	private List<Token> normalize(String text){	
		text = text.replace("\t", " ");
		text = text.toLowerCase();
		text = TextNormalizer.convertToUnicode(text);		
		text = TextNormalizer.removePunctuations(text);		
		text = text.replaceAll("\n", " ").trim();		
		text = TextNormalizer.deAccent(text);
		return tokenizer.tokenize(text);
	}

	public Set<ClassificationDocument> readUMICHData() throws IOException {
		BufferedReader reader = BasicFileTools.getBufferedReader(new File("SentiData//DataUMICHSI650.txt"));
		Set<ClassificationDocument> dataSet = new HashSet<ClassificationDocument>();
		String line = null;
		while ((line = reader.readLine())!=null) {
			String label = null;
			List<Token> tokens = normalize(line);
			Token sentToken = tokens.get(0);
			int sentTag = Integer.parseInt(sentToken.getValue());			
			if(sentTag == 1) 
				label = POSITIVE;
			else 
				label = NEGATIVE;
			tokens.remove(0);
			String text = TextNormalizer.joinTokens(tokens);
			ClassificationDocument doc = new ClassificationDocument(text, label);
			dataSet.add(doc);
		}
		return dataSet;
	}	

	public Set<ClassificationDocument> readReviewData() throws IOException {
		Set<ClassificationDocument> docs = new HashSet<ClassificationDocument>(); 
		String negData = "SentiData//review//neg";
		File[] files = new File(negData).listFiles();
		String label = NEGATIVE;
		for(File file : files) {
			String text = BasicFileTools.extractText(file);
			text = TextNormalizer.joinTokens(normalize(text));
			docs.add(new ClassificationDocument(text, label));
		}		
		String posData = "SentiData//review//pos";
		files = new File(posData).listFiles();
		label = POSITIVE;
		for(File file : files) {
			String text = BasicFileTools.extractText(file);
			text = TextNormalizer.joinTokens(normalize(text));
			docs.add(new ClassificationDocument(text, label));
		}		
		return docs;
	}

	public Set<ClassificationDocument> readRTData() throws IOException {
		Set<ClassificationDocument> docs = new HashSet<ClassificationDocument>();		
		String negData = "SentiData/rt-polaritydata/rt-polarity.neg";
		String posData = "SentiData/rt-polaritydata/rt-polarity.pos";		
		File negFile = new File(negData);
		File posFile = new File(posData);		
		BufferedReader reader = BasicFileTools.getBufferedReader(negFile);
		String text = null;
		while((text=reader.readLine()) != null) {
			String label = NEGATIVE;			
			text = TextNormalizer.joinTokens(normalize(text));
			docs.add(new ClassificationDocument(text, label));				 
		}
		reader = BasicFileTools.getBufferedReader(posFile);
		text = null;
		while((text=reader.readLine()) != null) {
			String label = POSITIVE;			
			text = TextNormalizer.joinTokens(normalize(text));
			docs.add(new ClassificationDocument(text, label));				 
		}
		return docs;
	}

}

