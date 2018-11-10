package processor.memorysystem;

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

public class Cache implements Element{
	int size;
	CacheLine[] cblock;
	int latency;
	int type;
	Processor p1;
	int readingaddress;
	
	public int getlatency() {
		return this.latency;
	}
	
	public Cache(int s, int l, Processor p, int t) {
		this.size = s;
		this.latency = l;
		this.p1 = p;
		this.type = t;
		cblock = new CacheLine[size];
		for(int i=0;i<size;i++) {
			cblock[i] =  new CacheLine();
		}
	}
	
	public void cacheread(int address) {
		int tagg = address/size;
		int index = address%size;
		if(cblock[index].tag == tagg) {
			System.out.println("hit");
			if(type==0) {
				Simulator.getEventQueue1().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(), this, p1.getIFUnit(), cblock[index].data));
			}
			else if(type==1) {
				Simulator.getEventQueue1().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(), this, p1.getMAUnit(), cblock[index].data));
			}
		}
		else if(cblock[index].tag!=tagg) {
			System.out.println("miss");
			handlecachemiss1(address);
		}
	}
	
	public void handlecachemiss1(int address) {
		readingaddress = address;
		Simulator.getEventQueue1().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,this, p1.getMainMemory(), address));
	}
	
	public void cachewrite(int address, int value) {
		int tagg = address/size;
		int index = address%size;
		if(cblock[index].tag == tagg) {
			System.out.println("hit");
			cblock[index].data = value;
			Simulator.getEventQueue2().addEvent(new StoreResponseEvent(Clock.getCurrentTime(), this, p1.getMAUnit(), true));
			Simulator.getEventQueue2().addEvent(new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this,p1.getMainMemory(), address, value));
		}
		else {
			System.out.println("miss");
			cblock[index].tag = tagg;
			cblock[index].data = value;
			//this.readingaddress=address;
			Simulator.getEventQueue2().addEvent(new StoreResponseEvent(Clock.getCurrentTime(), this, p1.getMAUnit(), true));
			Simulator.getEventQueue2().addEvent(new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this,p1.getMainMemory(), address, value));
		}
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		if(event.getEventType() == EventType.MemoryResponse) {
			System.out.println("adding response in cache");
			int tagg = this.readingaddress/size;
			int index = this.readingaddress%size;
			MemoryResponseEvent ev = (MemoryResponseEvent) event;
			cblock[index].data = ev.getValue();
			cblock[index].tag = tagg;
			if(type==0) {
				Simulator.getEventQueue1().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(), this, p1.getIFUnit(), ev.getValue()));
			}
			else if(type==1) {
				Simulator.getEventQueue1().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(), this, p1.getMAUnit(), ev.getValue()));
			}
			}
		else if(event.getEventType() == EventType.StoreResponse) {
			
		}
		else if(event.getEventType() == EventType.MemoryRead) {
			MemoryReadEvent ev = (MemoryReadEvent) event;
			cacheread(ev.getAddressToReadFrom());
		}
		else if(event.getEventType() == EventType.MemoryWrite) {
			MemoryWriteEvent ev = (MemoryWriteEvent) event;
			cachewrite(ev.getAddressToWriteTo(), ev.getValue());
		}
		
	}
}
