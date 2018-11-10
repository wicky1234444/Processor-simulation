package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	int pc;
	boolean OFwait;
	public IF_OF_LatchType()
	{
		OF_enable = false;
		OFwait= false;
	}

	public int getpc() {
		return this.pc;
	}
	
	public boolean OF_wait() {
		return this.OFwait;
	}
	
	public void setwait(boolean w) {
		this.OFwait=w;
	}
	
	public void setpc(int p) {
		this.pc=p;
	}
	
	public boolean isOF_enable() {
		return this.OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		this.OF_enable = oF_enable;
	}

	public int getInstruction() {
		return this.instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

}
