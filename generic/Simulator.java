package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue1;
	static EventQueue eventQueue2;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) throws IOException
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException
	{
		FileInputStream in = null;
		try {
			in = new FileInputStream(assemblyProgramFile);
		}
		catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		DataInputStream din = new DataInputStream(in);
		int counter = din.readInt();
		processor.getRegisterFile().setProgramCounter(counter);
		for(int i=0;i<counter;i++) {
			try {
			processor.getMainMemory().setWord(i, din.readInt());
		}
			catch (Exception e) {
				e.printStackTrace();
			}
			}
		try {
		while(din.available()>0) {
			int instr=din.readInt();
			processor.getMainMemory().setWord(counter, instr);
			counter++;
		}
		}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		processor.getRegisterFile().setValue(0, 0);
		processor.getRegisterFile().setValue(1, 65535);
		processor.getRegisterFile().setValue(2, 65535);
		//System.out.println(processor.getMainMemory().getContentsAsString(0, 30));
	}
	
	public static void simulate()
	{
		eventQueue1 = new EventQueue();
		eventQueue2 = new EventQueue();
 		int cycles=0;
		while(getSimulationComplete() == false)
		{
			//for(int i=0;i<200;i++) {
				System.out.println("/////////"+Clock.getCurrentTime()+" clockcycle////////");
				processor.getRWUnit().performRW();
				//System.out.println("clockrw "+Clock.getCurrentTime());
				processor.getMAUnit().performMA();
				//System.out.println("clockma "+Clock.getCurrentTime());
				processor.getEXUnit().performEX();
				//System.out.println("clockex "+Clock.getCurrentTime());
				System.out.println("@@@@@@@@@---event handling---@@@@@@@@@@@");
				eventQueue1.processEvents();
				System.out.println("in event queue2");
				eventQueue2.processEvents();
				System.out.println("@@@@@@@@@----end of events----@@@@@@@@@@@");
				processor.getOFUnit().performOF();
				//System.out.println("clockof "+Clock.getCurrentTime());
				processor.getIFUnit().performIF();
				//System.out.println("clockif "+Clock.getCurrentTime());
				Clock.incrementClock();
			cycles++;
			System.out.println("///////////////////////");
		}
		
		// TODO
		// set statistics
			//System.out.println(processor.getMainMemory().getContentsAsString(65513,65535));
		Statistics st= new Statistics();
		st.setNumberOfInstructions(processor.get_numinstr());
		System.out.println(processor.get_branch()+" branch");
		System.out.println(processor.get_stall()+" stall");
		st.setNumberOfCycles(cycles);
	}
	
	public static EventQueue getEventQueue1() {
		return eventQueue1;
	}
	
	public static EventQueue getEventQueue2() {
		return eventQueue2;
	}
	
	public static void flush_event_queue() {
		eventQueue1=new EventQueue();
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
	
	public static boolean getSimulationComplete() {
		return simulationComplete;
	}
}
