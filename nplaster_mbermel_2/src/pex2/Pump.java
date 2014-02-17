package pex2;

public class Pump extends Thread{
	private static final int UPPER_TIME_LIMIT = 5000;
	private static final int LOWER_TIME_LIMIT = 2000;
	private Generator rightGenerator;
	private Generator leftGenerator;
	private int id;
	private Status status;
	
	public Pump(int id, Generator rightGenerator, Generator leftGenerator) {
		super();
		this.rightGenerator = rightGenerator;
		this.leftGenerator = leftGenerator;
		this.id = id;
	}

	public void requestPower(){
		
	}
	
	public void pumpWater(){
		
	}
	
	public void pumpWait(){
		
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void run(){
		
	}
}
