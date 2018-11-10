package processor;

import processor.memorysystem.Cache;
import processor.memorysystem.MainMemory;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;

public class Processor {
	
	RegisterFile registerFile;
	MainMemory mainMemory;
	Cache l1i;
	Cache l1d;
	
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;
	int[] instdest=new int[5];
	int[] pcs=new int[5];
	boolean exbranch;
	int[] pipe=new int[5];
	int num_instr;
	int stall;
	int branchh;
	boolean ma_store_wait;
	
	
	public Processor()
	{	
		ma_store_wait=false;
		stall=0;
		branchh=0;
		num_instr=0;
		exbranch=false;
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		l1i = new Cache(1,1,this,0);
		l1d = new Cache(256,12,this,1);
		
		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);
		for(int i=0;i<5;i++) {
			pcs[i]=-1;
			instdest[i]=-2;
			pipe[i]=-1;
		}
	}
	
	public int ControlUnit(int opcode) {
		int imm=0;
		if(opcode==1 || opcode==3 || opcode==5 || opcode==7 || opcode==9 || opcode==11 || opcode==13 || opcode==15 || opcode==17 || opcode==19 || opcode==21 || opcode==22) {
			imm=2;
		}
		else if(opcode==25 || opcode==26 || opcode==27 || opcode==28) {
			imm=5;
		}
		else if(opcode==24){
			imm=1;
		}
		else if(opcode==23) {
			imm=4;
		}
		else {
			imm=3;
		}
		return imm;
	}
	
	public boolean isdatahazard(int op1,int op2,int p) {
		boolean datahazard=false;
		System.out.println("datahazard pcs");
		for(int i=0;i<5;i++) {
			if((this.instdest[i]==op1 || this.instdest[i]==op2)&& (pcs[i]!=p)) {
				System.out.println(pcs[i]+" pcs p "+p+" op1 "+op1+" op2 "+op2+" dest "+instdest[i]);
				datahazard=true;
			}
		}
		return datahazard;
	}
	
	public void flush_pipeline() {
		this.IF_EnableLatch= new IF_EnableLatchType();
		this.IF_OF_Latch = new IF_OF_LatchType();
		this.OF_EX_Latch = new OF_EX_LatchType();
		this.IFUnit =  new InstructionFetch(this, this.IF_EnableLatch, this.IF_OF_Latch, this.EX_IF_Latch);
		this.OFUnit =  new OperandFetch(this, this.IF_OF_Latch, this.OF_EX_Latch);
		this.EXUnit =  new Execute(this, this.OF_EX_Latch, this.EX_MA_Latch, this.EX_IF_Latch);
		for(int i=0;i<5;i++) {
			pcs[i]=-1;
			instdest[i]=-2;
		}
		System.out.println("------------------------pipeline flush--------------------------------");
	}
	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());
		
		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}
	
	public void delpc(int p) {
		for(int i=0;i<5;i++) {
			if(pcs[i]==p) {
				System.out.println("deleted "+pcs[i]+" pcs p "+p);
				pcs[i]=-1;
				instdest[i]=-2;
			}
			if(pcs[i]==pipe[i]) {
				pipe[i]=-1;
			}
		}
		/*for(int i=0;i<5;i++) {
			System.out.println(pcs[i]+" pipe "+instdest[i]);
		}*/
	}
	
	public void setpc(int dest,int p) {
		int f=0;
		for(int i=0;i<5;i++) {
			if(pcs[i]==p) {f=1;}
		}
		if(f==0) {
		for(int i=0;i<5;i++) {
			if(pcs[i]==-1) {
				pcs[i]=p;
				instdest[i]=dest;
				break;
			}
		}
		}
	}
	
	public void setpipe(int p) {
		for(int i=0;i<5;i++) {
			if(pipe[i]==-1) {
				pipe[i]=p;
				break;
			}
		}
	}
	
	public void printpipe() {
		for(int i=0;i<5;i++) {
			System.out.println(pipe[i]+" pipe");
		}
	}
	
	public void setexbranch(boolean g) {
		this.exbranch=g;
	}

	public boolean getexbranch() {
		return this.exbranch;
	}
	
	public void incrementinstr() {
		this.num_instr++;
	}
	
	public int get_numinstr() {
		return this.num_instr;
	}
	public void incrementstall() {
		this.stall++;
	}
	
	public int get_stall() {
		return this.stall;
	}
	public void incrementbranch() {
		this.branchh++;
	}
	
	public int get_branch() {
		return this.branchh;
	}
	
	public boolean getMaStoreWait() {
		return this.ma_store_wait;
	}
	
	public void setMaStoreWait(boolean w) {
		this.ma_store_wait =w;
	}
	
	public Cache getL1i() {
		return this.l1i;
	}
	
	public Cache getL1d() {
		return this.l1d;
	}
	
}
