package pex2;

public class Generator {
	private boolean generating;

	public boolean generating(){
		return generating;
	}
	
	public void startGenerator(){
		generating = true;
	}

	public void stopGenerator(){
		generating = false;
	}

}
