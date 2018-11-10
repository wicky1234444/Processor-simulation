package processor.memorysystem;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;

import generic.Element;
import generic.Event;
import generic.Event.EventType;
import processor.Clock;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.StoreResponseEvent;

public class MainMemory implements Element{
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		if(event.getEventType()==EventType.MemoryRead) {
			MemoryReadEvent ev = (MemoryReadEvent) event;
			System.out.println("adding memory response event in main memory");
			Simulator.getEventQueue1().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(), this, ev.getRequestingElement(), getWord(ev.getAddressToReadFrom())));
			System.out.println(getWord(ev.getAddressToReadFrom()));
		}
		else if(event.getEventType()==EventType.MemoryWrite) {
			MemoryWriteEvent ev = (MemoryWriteEvent) event;
			setWord(ev.getAddressToWriteTo(), ev.getValue());
			System.out.println("written to address"+ev.getAddressToWriteTo()+" value is "+ev.getValue());
			Simulator.getEventQueue2().addEvent(new StoreResponseEvent(Clock.getCurrentTime(), this, ev.getRequestingElement(), true));
		}
		
	}
}
