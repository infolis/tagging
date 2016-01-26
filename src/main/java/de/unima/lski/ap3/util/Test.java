package de.unima.lski.ap3.util;

import java.util.HashMap;

import de.unima.lski.ap3.analyse.Processing;
import de.unima.lski.ap3.analyse.TFIDFAnalyse;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Processing process= new Processing();
		process.init();
		int count=0;
		int count2=0;
		for(String c:process.posTaggedResultEN.keySet()){
			count++;
			if(count==10){
				break;
			}
			System.out.println(c);
			HashMap<String,Double> temp=process.posTaggedResultEN.get(c);
			for(String d:temp.keySet()){
				System.out.println(d+"\t"+temp.get(d));
			}
		}
		for(String c:process.posTaggedResultDE.keySet()){
			count2++;
			if(count2==10){
				break;
			}
			System.out.println(c);
			HashMap<String,Double> temp=process.posTaggedResultDE.get(c);
			for(String d:temp.keySet()){
				System.out.println(d+"\t"+temp.get(d));
			}
		
		}
		
//		long l=System.currentTimeMillis();
//		TFIDFAnalyse ana= new TFIDFAnalyse("C:/project/ssoarxml/");
//		
//		ana.calcTFIDF();
//		
//
//		//Testausgabe
//		int i=0;
//		for(String c:ana.gerResultMap.keySet()){
//			i++;
//				System.out.println("\n"+c+"\n");
//				double w=0;double x=0; double y=0;double z=0;
//				String ws="";String xs="";String ys="";String zs="";
//				for(String b:ana.gerResultMap.get(c).keySet()){
//					
//					if(ana.gerResultMap.get(c).get(b)>w&&ana.gerResultMap.get(c).get(b)>x&&ana.gerResultMap.get(c).get(b)>y&&ana.gerResultMap.get(c).get(b)>z){
//						z=y;zs=ys;
//						y=x;ys=xs;
//						x=w;xs=ws;
//						w=ana.gerResultMap.get(c).get(b);ws=b;
//					}else if(ana.gerResultMap.get(c).get(b)>x&&ana.gerResultMap.get(c).get(b)>y&&ana.gerResultMap.get(c).get(b)>z){
//						z=y;zs=ys;
//						y=x;ys=xs;
//						x=ana.gerResultMap.get(c).get(b);xs=b;
//					}else if(ana.gerResultMap.get(c).get(b)>y&&ana.gerResultMap.get(c).get(b)>z){
//						z=y;zs=ys;
//						y=ana.gerResultMap.get(c).get(b);ys=b;
//					}else if(ana.gerResultMap.get(c).get(b)>z){
//						z=ana.gerResultMap.get(c).get(b);zs=b;
//					}
//					
//					
//			}	
//				System.out.println(ws+" "+w);
//				System.out.println(xs+" "+x);
//				System.out.println(ys+" "+y);
//				System.out.println(zs+" "+z);
//				if(i>9){
//					break;
//				}
//		}
//		i=0;
//		System.out.println("\n ENGLISCH  \n");
//		for(String c:ana.engResultMap.keySet()){
//			i++;
//				System.out.println("\n"+c+"\n");
//				double w=0;double x=0; double y=0;double z=0;
//				String ws="";String xs="";String ys="";String zs="";
//				for(String b:ana.engResultMap.get(c).keySet()){
//					
//					if(ana.engResultMap.get(c).get(b)>w&&ana.engResultMap.get(c).get(b)>x&&ana.engResultMap.get(c).get(b)>y&&ana.engResultMap.get(c).get(b)>z){
//						z=y;zs=ys;
//						y=x;ys=xs;
//						x=w;xs=ws;
//						w=ana.engResultMap.get(c).get(b);ws=b;
//					}else if(ana.engResultMap.get(c).get(b)>x&&ana.engResultMap.get(c).get(b)>y&&ana.engResultMap.get(c).get(b)>z){
//						z=y;zs=ys;
//						y=x;ys=xs;
//						x=ana.engResultMap.get(c).get(b);xs=b;
//					}else if(ana.engResultMap.get(c).get(b)>y&&ana.engResultMap.get(c).get(b)>z){
//						z=y;zs=ys;
//						y=ana.engResultMap.get(c).get(b);ys=b;
//					}else if(ana.engResultMap.get(c).get(b)>z){
//						z=ana.engResultMap.get(c).get(b);zs=b;
//					}
//					
//					
//			}	
//				System.out.println(ws+" "+w);
//				System.out.println(xs+" "+x);
//				System.out.println(ys+" "+y);
//				System.out.println(zs+" "+z);
//				if(i>9){
//					break;
//				}
//		}
//		System.out.println(System.currentTimeMillis()-l);
	}

}
