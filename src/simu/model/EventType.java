package simu.model;

import simu.framework.IEventType;

// TODO:
// Event types are defined by the requirements of the simulation model
public enum EventType implements IEventType {
    ARR_HOSPITAL, DEP_HOSPITAL, DEP_TREATMENT, DEP_AFTERCARE, DEP_CHECKUP,DEP_VACCINE,DEP_MORGUE;
}
