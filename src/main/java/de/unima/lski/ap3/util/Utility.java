package de.unima.lski.ap3.util;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import org.knallgrau.utils.textcat.TextCategorizer;


public class Utility {
	
	/**
	 * 
	 * @param text The text you want to get the language from.
	 * @return 0=unknown,1=german,2=english,3=french,4=spanish if none of them -1 will be returned.
	 */
	public static int getLanguage(String text){
		TextCategorizer guesser =new TextCategorizer();
		String temp=guesser.categorize(text).toLowerCase();
		if(temp.equals("unknown")){
			return 0;
		}
		if(temp.equals("german")){
			return 1;
		}
		if(temp.equals("english")){
			return 2;
		}
		if(temp.equals("french")){
			return 3;
		}
		if(temp.equals("spanish")){
			return 4;
		}
		return -1;
	}
	public static File[] getFileNames(String dir){
		File f=new File(dir);
		if(f.isDirectory()){
			return f.listFiles();
		}else{
			throw new RuntimeException("The path was no directory");
			
		}
	}
	public static HashMap<String,String> normalizeTaggedWords(HashSet<String> set){
		HashMap<String,String> map = new HashMap<String,String>();
		for(String original:set){
			String temp=original.toLowerCase().replaceAll("\\p{Punct}", "").trim();
			map.put(temp, original);
		}
		return map;
	}
	
	
}
