package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean gotend;
	int pc;
	
	public IF_EnableLatchType()
	{	
		pc=0;
		gotend=false;
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}
	
	public boolean isend() {
		return this.gotend;
	}
	
	public void setend(boolean w) {
		this.gotend=w;
	}
	
	public void setpc(int p) {
		this.pc=p;
	}
	
	public int getpc() {
		return this.pc;
	}
	

}
