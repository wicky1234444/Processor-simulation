package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable() && MA_RW_Latch.getRWwait()==false && containingProcessor.getMaStoreWait()==false)
		{	containingProcessor.incrementinstr();
			//System.out.println("register write");
			System.out.println("RW stage "+MA_RW_Latch.getpc()+" pc");
			//containingProcessor.printpipe();
			containingProcessor.delpc(MA_RW_Latch.getpc());
			if(MA_RW_Latch.getend()==true) {
				System.out.println("got end");
				containingProcessor.getRegisterFile().incrementProgramCounter();
				Simulator.setSimulationComplete(true);}
			else {
				if(MA_RW_Latch.getisload()==true) {
					//System.out.println("is load");
					System.out.println("value "+ MA_RW_Latch.get_ld()+" stored in register x"+MA_RW_Latch.getdest());
					containingProcessor.getRegisterFile().setValue(MA_RW_Latch.getdest(), MA_RW_Latch.get_ld());
					//Simulator.getEventQueue(new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.,this, containingProcessor.getRegisterFile(), address, value));
				}
				else if(MA_RW_Latch.getisstore()) {
					//System.out.println("isstore");
				}
				else if(MA_RW_Latch.getdest()!=-1 && MA_RW_Latch.getisalu()==true) {
					//System.out.println("alu");
					//System.out.println("value "+ MA_RW_Latch.getalu());
					containingProcessor.getRegisterFile().setValue(MA_RW_Latch.getdest(),MA_RW_Latch.getalu() );
				}
			}
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			
			//MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
		else if(MA_RW_Latch.getRWwait()==true || containingProcessor.getMaStoreWait()==true) {
			System.out.println("RW wait");
		}
	}

}
