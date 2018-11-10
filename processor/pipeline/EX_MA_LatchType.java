package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int alu_result;
	boolean is_store;
	boolean is_load;
	int dest;
	boolean end;
	int dest1;
	boolean isalu;
	int pc;
	boolean MAwait;
	public EX_MA_LatchType()
	{	
		MAwait=false;
		isalu=false;
		end=false;
		dest=-1;
		is_load=false;
		is_store=false;
		alu_result=-1;
		MA_enable = false;
		dest1=-1;
	}
	
	public void isend(boolean end1) {
		this.end=end1;
	}
	
	public boolean getend() {
		return end;
	}
	
	public void setdest(int d) {
		this.dest1=d;
	}
	
	public int getdest() {
		return dest1;
	}
	
	public void set_aluresult(int alu) {
		this.alu_result=alu;
	}
	
	public void set_isstore(boolean store) {
		this.is_store=store;
	}
	
	public void set_isload(boolean load) {
		this.is_load=load;
	}
	
	public boolean Is_store() {
		return is_store;
	}
	
	public boolean Is_load() {
		return is_load;
	}
	
	public void set_op2(int op) {
		this.dest=op;
	}
	
	public int get_dest() {
		return dest;
	}
	
	public int get_alu() {
		return alu_result;
	}
	
	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public void setisalu(boolean a) {
		this.isalu=a;
	}
	
	public boolean get_isalu() {
		return isalu;
	}
	
	public int getpc() {
		return this.pc;
	}
	
	public void setpc(int p) {
		this.pc=p;
	}
	
	public void setMAwait(boolean w) {
		this.MAwait=w;
	}
	
	public boolean getMAwait() {
		return this.MAwait;
	}
	
}
