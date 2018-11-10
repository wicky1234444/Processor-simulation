package generic;

import generic.Event.EventType;

public class StoreResponseEvent extends Event {
	boolean value;
	
	public StoreResponseEvent(long eventTime, Element requestingElement, Element processingElement, boolean value) {
		super(eventTime, EventType.StoreResponse, requestingElement, processingElement);
		this.value = value;
	}

	public boolean getValue() {
		return this.value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
