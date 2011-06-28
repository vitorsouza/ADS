package it.unitn.disi.ads.core.application;

import br.com.engenhodesoftware.util.ejb3.application.CrudServiceLocal;
import javax.ejb.Local;

import it.unitn.disi.ads.core.domain.Employee;

/**
 * Local EJB interface for the service class (application class) that implements the use case "Manage Employees". This interface extends and
 * gets all of its methods from the CrudServiceLocal interface from the Engenho de Software CRUD framework for EJB3.
 *
 * @see br.com.engenhodesoftware.util.ejb3.application.CrudServiceLocal
 * @author Vitor Souza (vitorsouza@gmail.com)
 */
@Local
public interface ManageEmployeesService extends CrudServiceLocal<Employee> {
}
