package pex2;

import java.util.concurrent.ThreadLocalRandom;

public class Pump extends Thread{
	private static final int UPPER_TIME_LIMIT = 5000;
	private static final int LOWER_TIME_LIMIT = 2000;
	private int pumpTime;
	private Generator rightGenerator;
	private Generator leftGenerator;
	private int id;
	private Status status;
	private static int volume;

	public Pump(int id, Generator leftGenerator, Generator rightGenerator) {
		super();
		this.rightGenerator = rightGenerator;
		this.leftGenerator = leftGenerator;
		this.id = id;
		pumpTime = LOWER_TIME_LIMIT;
		volume = 0;
	}

	public void requestPower(){
		setStatus(Status.READY);
		synchronized(leftGenerator){
			while(leftGenerator.generating() || rightGenerator.generating())
				try {
					setStatus(Status.WAITING);
					leftGenerator.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(rightGenerator){
				try{
					leftGenerator.startGenerator();
					rightGenerator.startGenerator();
					pumpWater();
				}
				finally{
					leftGenerator.stopGenerator();
					rightGenerator.stopGenerator();
					pumpCleaning();
				}
				}
			
		}
		
	}
	
	
	public void pumpWater(){
		setStatus(Status.PUMPING);
		try {
			pumpTime = ThreadLocalRandom.current().nextInt(LOWER_TIME_LIMIT, UPPER_TIME_LIMIT);
			sleep(pumpTime);
			volume += pumpTime;
		} catch (InterruptedException e) {
			e.printStackTrace();
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

	public Status getStatus() {
		return status;
	}
	
	public int getVolume() {
		return volume;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void run(){
		while(volume < 60000){
			requestPower();
		}
	}
}
