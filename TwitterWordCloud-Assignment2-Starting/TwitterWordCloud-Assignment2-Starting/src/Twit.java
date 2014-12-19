import java.util.StringTokenizer;


public class Twit {
		
		public long id;
		public String screenName;
		public String content;
		public double latitude;
		public double longitude;		
		public String keyword;
		public String sentiment;
		public long score;
		public String time;
		
		public static String TOKENIZER = "^^*^^";
		public Twit() {
		}
		public Twit(String concatenatedString) {
			fromString(concatenatedString);
		}
		public Twit(String screenName, String content, double latitude, double longitude, String keyword, long id, String time) {
			this.id = id;
			this.screenName = screenName;
			this.content = content;
			this.latitude = latitude;
			this.longitude = longitude;
			this.keyword = keyword;
			this.sentiment = null;
			this.time = time;
			//this.score = null;
		}
		
		public String toString() {
			return id + TOKENIZER + screenName + TOKENIZER + latitude + TOKENIZER 
					+ longitude + TOKENIZER + keyword + TOKENIZER + sentiment + TOKENIZER + time + TOKENIZER + content ;
		}
		
		public void fromString(String concantenatedString) {
			System.out.println("Full string = " + concantenatedString);
			StringTokenizer tokenizer = new StringTokenizer(concantenatedString, TOKENIZER);
            this.id = (long) (Double.parseDouble(tokenizer.nextToken()));
			this.screenName = tokenizer.nextToken();
			this.latitude = (long) (Double.parseDouble(tokenizer.nextToken()));
			this.longitude = (long) (Double.parseDouble(tokenizer.nextToken()));
			this.keyword = tokenizer.nextToken();
			this.sentiment = tokenizer.nextToken();
			//this.score = tokenizer.nextToken();
			this.time = tokenizer.nextToken();
			this.content = tokenizer.nextToken();
		}
}
