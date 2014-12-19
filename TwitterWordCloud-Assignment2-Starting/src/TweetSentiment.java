public class TweetSentiment {
	
	public String key;
	public String color;
	public int value;
	public String date;
	public int totalPositive;
	public int totalNegative;
	
	TweetSentiment()
	{		
	}
	
	TweetSentiment(String key, int value, String date)
	{
		this.key = key;
		if(value<0)
		{
			this.color = "Red";
		    this.totalNegative = this.totalNegative + Math.abs(value);
		}
		else
		{
			this.color="Green";
			this.totalPositive = this.totalPositive + value;
		}
		this.value = value;
		this.date = date;
	}
}
