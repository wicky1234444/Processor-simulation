package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int immx;
	int op1;
	int op2;
	boolean sign;
	String operation;
	boolean is_imm;
	int dest;
	boolean destset;
	int pc;
	boolean exwait;
	boolean del;
	int delp;
	
	public OF_EX_LatchType()
	{	
		del=false;
		delp=-1;
		exwait=false;
		EX_enable = false;
		immx=0;
		sign=true;
		op1=0;
		op2=0;
		operation="";
		is_imm=false;
		dest=-1;
		destset=false;
	}

	public void setdest(int d) {
		this.dest=d;
		this.destset=true;
	}
	
	public boolean getexwait() {
		return this.exwait;
	}
	
	public void setexwait(boolean w) {
		this.exwait=w;
	}
	
	public int getdest() {
		return dest;
	}
	
	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	
	public void set_immx(int imm) {
		this.immx=imm;
	} 
	
	public void set_isimmx(boolean im) {
		is_imm=im;
	}
	
	public boolean get_isimm() {
		return is_imm;
	}
	
	public void set_sign(boolean sign1) {
		this.sign=sign1;
	}
	
	public boolean get_sign() {
		return sign;
	}
	
	public void set_op1(int op) {
		this.op1=op;
	}
	
	public void set_op2(int op) {
		this.op2=op;
	}
	
	public String get_operation() {
		return operation;
	}
	public int getpc() {
		return this.pc;
	}
	
	public void setpc(int p) {
		this.pc=p;
	}
	public void set_operation(int opcode) {
		//System.out.println(opcode+" inlatch");
		if(opcode==0)
			this.operation="add";
		else if(opcode==1)
			this.operation="addi";
		else if(opcode==2)
			this.operation="sub";
		else if(opcode==3)
			this.operation="subi";
		else if(opcode==4)
			this.operation="mul";
		else if(opcode==5)
			this.operation="muli";
		else if(opcode==6)
			this.operation="div";
		else if(opcode==7)
			this.operation="divi";
		else if(opcode==8)
			this.operation="and";
		else if(opcode==9)
			this.operation="andi";
		else if(opcode==10)
			this.operation="or";
		else if(opcode==11)
			this.operation="ori";
		else if(opcode==12)
			this.operation="xor";
		else if(opcode==13)
			this.operation="xori";
		else if(opcode==14)
			this.operation="slt";
		else if(opcode==15)
			this.operation="slti";
		else if(opcode==16)
			this.operation="sll";
		else if(opcode==17)
			this.operation="slli";
		else if(opcode==18)
			this.operation="srl";
		else if(opcode==19)
			this.operation="srli";
		else if(opcode==20)
			this.operation="sra";
		else if(opcode==21)
			this.operation="srai";
		else if(opcode==22)
			this.operation="load";
		else if(opcode==23)
			this.operation="store";
		else if(opcode==24)
			this.operation="jmp";
		else if(opcode==25)
			this.operation="beq";
		else if(opcode==26)
			this.operation="bne";
		else if(opcode==27)
			this.operation="blt";
		else if(opcode==28)
			this.operation="bgt";
		else if(opcode==29)
			this.operation="end";
	}
	
	public int get_immx() {
		return immx;
	}
	public int get_op1() {
		return op1;
	}
	public int get_op2() {
		return op2;
	}
	
	public void setdel(boolean p) {
		this.del=p;
	}
	
	public void setdelp(int p) {
		this.delp=p;
	}
	
	public boolean getdel() {
		return this.del;
	}
	
	public int getdelp() {
		return this.delp;
	}
	
	
}
