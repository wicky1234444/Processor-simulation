package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int ldresult;
	boolean end;
	int dest;
	boolean isload;
	int aluresult;
	boolean isalu;
	boolean isstore;
	int pc;
	boolean RWwait;
	public MA_RW_LatchType()
	{	
		RWwait=false;
		isstore=false;
		aluresult=0;
		isload=false;
		dest=-1;
		end=false;
		ldresult=0;
		RW_enable = false;
		isalu=false;
	}
	
	/*public boolean isinvalid() {
		if(isstore=false)
	}*/
	
	public boolean getRWwait() {
		return this.RWwait;
	}
	
	public void setRWwait(boolean w) {
		this.RWwait=w;
	}
	
	public int getpc() {
		return this.pc;
	}
	
	public void setpc(int p) {
		this.pc=p;
	}
	
	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setdest(int d) {
		this.dest=d;
	}
	
	public void setisstore(boolean store) {
		this.isstore=store;
	}
	
	public boolean getisstore() {
		return isstore;
	}
	
	public void setisload(boolean d) {
		this.isload=d;
	}
	
	public boolean getisload() {
		return isload;
	}
	
	public int getdest() {
		return dest;
	}
	
	public void setalu(int a) {
		this.aluresult=a;
	}
	
	public void setisalu(boolean a) {
		this.isalu=a;
	}
	
	public boolean getisalu() {
		return isalu;
	}
	
	public int getalu() {
		return aluresult;
	}
	
	public void set_ld(int ld) {
		this.ldresult=ld;
	}
	
	public void isend(boolean end1) {
		this.end=end1;
	}
	
	public boolean getend() {
		return end;
	}
	
	public int get_ld() {
		return ldresult;
	}
	
	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

}
