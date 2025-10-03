package simu.model;

import Roberto.Human;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;
import eduni.distributions.Negexp;

import java.util.Random;

/**
 * Main simulator engine.
 *
 * TODO: This is the place where you implement your own simulator
 *
 * Demo simulation case:
 * Simulate three service points, customer goes through all three service points to get serviced
 * 		--> SP1 --> SP2 --> SP3 -->
 */
public class MyEngine extends Engine {
	private ArrivalProcess arrivalProcess;
	private ServicePoint[] servicePoints;
	public static final boolean TEXTDEMO = true;
	public static final boolean FIXEDARRIVALTIMES = false;
	public static final boolean FXIEDSERVICETIMES = false;

	/**
	 * Service Points and random number generator with different distributions are created here.
	 * We use exponent distribution for customer arrival times and normal distribution for the
	 * service times.
	 */
	public MyEngine() {
		servicePoints = new ServicePoint[6];

		if (TEXTDEMO) {
			/* special setup for the example in text
			 * https://github.com/jacquesbergelius/PP-CourseMaterial/blob/master/1.1_Introduction_to_Simulation.md
			 */
			Random r = new Random();

			ContinuousGenerator arrivalTime = null;
			if (FIXEDARRIVALTIMES) {
				/* version where the arrival times are constant (and greater than service times) */

				// make a special "random number distribution" which produces constant value for the customer arrival times
				arrivalTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 10;
					}

					@Override
					public void setSeed(long seed) {
					}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {
					}
				};
			} else
				// exponential distribution is used to model customer arrivals times, to get variability between programs runs, give a variable seed
				arrivalTime = new Negexp(10, Integer.toUnsignedLong(r.nextInt()));

			ContinuousGenerator serviceTime = null;
			if (FXIEDSERVICETIMES) {
				// make a special "random number distribution" which produces constant value for the service time in service points
				serviceTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 9;
					}

					@Override
					public void setSeed(long seed) {
					}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {
					}
				};
			} else
				// normal distribution used to model service times
				serviceTime = new Normal(10, 6, Integer.toUnsignedLong(r.nextInt()));

			servicePoints[0] = new ServicePoint(serviceTime, eventList, EventType.DEP_HOSPITAL);
			servicePoints[1] = new ServicePoint(serviceTime, eventList, EventType.DEP_TREATMENT);
			servicePoints[2] = new ServicePoint(serviceTime, eventList, EventType.DEP_AFTERCARE);
            servicePoints[3] = new ServicePoint(serviceTime, eventList, EventType.DEP_CHECKUP);
            servicePoints[4] = new ServicePoint(serviceTime, eventList, EventType.DEP_VACCINE);
            servicePoints[5] = new ServicePoint(serviceTime, eventList, EventType.DEP_MORGUE);

			arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR_HOSPITAL);
		} else {
			/* more realistic simulation case with variable customer arrival times and service times */
			servicePoints[0] = new ServicePoint(new Normal(5, 3), eventList, EventType.DEP_HOSPITAL);
			servicePoints[1] = new ServicePoint(new Normal(10, 10), eventList, EventType.DEP_TREATMENT);
			servicePoints[2] = new ServicePoint(new Normal(15, 5), eventList, EventType.DEP_AFTERCARE);
            servicePoints[3] = new ServicePoint(new Normal(10, 3), eventList, EventType.DEP_CHECKUP);
            servicePoints[4] = new ServicePoint(new Normal(5, 2), eventList, EventType.DEP_VACCINE);
            servicePoints[5] = new ServicePoint(new Normal(5, 2), eventList, EventType.DEP_MORGUE);

			arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, EventType.ARR_HOSPITAL);
		}
	}

	@Override
	protected void initialize() {	// First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	@Override
	protected void runEvent(Event t) {  // B phase events
		Human a;

		switch ((EventType)t.getType()) {
		case ARR_HOSPITAL:
			servicePoints[0].addQueue(new Human());
			arrivalProcess.generateNextEvent();
			break;

		case DEP_HOSPITAL:
			a = servicePoints[0].removeQueue();
            if(a.getReason()){servicePoints[3].addQueue(a);}
			else {servicePoints[1].addQueue(a);}
			break;

		case DEP_TREATMENT:
			a = servicePoints[1].removeQueue();
            if(a.getSeverity()>=9){
                servicePoints[5].addQueue(a);
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                break;
            }
			else{a.setIllness(false);
                servicePoints[2].addQueue(a);}
			break;

        case DEP_AFTERCARE:
             a = servicePoints[2].removeQueue();
             servicePoints[3].addQueue(a);
             break;

        case DEP_CHECKUP:
             a = servicePoints[3].removeQueue();
             if(a.getIllness()){servicePoints[1].addQueue(a);}
             else {servicePoints[4].addQueue(a);}
             break;

		case DEP_VACCINE:
			a = servicePoints[4].removeQueue();
            a.setVaccine(true);
			a.setRemovalTime(Clock.getInstance().getClock());
		    a.reportResults();
			break;
		}
	}

	@Override
	protected void tryCEvents() {
		for (ServicePoint p: servicePoints){
			if (!p.isReserved() && p.isOnQueue()){
				p.beginService();
			}
		}
	}

    @Override
    protected void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());
        for (int i = 0; i < servicePoints.length; i++) {
            ServicePoint sp = servicePoints[i];
            System.out.println("ServicePoint " + i + ": served=" + sp.getTotalServed() +
                    ", avgTime=" + sp.getAvgServiceTime() +
                    ", maxQueue=" + sp.getMaxQueueLength());
        }
        int totalCustomers = 0;
        double totalServiceTime = 0;
        for (ServicePoint sp: servicePoints) {
            totalCustomers += sp.getTotalServed();
            totalServiceTime += sp.getAvgServiceTime() * sp.getTotalServed();
        }
        System.out.println("Total customers serviced: " + totalCustomers);
        System.out.println("Overall mean service time: " + (totalCustomers == 0 ? 0 : totalServiceTime / totalCustomers));

    }

}
