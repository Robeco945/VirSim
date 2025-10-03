package simu.model;

import simu.framework.IEventType;

/**
 * Event types are defined by the requirements of the simulation model
 *
 * TODO: This must be adapted to the actual simulator
 */
public enum EventType implements IEventType {
    ARR_HOSPITAL, DEP_HOSPITAL, DEP_TREATMENT, DEP_AFTERCARE, DEP_CHECKUP,DEP_VACCINE,DEP_MORGUE;
}

