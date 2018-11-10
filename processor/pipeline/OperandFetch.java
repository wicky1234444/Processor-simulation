package processor.pipeline;

import generic.Element;
import generic.Event;
import processor.Processor;

public class OperandFetch implements Element{
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		//System.out.println(IF_OF_Latch.isOF_enable());
		if(IF_OF_Latch.isOF_enable() && containingProcessor.getMaStoreWait()==false)
		{	OF_EX_Latch.setpc(IF_OF_Latch.getpc());
			//System.out.println("running OF");
			System.out.println("OF stage "+IF_OF_Latch.getpc()+" pc");
			containingProcessor.setpipe(IF_OF_Latch.getpc());
			//int currentpc=containingProcessor.getRegisterFile().getProgramCounter();
			int currentinstruction = IF_OF_Latch.getInstruction();
			String s=String.format("%32s",Integer.toBinaryString(currentinstruction)).replace(' ', '0');
			//System.out.println(s.substring(0,5));
			//System.out.println(s);
			OF_EX_Latch.set_operation(Integer.parseInt(s.substring(0,5),2));
			if(containingProcessor.ControlUnit(Integer.parseInt(s.substring(0, 5),2))==2) {  //addi,subi..
				//System.out.println("r2i type");
				int imm=Integer.parseInt(s.substring(15,32),2);
				if(Integer.parseInt(s.substring(15,16),2)==1) {
					imm=(int)Math.pow(2, 17)-imm+1;
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(false);
				}
				else {
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(true);
				}
				//System.out.println(imm+" immx");
				int op=Integer.parseInt(s.substring(5,10),2);
				//System.out.println(" rs1 "+op);
				//System.out.println(" rd "+Integer.parseInt(s.substring(10,15),2));
				OF_EX_Latch.setdest(Integer.parseInt(s.substring(10,15),2));
				OF_EX_Latch.set_op1(op);
				if(containingProcessor.isdatahazard(op, -1,IF_OF_Latch.getpc())==true) {
					containingProcessor.incrementstall();
					IF_OF_Latch.setwait(true);
					OF_EX_Latch.setexwait(true);
					OF_EX_Latch.setEX_enable(false);
				}
				else {
					IF_OF_Latch.setwait(false);
					OF_EX_Latch.setexwait(false);
					OF_EX_Latch.setEX_enable(true);
					//System.out.println("here1");
					//System.out.println(OF_EX_Latch.getdel());
					if(OF_EX_Latch.getdel()==false || OF_EX_Latch.getdelp()!=IF_OF_Latch.getpc()) {
						//System.out.println("here ");
						IF_OF_Latch.setOF_enable(false);
						containingProcessor.setpc(Integer.parseInt(s.substring(10,15),2), IF_OF_Latch.getpc());}
				}
				//IF_OF_Latch.setOF_enable(false);
				//OF_EX_Latch.setexwait(false);
				
				
			}
			else if(containingProcessor.ControlUnit(Integer.parseInt(s.substring(0,5),2))==5) {    //beq,blt,bgt
				//System.out.println("beq,bgt,bne etc.");
				int imm=Integer.parseInt(s.substring(15,32),2);
				int dest=-1;
				if(Integer.parseInt(s.substring(15,16),2)==1) {
					imm=(int)Math.pow(2, 17)-imm+1;
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(false);
					dest=-imm;
				}
				else {
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(true);
					dest=imm;
				}
				int op1=Integer.parseInt(s.substring(5,10),2);
				int op2=Integer.parseInt(s.substring(10,15),2);
				OF_EX_Latch.set_op1(op1);
				OF_EX_Latch.set_op2(op2);
				if(containingProcessor.isdatahazard(op1, op2,IF_OF_Latch.getpc())==true) {
					containingProcessor.incrementstall();
					IF_OF_Latch.setwait(true);
					OF_EX_Latch.setexwait(true);
					OF_EX_Latch.setEX_enable(false);
				}
				else {
					IF_OF_Latch.setwait(false);
					OF_EX_Latch.setexwait(false);
					OF_EX_Latch.setEX_enable(true);
					if(OF_EX_Latch.getdel()==false && OF_EX_Latch.getdelp()!=IF_OF_Latch.getpc()) {
						IF_OF_Latch.setOF_enable(false);
						containingProcessor.setpc(dest, IF_OF_Latch.getpc());
					}
				}
				//IF_OF_Latch.setOF_enable(false);
				//OF_EX_Latch.setexwait(false);
				//OF_EX_Latch.setEX_enable(true);
			}
			else if(containingProcessor.ControlUnit(Integer.parseInt(s.substring(0,5),2))==3) {    //add,sub
				//System.out.println("r3 type");
				int op1=Integer.parseInt(s.substring(5,10),2);
				int op2=Integer.parseInt(s.substring(10,15),2);
				//System.out.println(op1);
				//System.out.println(op2);
				OF_EX_Latch.set_isimmx(false);
				OF_EX_Latch.set_sign(false);
				OF_EX_Latch.set_op1(op1);
				OF_EX_Latch.set_op2(op2);
				OF_EX_Latch.setdest(Integer.parseInt(s.substring(15,20),2));
				if(containingProcessor.isdatahazard(op1, op2,IF_OF_Latch.getpc())==true) {
					containingProcessor.incrementstall();
					IF_OF_Latch.setwait(true);
					OF_EX_Latch.setexwait(true);
					OF_EX_Latch.setEX_enable(false);
				}
				else {
					IF_OF_Latch.setwait(false);
					OF_EX_Latch.setexwait(false);
					OF_EX_Latch.setEX_enable(true);
					if(OF_EX_Latch.getdel()==false && OF_EX_Latch.getdelp()!=IF_OF_Latch.getpc()) {
						IF_OF_Latch.setOF_enable(false);
					containingProcessor.setpc(Integer.parseInt(s.substring(10,15),2), IF_OF_Latch.getpc());}
				}
				
				//IF_OF_Latch.setOF_enable(false);
				//OF_EX_Latch.setexwait(false);
				//OF_EX_Latch.setEX_enable(true);
				
			}
			else if(containingProcessor.ControlUnit(Integer.parseInt(s.substring(0,5),2))==1) {
				//System.out.println("is jmp");
				int imm=Integer.parseInt(s.substring(10,32),2);
				//System.out.println(imm);
				if(Integer.parseInt(s.substring(10,11),2)==1) {
					imm=(int)Math.pow(2, 22)-imm+1;
					//System.out.println((int)Math.pow(2, 22));
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(false);
				}
				else {
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(true);
				}
				//System.out.println(imm+" from OF imm");
				//IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setexwait(false);
				OF_EX_Latch.setEX_enable(true);
			}
			else if(containingProcessor.ControlUnit(Integer.parseInt(s.substring(0,5),2))==4) {  //store
				//System.out.println("of store");
				int imm=Integer.parseInt(s.substring(15,32),2);
				if(Integer.parseInt(s.substring(15,16),2)==1) {
					imm=(int)Math.pow(2, 17)-imm+1;
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(false);
				}
				else {
					OF_EX_Latch.set_immx(imm);
					OF_EX_Latch.set_isimmx(true);
					OF_EX_Latch.set_sign(true);
				}
				//System.out.println("immx "+imm);
				int op2=Integer.parseInt(s.substring(10,15),2);
				int op1=Integer.parseInt(s.substring(5,10),2);
				//System.out.println("op2,op1 "+op2+" "+op1);
				OF_EX_Latch.set_op1(op1);
				OF_EX_Latch.set_op2(op2);
				if(containingProcessor.isdatahazard(op1, op2,IF_OF_Latch.getpc())==true) {
					containingProcessor.incrementstall();
					IF_OF_Latch.setwait(true);
					OF_EX_Latch.setexwait(true);
					OF_EX_Latch.setEX_enable(false);
				}
				else {
					IF_OF_Latch.setwait(false);
					OF_EX_Latch.setexwait(false);
					OF_EX_Latch.setEX_enable(true);
					//containingProcessor.setpc(Integer.parseInt(s.substring(10,15),2), IF_OF_Latch.getpc());
				}
				//IF_OF_Latch.setOF_enable(false);
				
				
			}
			
		}
		else if(IF_OF_Latch.OF_wait()==true || containingProcessor.getMaStoreWait()==true) {
			System.out.println("OF stage is in wait");
			OF_EX_Latch.setexwait(true);
		}
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

}
