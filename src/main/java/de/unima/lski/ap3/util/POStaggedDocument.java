package de.unima.lski.ap3.util;

import java.util.HashSet;

public class POStaggedDocument {
	private String wglIdentifier=null;
	private String Identifier=null;
	private HashSet<String> taggedWordsEN=null;//key: word(s) value: label
	private HashSet<String> taggedWordsDE=null;//key: word(s) value: label
	
	public String getWglIdentifier() {
		return wglIdentifier;
	}
	public void setWglIdentifier(String wglIdentifier) {
		this.wglIdentifier = wglIdentifier;
	}
	public String getIdentifier() {
		return Identifier;
	}
	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}
	public HashSet<String> getTaggedWordsEN() {
		return taggedWordsEN;
	}
	public void setTaggedWordsEN(HashSet<String> taggedWordsEN) {
		this.taggedWordsEN = taggedWordsEN;
	}
	public HashSet<String> getTaggedWordsDE() {
		return taggedWordsDE;
	}
	public void setTaggedWordsDE(HashSet<String> taggedWordsDE) {
		this.taggedWordsDE = taggedWordsDE;
	}
}
