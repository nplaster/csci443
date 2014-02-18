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
	private Status status;
	private static int volume;

	public Pump(int id, Generator leftGenerator, Generator rightGenerator) {
		super();
		this.rightGenerator = rightGenerator;
		this.leftGenerator = leftGenerator;
		this.id = id;
		pumpTime = LOWER_TIME_LIMIT;
		volume = 0;
		cycle = 0;
		setStatus(Status.WAITING);
	}

	public void requestPower(){
		setStatus(Status.READY);
		synchronized(leftGenerator){
			while(leftGenerator.generating() || rightGenerator.generating())
				try {
					leftGenerator.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(rightGenerator){
				try{
					setStatus(Status.WAITING);
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					leftGenerator.startGenerator();
					rightGenerator.startGenerator();
					pumpWater();
				}
				finally{
					leftGenerator.stopGenerator();
					rightGenerator.stopGenerator();
				}
				}
			
		}
		pumpCleaning();
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
			try {
				sleep(pumpVolume);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			volume += pumpVolume;
			totalPumped += pumpVolume;
		}
	}

	public Generator getRightGenerator() {
		return rightGenerator;
	}

	public Generator getLeftGenerator() {
		return leftGenerator;
	}

	public void pumpCleaning(){
		setStatus(Status.CLEANING);
		try {
			sleep(pumpTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void run(){
		while(volume < 60000){
			requestPower();
		}
		System.out.println("Pump " + id + " pumped " + totalPumped + " gallons in " + cycle + " cycles.");
	}

}
