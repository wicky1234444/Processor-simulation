package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if((IF_EnableLatch.isIF_enable()==true || EX_IF_Latch.IF_enable()==true ) && containingProcessor.getexbranch()==false && containingProcessor.getMaStoreWait()==false)
		{	
			//System.out.println("running in IF");
			int currentPC=0;
			if(EX_IF_Latch.isBranchTaken() ) {
				currentPC = EX_IF_Latch.getBranchPC();
				EX_IF_Latch.setBranchTaken(false);
				
			}
			else {
				currentPC=containingProcessor.getRegisterFile().getProgramCounter();	
			}
			
			
			containingProcessor.getRegisterFile().setProgramCounter(currentPC);
			System.out.println("IF stage "+containingProcessor.getRegisterFile().getProgramCounter()+" pc");
			if(currentPC!=IF_EnableLatch.getpc()) {
				//System.out.println("in loooop "+containingProcessor.getRegisterFile().getProgramCounter());
			Simulator.getEventQueue1().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+containingProcessor.getL1i().getlatency(), this, (Element) containingProcessor.getL1i() , containingProcessor.getRegisterFile().getProgramCounter()));
			//System.out.println(Clock.getCurrentTime()+Configuration.mainMemoryLatency/40);
			}
			IF_EnableLatch.setpc(currentPC);
			//int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			/*String s=String.format("%32s",Integer.toBinaryString(newInstruction)).replace(' ', '0');
			if(Integer.parseInt(s.substring(0,5),2)==29) {
				//System.out.println(newInstruction);
					System.out.println("got end in if");
					IF_EnableLatch.setIF_enable(false);
					IF_EnableLatch.setend(true);
				}*/
			
			//IF_EnableLatch.setIF_enable(false);
			
		
			
		}
		else if(containingProcessor.getexbranch()==true) {
			
			containingProcessor.setexbranch(false);
			
		}
	}

	@Override
	public void handleEvent(Event e) {
		// TODO Auto-generated method stub
		System.out.println("------------------in IF handle event-------------------------");
		if(IF_OF_Latch.OF_wait()==true  || containingProcessor.getMaStoreWait()==true) {
			//System.out.println("here");
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue1().addEvent(e);
		}
		else {
			MemoryResponseEvent ev = (MemoryResponseEvent) e;
			System.out.println("in else");
			int newInstruction=ev.getValue();
			String s=String.format("%32s",Integer.toBinaryString(newInstruction)).replace(' ', '0');
			if(Integer.parseInt(s.substring(0,5),2)==29) {
				//System.out.println(newInstruction);
					//System.out.println("got end in if");
					IF_EnableLatch.setIF_enable(false);
					IF_EnableLatch.setend(true);
				}
		//	if(IF_OF_Latch.OF_wait()==false) {
				IF_OF_Latch.setpc(IF_EnableLatch.getpc());
				//IF_OF_Latch.setInstruction(newInstruction);
				IF_OF_Latch.setInstruction(ev.getValue());
				System.out.println("got instruction "+IF_OF_Latch.getInstruction()+" "+ev.getValue()+" with pc "+IF_EnableLatch.getpc());
				if(IF_EnableLatch.isend()==false) {
					//System.out.println("here in end false "+containingProcessor.getRegisterFile().getProgramCounter());
					containingProcessor.getRegisterFile().incrementProgramCounter();
					System.out.println("for next cycle IF stage pc increased to "+containingProcessor.getRegisterFile().getProgramCounter());
				}
			//	}
			//IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
			System.out.println("OF stage"+IF_OF_Latch.isOF_enable()+" of enable");
			
		}
		System.out.println("---------------------end of handle event-----------------------");
	}

}
