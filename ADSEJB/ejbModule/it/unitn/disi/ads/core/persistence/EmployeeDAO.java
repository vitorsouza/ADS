package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.domain.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 * Data access object (DAO) interface for the domain class Employee.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface EmployeeDAO extends BaseDAO<Employee> {
	/**
	 * Retrieves an employee given her username.
	 *
	 * @param username
	 *		The username that identifies the employee in the system.
	 *
	 * @return An employee object
	 */
	Employee retrieveByUsername(String username) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;

	/**
	 * Retrieves all the registered employees who are drivers.
	 *
	 * @return A list of Employee objects representing the drivers.
	 */
	List<Employee> retrieveDrivers();
}
