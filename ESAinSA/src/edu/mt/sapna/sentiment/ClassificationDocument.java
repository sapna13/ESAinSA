package edu.mt.sapna.sentiment;


import java.io.File;


public class ClassificationDocument {
	private String text;
	private String label;		
		
	public ClassificationDocument(String text, String label) {
		this.text = text;
		this.label = label;
	}
	
	public ClassificationDocument(File textFile, String label) {
		String text = DataFile.extractText(textFile);
		this.text = text;
		this.label = label;
	}	
	
	public String getLabel() {
		return label;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean setText(String text) {
		this.text = text;
		return true;
	}
	
	public boolean setLabel(String label) {
		this.label = label;
		return true;
	}

}
