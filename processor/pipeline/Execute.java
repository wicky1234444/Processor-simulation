package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public int ALU(String operation,int a,int b) {
		if(operation=="add" || operation=="addi") return a+b;
		else if(operation=="sub" || operation=="subi") return a-b;
		else if(operation=="mul" || operation=="muli") return a*b;
		else if(operation=="div" || operation=="divi") { 
			containingProcessor.getRegisterFile().setValue(31, a%b);
			return a/b;}
		else if(operation=="and" || operation=="andi") return a&b;
		else if(operation=="or" || operation=="ori") return a|b;
		else if(operation=="xor" || operation=="xori") return (a|b)&(~a|~b);
		else if(operation=="slt" || operation=="slti") {
			if(a<b) return 1;
			else return 0;
		}
		else if(operation=="sll" || operation=="slli") return (a<<b);
		else if(operation=="srl" || operation=="srli") return (a>>b);
		else if(operation=="sra" || operation=="srai") return (a>>>b);
		else if(operation=="beq") {
			if(a==b) return 1;
			else return 0;
		}
		else if(operation=="bgt") {
			if(a>b) return 1;
			else return 0;
		}
		else if(operation=="blt") {
			if(a<b) return 1;
			else return 0;
		}
		else if(operation=="bne") {
			if(a!=b) return 1;
			else return 0;
		}
		else if(operation=="load") {
			return a+b;
		}
		else if(operation=="store") {
			return a+b;
		}
		return -1;
		
	}
	
	public void performEX()
	{
		
		if(OF_EX_Latch.EX_enable==true && OF_EX_Latch.getexwait()==false && EX_MA_Latch.getpc()!=OF_EX_Latch.getpc() && containingProcessor.getMaStoreWait()==false) {
			EX_MA_Latch.setpc(OF_EX_Latch.getpc());
			containingProcessor.setpipe(OF_EX_Latch.getpc());
			//System.out.println("execute");
			int pc=OF_EX_Latch.getpc();
			System.out.println("EX stage"+OF_EX_Latch.getpc()+" pc");
			int a=-1,b=-1;
			if(OF_EX_Latch.get_isimm()==true && OF_EX_Latch.get_operation()!="jmp" && OF_EX_Latch.get_operation()!="store" && OF_EX_Latch.get_operation()!="bne" && OF_EX_Latch.get_operation()!="beq" && OF_EX_Latch.get_operation()!="blt" && OF_EX_Latch.get_operation()!="bgt") {
				b=OF_EX_Latch.get_immx();
				a=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.get_op1());
				//System.out.println("immx");
			}
			else if(OF_EX_Latch.get_operation()!="jmp" && OF_EX_Latch.get_operation()!="store" && (OF_EX_Latch.get_operation()=="bne" || OF_EX_Latch.get_operation()=="beq" || OF_EX_Latch.get_operation()=="blt" || OF_EX_Latch.get_operation()=="bgt")) {
				a=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.get_op1());
				b=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.get_op2());
				System.out.println("bne,beq,blt,bgt etc");
				//System.out.println(a+" a, b "+b);
			}
			else if(OF_EX_Latch.get_operation()!="jmp" && OF_EX_Latch.get_operation()!="store") {
				a=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.get_op1());
				b=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.get_op2());
				//System.out.println("sub,add,mul,div etc");
				//System.out.println(a+" a, b "+b);
			}
			else if(OF_EX_Latch.get_operation()=="store") {
				b=OF_EX_Latch.get_immx();
				a=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.get_op1());
			}
			String op=OF_EX_Latch.get_operation();
			System.out.println(op+" operation");
			int res=-1;
			if(op!="jmp" || op!="end") {
			res=ALU(op,a,b);
			EX_MA_Latch.setisalu(true);
			EX_MA_Latch.set_aluresult(res);}
			else {
				EX_MA_Latch.setisalu(false);
			}
			int branch=0;
			int counter=0;
			if(op=="beq" && res==1) {
				branch=1;
			}
			else if(op=="bne" && res==1) {
				branch=1;
			}
			else if(op=="blt" && res==1) {
				branch=1;
			}
			else if(op=="bgt" && res==1) {
				branch=1;
			}
			else if(op=="jmp") {
				branch=1;
			}
			if(branch==1) {
				System.out.println("branch taken true");
				containingProcessor.setexbranch(true);
				containingProcessor.delpc(OF_EX_Latch.getpc());
				containingProcessor.incrementbranch();
				containingProcessor.flush_pipeline();
				Simulator.flush_event_queue();
				if(OF_EX_Latch.sign==false) {
					counter=-OF_EX_Latch.immx;
				}
				else {
					counter=OF_EX_Latch.immx;
				}
			EX_IF_Latch.setBranchTaken(true);
			//System.out.println(counter);
			//System.out.println(OF_EX_Latch.getpc());
			if(counter<0) {
				EX_IF_Latch.setBranchpc(counter+1+OF_EX_Latch.getpc());}
			else if(counter>0) {
				EX_IF_Latch.setBranchpc(counter+OF_EX_Latch.getpc());
			}
			//System.out.println(containingProcessor.getRegisterFile().getProgramCounter());
			}
			else {
				containingProcessor.setexbranch(false);
				EX_IF_Latch.setBranchTaken(false);	
			}
			//System.out.println(op);
			System.out.println(pc+" new pc");
			System.out.println(res+" result of alu");
			if(op=="load") {
				//System.out.println("isload");
				EX_MA_Latch.set_isload(true);
				//System.out.println(EX_MA_Latch.is_load);
			}
			else if(op=="store") {
				EX_MA_Latch.set_op2(OF_EX_Latch.get_op2());
				EX_MA_Latch.set_isstore(true);
			}
			else {
				EX_MA_Latch.set_isload(false);
				EX_MA_Latch.set_isstore(false);
				//System.out.println(EX_MA_Latch.is_load);
			}
			if(op=="end") {
				//System.out.println("end");
				EX_MA_Latch.isend(true);
			}
			if(OF_EX_Latch.getdest()!=-1) {
				EX_MA_Latch.setdest(OF_EX_Latch.getdest());
			}
			//OF_EX_Latch.setEX_enable(false);
			if(((op=="bgt" || op=="bne" || op=="blt" || op=="beq") && res==0)|| (op=="jmp")) {
				System.out.println("not enabled");
				//System.out.println(pc+" pc");
				containingProcessor.delpc(pc);
				OF_EX_Latch.setdel(true);
				OF_EX_Latch.setdelp(pc);
				EX_MA_Latch.setMA_enable(false);
				EX_IF_Latch.setIF_enable(true);
			}
			else if((op=="bgt" || op=="bne" || op=="blt" || op=="beq") && res==1){
				OF_EX_Latch.setdel(false);
				OF_EX_Latch.setdelp(-1);
				EX_MA_Latch.setMA_enable(false);
				EX_IF_Latch.setIF_enable(true);
				}
			else {
				OF_EX_Latch.setdel(false);
				OF_EX_Latch.setdelp(-1);
				EX_MA_Latch.setMAwait(false);
				EX_MA_Latch.setMA_enable(true);
			}
			
		}
		else if(OF_EX_Latch.getexwait()==true || containingProcessor.getMaStoreWait()==true) {
			System.out.println("execute is in  wait");
			EX_MA_Latch.setMAwait(true);
		}
		
	}
	
}
