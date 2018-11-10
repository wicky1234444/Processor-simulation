package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.Event.EventType;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.StoreResponseEvent;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable()==true && EX_MA_Latch.getMAwait()==false && containingProcessor.getMaStoreWait()==false) {
			MA_RW_Latch.setpc(EX_MA_Latch.getpc());
			//System.out.println("memory access");
			System.out.println("MA stage"+EX_MA_Latch.getpc()+" pc");
			containingProcessor.setpipe(EX_MA_Latch.getpc());
			if(EX_MA_Latch.Is_store()) {
				System.out.println("is store");
				MA_RW_Latch.setisstore(true);
				//containingProcessor.getMainMemory().setWord(containingProcessor.getRegisterFile().getValue(EX_MA_Latch.get_dest()), EX_MA_Latch.get_alu());
				Simulator.getEventQueue2().addEvent(new MemoryWriteEvent(Clock.getCurrentTime()+containingProcessor.getL1d().getlatency(), this, containingProcessor.getL1d(),containingProcessor.getRegisterFile().getValue(EX_MA_Latch.get_dest()) ,  EX_MA_Latch.get_alu()));
				containingProcessor.setMaStoreWait(true);
			}
			else if(EX_MA_Latch.Is_load()==true) {
				//System.out.println("is load");
				MA_RW_Latch.setisload(true);
				//MA_RW_Latch.set_ld(containingProcessor.getMainMemory().getWord(EX_MA_Latch.get_alu()));
				Simulator.getEventQueue2().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+containingProcessor.getL1d().getlatency(), this, containingProcessor.getL1d(),  EX_MA_Latch.get_alu()));
				containingProcessor.setMaStoreWait(true);
			}
			else {
				MA_RW_Latch.setisload(false);
				MA_RW_Latch.setisstore(false);
			}
			if(EX_MA_Latch.get_isalu()==true) {
				MA_RW_Latch.setisalu(true);
				//System.out.println("got alu");
				MA_RW_Latch.setalu(EX_MA_Latch.get_alu());
			}
			else {
				MA_RW_Latch.setisalu(false);
			}
			if(EX_MA_Latch.getend()==true) {
				MA_RW_Latch.isend(true);
				//System.out.println("is end");
			}
			else {
				MA_RW_Latch.isend(false);
			}
			if(EX_MA_Latch.getdest()!=-1) {
				//System.out.println("dest "+EX_MA_Latch.getdest());
				MA_RW_Latch.setdest(EX_MA_Latch.getdest());
			}
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.setRWwait(false);
		}
		else if(EX_MA_Latch.getMAwait()==true || containingProcessor.getMaStoreWait()==true) {
			System.out.println("MA is in wait");
			MA_RW_Latch.setRWwait(true);
		}
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		if(event.getEventType() == EventType.StoreResponse) {
			StoreResponseEvent ev = (StoreResponseEvent) event;
			if(ev.getValue() == true) {
				containingProcessor.setMaStoreWait(false);
				MA_RW_Latch.setRWwait(false);
			}
		}
		else if(event.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent ev = (MemoryResponseEvent) event;
			//MA_RW_Latch.set_ld(containingProcessor.getMainMemory().getWord(EX_MA_Latch.get_alu()));
			System.out.println("----------------MA load event--------------");
			containingProcessor.setMaStoreWait(false);
			MA_RW_Latch.setRWwait(false);
			//MA_RW_Latch.setisload(true);
			MA_RW_Latch.setdest(EX_MA_Latch.getdest());
			System.out.println(EX_MA_Latch.getdest()+" destination value "+ev.getValue());
			MA_RW_Latch.set_ld(ev.getValue());
			System.out.println("---------------------MA end------------------");
		}
		
	}

}
