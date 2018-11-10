package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		System.out.println("event added");
		queue.add(event);
	}

	public void event_flush() {
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void processEvents()
	{
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			System.out.println("event queue ");
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
		if(queue.isEmpty()) {
			System.out.println("queue empty");
		}
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		if(x.getEventTime() < y.getEventTime())
		{
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}
