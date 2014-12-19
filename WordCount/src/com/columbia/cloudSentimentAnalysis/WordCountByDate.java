package com.columbia.cloudSentimentAnalysis;
 

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.net.URI;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WordCountByDate {

	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		Path s3FilePath;
		HashMap<String, Integer> dictionary;
		Configuration conf;
		String dictionary_str;
		@Override
		public void configure(JobConf job) {
			try {
			s3FilePath = DistributedCache.getLocalCacheFiles(job)[0];
 			dictionary = getDictionary(s3FilePath.toString());
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
 
		
 
		public void map(LongWritable key, Text value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			String date = line.substring(line.lastIndexOf(" ")+1);
			
			StringTokenizer tokenizer = new StringTokenizer(line);
			
			while (tokenizer.hasMoreTokens()) {
				String tok=tokenizer.nextToken();
				
				if(tok.equals(date))
					continue;
				//word.set(tok);
				
				if(dictionary.containsKey(tok)   ){
					IntWritable sentimentValue= new IntWritable(dictionary.get(tok));
					word.set(tokenizer.nextToken() + ":#:" + date);
					output.collect(word, sentimentValue);
				}
			}
		}//---end of map
	
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("wordcount");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		String s3FilePath = args[2];
		DistributedCache.addCacheFile(new URI(s3FilePath), conf);
	    // s3File should be a URI with s3: or s3n: protocol. It will be accessible as a local filed called 'theFile'
		//conf.adaddCacheFile(new URI(args[2]));
        //set dictionary
/*		Gson gson = new Gson();
		HashMap<String, Integer> dict= getDictionary(s3FilePath);
		String dictionarySerialised = gson.toJson(dict);
 
		conf.set("dictionarySerialised", dictionarySerialised);*/
		
		JobClient.runJob(conf);
 
	}
	
	static HashMap<String, Integer> getDictionary(String dictionaryPath){
		HashMap<String, Integer> dictionary = new HashMap<String, Integer>( );
		try {

			FileInputStream fstream = new FileInputStream(dictionaryPath);
			BufferedReader br=new BufferedReader(new InputStreamReader(fstream));
			
			String line=br.readLine();
			while (line != null) {
				String[] arr=line.split("\\|", 0);
				if(arr!= null && arr.length==2)
					dictionary.put(arr[0] ,Integer.parseInt(arr[1]));
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