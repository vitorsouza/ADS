package it.unitn.disi.ads.core.persistence;

import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.DriverAssignment;
import it.unitn.disi.ads.core.domain.Employee;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@StaticMetamodel(DriverAssignment.class)
public class DriverAssignmentJPAMetamodel {
	public static volatile SingularAttribute<DriverAssignment, Date> beginDate;
	public static volatile SingularAttribute<DriverAssignment, Date> endDate;
	public static volatile SingularAttribute<DriverAssignment, Ambulance> ambulance;
	public static volatile SingularAttribute<DriverAssignment, Employee> driver;
}
