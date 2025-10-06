package simu.model;

import Roberto.Human;
import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;

// TODO:
// Service Point functionalities & calculations (+ variables needed) and reporting to be implemented
public class ServicePoint {
	private LinkedList<Human> queue = new LinkedList<Human>(); // Data Structure used
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
    private int totalServed = 0;
    private double sumServiceTime = 0.0;
    private double maxQueueLength = 0;
    public int getTotalServed() { return totalServed; }
    public double getAvgServiceTime() { return totalServed == 0 ? 0 : sumServiceTime / totalServed; }
    public int getQueueLength() { return queue.size(); }
    public double getMaxQueueLength() { return maxQueueLength; }
	//Queuestrategy strategy; // option: ordering of the customer
	private boolean reserved = false;


	public ServicePoint(ContinuousGenerator generator, EventList tapahtumalista, EventType tyyppi){
		this.eventList = tapahtumalista;
		this.generator = generator;
		this.eventTypeScheduled = tyyppi;
				
	}

	public void addQueue(Human a){   // First customer at the queue is always on the service
		queue.add(a);
        if(queue.size() > maxQueueLength) maxQueueLength = queue.size();
	}

	public Human removeQueue(){		// Remove serviced customer
		reserved = false;
		Human h = queue.poll();
        if (h != null) {
            totalServed++;
            sumServiceTime += Clock.getInstance().getClock() - h.getArrivalTime();
        }
        return h;
	}

	public void beginService() {  		// Begins a new service, customer is on the queue during the service
		if (!queue.isEmpty()) {
            Human Adam = queue.peek();
            Trace.out(Trace.Level.INFO, "Starting a new service for " + Adam);
        }
        reserved = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
	}

	public boolean isReserved(){
		return reserved;
	}

	public boolean isOnQueue(){
		return !queue.isEmpty();
	}
}
