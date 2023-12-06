import java.util.function.BinaryOperator;

public class SequenceCounter {
	private String count;
	public int con;

	public SequenceCounter() {
		this.count = "0000";
		con = Integer.parseInt(count, 2);
		
				
	}

	public String getCount() {
		return count;
	}


	public void increment() {
		con++;
	}
}
