package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean BranchTaken;
	int Branchpc;
	boolean IF_enable;
	
	public EX_IF_LatchType()
	{
		this.BranchTaken =false;
	}
	
	public void setIF_enable(boolean if_enable) {
		this.IF_enable=if_enable;
	}
	
	public boolean IF_enable() {
		return IF_enable;
	}
	
	public boolean isBranchTaken() {
		return BranchTaken;
	} 
	public int getBranchPC() {
		return Branchpc;
	}
	
	public void setBranchTaken(boolean branch) {
		this.BranchTaken= branch;
	}
	
	public void setBranchpc(int pc){
		this.Branchpc=pc;
	}

}
