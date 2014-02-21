package pex2;

import java.util.concurrent.ThreadLocalRandom;

public class Pump extends Thread{
	private static final int UPPER_TIME_LIMIT = 5000;
	private static final int LOWER_TIME_LIMIT = 2000;
	private int pumpTime;
	private Generator rightGenerator;
	private Generator leftGenerator;
	private int id;
	private int totalPumped;
	private int cycle;
	private static int counter;
	private Status status;
	private static int volume;
	private RequestPowerThread rpt;

	public Pump(int id, Generator leftGenerator, Generator rightGenerator) {
		super();
		this.rightGenerator = rightGenerator;
		this.leftGenerator = leftGenerator;
		this.id = id;
		pumpTime = LOWER_TIME_LIMIT;
		volume = 0;
		cycle = 0;
		counter = 0;
		setStatus(Status.WAITING);
	}

	public void requestPower(){
		rpt = new RequestPowerThread(this);
		rpt.run();
	}
	
	public void pumpWaiting(){
		setStatus(Status.WAITING);
	}
	
	public void pumpWater(){
		setStatus(Status.PUMPING);
		if(cycle == 0){
			System.out.println("Starting pump " + id + "...");
		}
		cycle ++;
		pumpTime = ThreadLocalRandom.current().nextInt(LOWER_TIME_LIMIT, UPPER_TIME_LIMIT);
		int pumpVolume = pumpTime/100;
		
		for(int i = 0; i< pumpTime; i = i+pumpVolume){
			if(volume < 60000){
				try {
					sleep(pumpVolume);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				totalPumped += pumpVolume;
				volume += pumpVolume;
			}
		}
		
	}
	
	public void pumpCleaning(){
		setStatus(Status.CLEANING);
		try {
			sleep(pumpTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Generator getRightGenerator() {
		return rightGenerator;
	}

	public Generator getLeftGenerator() {
		return leftGenerator;
	}

	public Status getStatus() {
		return status;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public int getID() {
		return id;
	}
	
	public int getTotalPumped() {
		return totalPumped;
	}
	
	public static int getCounter() {
		return counter;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void run(){
		while(volume < 60000){
			requestPower();
		}
		counter++;
		System.out.println("Pump " + id + " pumped " + totalPumped + " gallons in " + cycle + " cycles.");
	}

}
