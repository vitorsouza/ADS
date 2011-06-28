package it.unitn.disi.ads.core.persistence;

import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.domain.EmployeeType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@StaticMetamodel(Employee.class)
public class EmployeeJPAMetamodel {
	public static volatile SingularAttribute<Employee, String> name;
	public static volatile SingularAttribute<Employee, String> login;
	public static volatile SingularAttribute<Employee, String> password;
	public static volatile SingularAttribute<Employee, EmployeeType> type;
}
