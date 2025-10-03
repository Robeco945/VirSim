package simu.model;

import Roberto.Human;
import eduni.distributions.ContinuousGenerator;
import simu.framework.*;
import java.util.LinkedList;

/**
 * Service Point implements the functionalities, calculations and reporting.
 *
 * TODO: This must be modified to actual implementation. Things to be added:
 *     - functionalities of the service point
 *     - measurement variables added
 *     - getters to obtain measurement values
 *
 * Service point has a queue where customers are waiting to be serviced.
 * Service point simulated the servicing time using the given random number generator which
 * generated the given event (customer serviced) for that time.
 *
 * Service point collects measurement parameters.
 */
public class ServicePoint {
	private LinkedList<Human> queue = new LinkedList<>(); // Data Structure used
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
    private int totalServed = 0;
    public int totalDead = 0;
    private double sumServiceTime = 0.0;
    private double maxQueueLength = 0;
    public int getTotalServed() { return totalServed; }
    public int getTotalDead() { return totalDead; }

    public double getAvgServiceTime() { return totalServed == 0 ? 0 : sumServiceTime / totalServed; }
    public int getQueueLength() { return queue.size(); }
    public double getMaxQueueLength() { return maxQueueLength; }


    //Queuestrategy strategy; // option: ordering of the customer
	private boolean reserved = false;


	/**
	 * Create the service point with a waiting queue.
	 *
	 * @param generator Random number generator for service time simulation
	 * @param eventList Simulator event list, needed for the insertion of service ready event
	 * @param type Event type for the service end event
	 */
	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList = eventList;
		this.generator = generator;
		this.eventTypeScheduled = type;
	}

	/**
	 * Add a customer to the service point queue.
	 *
	 * @param a Customer to be queued
	 */
	public void addQueue(Human a) {	// The first customer of the queue is always in service
		queue.add(a);
        if(queue.size() > maxQueueLength)
            maxQueueLength = queue.size();
	}

	/**
	 * Remove customer from the waiting queue.
	 * Here we calculate also the appropriate measurement values.
	 *
	 * @return Customer retrieved from the waiting queue
	 */
	public Human removeQueue() {		// Remove serviced customer
		reserved = false;
        Human h = queue.poll();
        if (h != null) {
            totalServed++;
            sumServiceTime += Clock.getInstance().getClock() - h.getArrivalTime();
            if (h.isDead()){totalDead++;}
        }
        return h;	}

	/**
	 * Begins a new service, customer is on the queue during the service
	 *
	 * Inserts a new event to the event list when the service should be ready.
	 */
	public void beginService() {		// Begins a new service, customer is on the queue during the service
        if (!queue.isEmpty()) {
            Human Adam = queue.peek();
            Trace.out(Trace.Level.INFO, "Starting a new service for " + Adam);
        }
		
		reserved = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
	}

	/**
	 * Check whether the service point is busy
	 *
	 * @return logical value indicating service state
	 */
	public boolean isReserved(){
		return reserved;
	}

	/**
	 * Check whether there is customers on the waiting queue
	 *
	 * @return logival value indicating queue status
	 */
	public boolean isOnQueue(){
		return !queue.isEmpty();
	}
}
