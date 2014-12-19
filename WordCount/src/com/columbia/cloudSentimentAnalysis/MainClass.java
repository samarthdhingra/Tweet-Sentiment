package com.columbia.cloudSentimentAnalysis;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainClass {

	public static void main(String[] args) throws IOException {
		String line="boo|and|foo";
		String[] arr=line.split("\\|");
		String inputFile="E:\\VyasvishColumbia\\sem1\\Cloud And BigData\\assignment3\\twolines_win.txt";
		String s3FilePath="E:\\VyasvishColumbia\\sem1\\Cloud And BigData\\assignment3\\dictionary_mini_win.csv";
		  
		Gson gson = new Gson();
		HashMap<String, Integer> dict= getDictionary(s3FilePath);
		String dictionarySerialised = gson.toJson(dict);	
		
		
		String dictionary_str = dictionarySerialised;
		Gson gson2 = new Gson();
		HashMap<String, Integer>   dictionary = gson2.fromJson(dictionary_str, new TypeToken<HashMap<String, Integer>>() {}.getType());
	 
		
		FileInputStream fs =new FileInputStream(inputFile);
		BufferedReader br= new BufferedReader(new InputStreamReader(fs));
		String line2;
		while( (line2=br.readLine()) != null){ 
		
		StringTokenizer tokenizer = new StringTokenizer(line2);
		while (tokenizer.hasMoreTokens()) {
			String tok=tokenizer.nextToken();
			if(dictionary.containsKey(tok)){
				 System.out.println("Found from Dict :  " + tok + ", value " + dictionary.get(tok) ) ;
			}else{
				System.out.println("NOT In Dict :  " + tok  ) ;
			}
		}
		}	
 
		 
		 
		// TODO Auto-generated method stub
//		   Configuration conf = new Configuration();
//		   
//		   Job job = new Job(conf, "wordcount");
//		   job.setJarByClass(MainClass.class);
//		 
//		   job.setOutputKeyClass(Text.class);
//		   job.setOutputValueClass(IntWritable.class);
//		 
//		   job.setMapperClass(Map.class);
//		   job.setReducerClass(Reduce.class);
//		 
// 
//		   
//		   //job.setInputFormatClass(org.apache.hadoop.mapred.TextInputFormat.class);
//		   //job.setOutputFormatClass(org.apache.hadoop.mapred.TextOutputFormat.class);
//		 
//		   FileInputFormat.addInputPath(job, new Path(args[0]));
//		   FileOutputFormat.setOutputPath(job, new Path(args[1]));
//		 
//		   job.waitForCompletion(true);
	}
	
	static HashMap<String, Integer> getDictionary(String dictionaryPath){
		HashMap<String, Integer> dictionary = new HashMap<String, Integer>( );
		try {

			FileInputStream fstream = new FileInputStream(dictionaryPath);
			BufferedReader br=new BufferedReader(new InputStreamReader(fstream));
			
			String line=br.readLine();
			while (line != null) {
				String[] arr=line.split("\\|", 0);
				if(arr!= null && arr.length==2){
					dictionary.put(arr[0] ,Integer.parseInt(arr[1]));
					System.out.println("Dictionary : "+ arr[0]  + "|" + Integer.parseInt(arr[1]));
				}
				 line=br.readLine();
			}
			return dictionary;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return dictionary;
		}
	}

}
