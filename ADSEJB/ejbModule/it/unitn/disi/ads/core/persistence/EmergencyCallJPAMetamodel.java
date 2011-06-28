package it.unitn.disi.ads.core.persistence;

import it.unitn.disi.ads.core.domain.Dispatch;
import it.unitn.disi.ads.core.domain.EmergencyCall;
import it.unitn.disi.ads.core.domain.EmergencyCallPriority;
import it.unitn.disi.ads.core.domain.Employee;
import java.util.Date;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@StaticMetamodel(EmergencyCall.class)
public class EmergencyCallJPAMetamodel {
	public static volatile SingularAttribute<EmergencyCall, Date> timeOfCall;
	public static volatile SingularAttribute<EmergencyCall, String> description;
	public static volatile SingularAttribute<EmergencyCall, String> address;
	public static volatile SingularAttribute<EmergencyCall, String> callerInfo;
	public static volatile SingularAttribute<EmergencyCall, EmergencyCallPriority> priority;
	public static volatile SingularAttribute<EmergencyCall, Boolean> completed;
	public static volatile SingularAttribute<EmergencyCall, Employee> operator;
	public static volatile SingularAttribute<EmergencyCall, Employee> dispatcher;
	public static volatile CollectionAttribute<EmergencyCall, Dispatch> dispatches;
	public static volatile SingularAttribute<EmergencyCall, EmergencyCall> duplicateOf;
	public static volatile CollectionAttribute<EmergencyCall, EmergencyCall> duplicates;
}
