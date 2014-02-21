package pex2;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class Pump extends Thread {
	// constants for the max and min amount of time a pump can pump
	private static final int UPPER_TIME_LIMIT = 5000;
	private static final int LOWER_TIME_LIMIT = 2000;
	// the actual amount of time a pump pumped
	private int pumpTime;
	// objects for the right and left generator for the specific pump
	private Generator rightGenerator;
	private Generator leftGenerator;
	// the pumps individual information
	private int id;
	private int totalPumped;
	private int cycle;
	private Status status;
	// the overall pump and counter of pumps
	private static int counter;
	private static int volume;
	// the threads to launch the left and right generators
	private RequestPowerThread rptleft;
	private RequestPowerThread rptright;

	/**
	 * Pump constructor setting up pump threads with initial values and statuses
	 * 
	 * @param id
	 *            - the id of the pump number
	 * @param leftGenerator
	 *            - the generator object corresponding to the left
	 * @param rightGenerator
	 *            - the generator object corresponding to the right
	 */
	public Pump(int id, Generator leftGenerator, Generator rightGenerator) {
		super();
		// get from main class
		this.rightGenerator = rightGenerator;
		this.leftGenerator = leftGenerator;
		this.id = id;
		// set initial values and statuses
		pumpTime = LOWER_TIME_LIMIT;
		volume = 0;
		cycle = 0;
		counter = 0;
		status = Status.READY;
	}

	/**
	 * starts two threads to send request for power and starts pumping if gets
	 * both power supplies
	 */
	public void requestPower() {
		// Ready state waits for half a second
		setStatus(Status.READY);
		pause(500);
		// creates two threads for left and right generators
		rptleft = new RequestPowerThread(this, leftGenerator);
		rptright = new RequestPowerThread(this, rightGenerator);
		// starts threads
		rptleft.run();
		rptright.run();
		// after threads finished if pump has both generators pump water
		if (rptleft.isStartPumping() && rptright.isStartPumping()) {
			pumpWater();
			// stop generators after pumping to release lock
			leftGenerator.stopGenerator();
			rightGenerator.stopGenerator();
			// clean pump after pumping water
			pumpCleaning();
			// if the pump has the left or right generator but not both
		} else if (rptleft.isStartPumping() || rptright.isStartPumping()) {
			// release the generators lock
			if (rptleft.isStartPumping())
				leftGenerator.stopGenerator();
			else
				rightGenerator.stopGenerator();
		}
	}

	/**
	 * pumps water into the tank at a rate defined with a floor and ceiling
	 */
	public void pumpWater() {
		// change status to pumping
		setStatus(Status.PUMPING);
		// if this is the first time pumping print pump started
		if (cycle == 0) {
			System.out.println("Starting pump " + id + "...");
		}
		cycle++;
		// pick a pump time randomly between the time limits
		pumpTime = ThreadLocalRandom.current().nextInt(LOWER_TIME_LIMIT,
				UPPER_TIME_LIMIT);
		// create pump volume that is divisible by 100
		int pumpVolume = pumpTime / 100;
		for (int i = 0; i < pumpTime; i = i + pumpVolume) {
			// adds water to the tank at a incremental value pausing for same
			// amount of time
			if (volume < 60000) {
				pause(pumpVolume);
				// increment individually pumped and total volume
				totalPumped += pumpVolume;
				volume += pumpVolume;
			}
		}
	}

	/**
	 * the state after a pump gets down pumping water
	 */
	public void pumpCleaning() {
		// change status and wait here for the same amount of time that was
		// pumped
		setStatus(Status.CLEANING);
		pause(pumpTime);
	}

	/**
	 * function to pause threads for a certain amount of time
	 * 
	 * @param time
	 *            - time thread needs to sleep
	 */
	public void pause(int time) {
		try {
			sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		// request power as long as the tank is not full
		while (volume < 60000) {
			requestPower();
		}

		// after threads finish print the statistics of the pumps
		System.out.println("Pump " + id + " pumped " + totalPumped
				+ " gallons in " + cycle + " cycles.");
		counter++;
	}

	/**
	 * getter for right generator
	 */
	public Generator getRightGenerator() {
		return rightGenerator;
	}

	/**
	 * getter for left generator
	 */
	public Generator getLeftGenerator() {
		return leftGenerator;
	}

	/**
	 * getter for threads status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * getter for volume of tank
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * getter for the count of finished threads
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * setter to set the status of the pump
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
}
