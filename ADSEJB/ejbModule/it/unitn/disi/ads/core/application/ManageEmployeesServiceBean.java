package it.unitn.disi.ads.core.application;

import br.com.engenhodesoftware.util.ejb3.application.CrudException;
import br.com.engenhodesoftware.util.ejb3.application.CrudOperation;
import br.com.engenhodesoftware.util.ejb3.application.CrudService;
import br.com.engenhodesoftware.util.ejb3.application.filters.Filter;
import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.persistence.EmployeeDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Stateless EJB (service/application class) that implements the use case "Manage Employees". This class extends and gets most of its
 * methods from the CrudService abstract class from the Engenho de Software CRUD framework for EJB3. It implements a basic CRUD of Employee
 * objects.
 *
 * @see it.unitn.disi.ads.core.application.ManageEmployeesService
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@RolesAllowed({"ADMIN"})
@Stateless
public class ManageEmployeesServiceBean extends CrudService<Employee> implements ManageEmployeesService {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ManageEmployeesServiceBean.class.getCanonicalName());

	/** The DAO for Employee objects. */
	@EJB
	private EmployeeDAO employeeDAO;

	/** Information on the current user session. */
	@Inject
	private SessionInformation sessionInformation;

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#createNewEntity() */
	@Override
	protected Employee createNewEntity() {
		return new Employee();
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#getDAO() */
	@Override
	protected BaseDAO<Employee> getDAO() {
		return employeeDAO;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#validateCreate(br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	public void validateCreate(Employee entity) throws CrudException {
		// Possibly throwing a CRUD Exception to indicate validation error.
		CrudException crudException = null;
		String crudExceptionMessage = "The employee \"" + entity.getLogin() + "\" cannot be created due to validation errors.";

		// Validates business rules.
		// Rule 1: cannot have two employees with the same username.
		try {
			Employee anotherEntity = employeeDAO.retrieveByUsername(entity.getLogin());
			if (anotherEntity != null) {
				logger.log(Level.WARNING, "Creation of employee \"{0}\" violates validation rule: employee with id {1} has same username", new Object[] {entity.getLogin(), anotherEntity.getId()});
				crudException = addValidationError(crudException, crudExceptionMessage, "username", "manageEmployees.error.repeatedUsername", anotherEntity.getName());
			}
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.FINE, "Rule 1 OK - there's no other employee with the same username: {0}", entity.getName());
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Creation of employee with username \"" + entity.getLogin() + "\" threw an exception: a query for employees with this username returned multiple results!", e);
			crudException = addValidationError(crudException, crudExceptionMessage, "name", "manageEmployees.error.multipleInstancesError");
		}

		// If one of the rules was violated, throw the exception.
		if (crudException != null)
			throw crudException;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#validateUpdate(br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	public void validateUpdate(Employee entity) throws CrudException {
		// Possibly throwing a CRUD Exception to indicate validation error.
		CrudException crudException = null;
		String crudExceptionMessage = "The employee \"" + entity.getLogin() + "\" cannot be created due to validation errors.";

		// Validates business rules.
		// Rule 1: cannot have two employees with the same username.
		try {
			Employee anotherEntity = employeeDAO.retrieveByUsername(entity.getLogin());
			if ((anotherEntity != null) && (!anotherEntity.getId().equals(entity.getId()))) {
				logger.log(Level.WARNING, "Update of employee \"{0}\" violates validation rule: employee with id {1} has same username", new Object[] {entity.getLogin(), anotherEntity.getId()});
				crudException = addValidationError(crudException, crudExceptionMessage, "username", "manageEmployees.error.repeatedUsername", anotherEntity.getName());
			}
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.FINE, "Rule 1 OK - there's no other employee with the same username: {0}", entity.getName());
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Creation of employee with username \"" + entity.getLogin() + "\" threw an exception: a query for employees with this username returned multiple results!", e);
			crudException = addValidationError(crudException, crudExceptionMessage, "name", "manageEmployees.error.multipleInstancesError");
		}

		// If one of the rules was violated, throw the exception.
		if (crudException != null)
			throw crudException;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#validateDelete(br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	public void validateDelete(Employee entity) throws CrudException {
		// Possibly throwing a CRUD Exception to indicate validation error.
		CrudException crudException = null;
		String crudExceptionMessage = "The employee \"" + entity.getLogin() + "\" cannot be deleted because of validation errors.";

		// Validates business rules.
		// Rule 1: cannot delete yourself.
		if (sessionInformation.getCurrentUser().getId().equals(entity.getId())) {
			logger.log(Level.WARNING, "Deletion of employee \"{0}\" violates validation rule: cannot delete yourself", entity.getLogin());
			crudException = addValidationError(crudException, crudExceptionMessage, "manageEmployees.error.deleteYourself");
		}

		// FIXME: what about deleting employees that are related to other objects (EmergencyCall, Dispatch, DriverAssignment)?

		// If one of the rules was violated, throw the exception.
		if (crudException != null)
			throw crudException;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#log(br.com.engenhodesoftware.util.ejb3.application.CrudOperation, br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	protected void log(CrudOperation operation, Employee entity) {
		switch (operation) {
			case CREATE:
			case UPDATE:
				logger.log(Level.INFO, "Storing employee: {0} ({1} / {2})", new Object[] {entity.getLogin(), entity.getName(), entity.getType()});
				break;
			case RETRIEVE:
				logger.log(Level.INFO, "Loading employee with id {0}: {1}", new Object[] {entity.getId(), entity.getLogin()});
				break;
			case DELETE:
				logger.log(Level.INFO, "Deleting employee with id {0}: {1}", new Object[] {entity.getId(), entity.getLogin()});
				break;
		}
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#log(br.com.engenhodesoftware.util.ejb3.application.CrudOperation, java.util.List, int[]) */
	@Override
	protected void log(CrudOperation operation, List<Employee> entities, int... interval) {
		switch (operation) {
			case LIST:
				logger.log(Level.INFO, "Listing employees in interval [{0}, {1}): {2} employee(s) loaded.", new Object[] {interval[0], interval[1], entities.size()});
		}
	}

	/* 
	 * IMPORTANT NOTE:
	 *
	 * The methods below are overridden in order for the security annotation @RolesAllowed to work. That is the ONLY reason they are
	 * overridden. Otherwise the @RolesAllowed annotation applies the security measure only to the methods declared in this class, and not
	 * the ones inherited from the superclass. A better solution is desired.
	 *
	 */
	@Override
	public long count() {
		return super.count();
	}

	@Override
	public long countFiltered(Filter<?> filterType, String filter) {
		return super.countFiltered(filterType, filter);
	}

	@Override
	public void create(Employee entity) {
		super.create(entity);
	}

	@Override
	public void delete(Employee entity) {
		super.delete(entity);
	}

	@Override
	public List<Employee> filter(Filter<?> filter, String filterParam, int... interval) {
		return super.filter(filter, filterParam, interval);
	}

	@Override
	public List<Employee> list(int... interval) {
		return super.list(interval);
	}

	@Override
	public Employee retrieve(Long id) {
		return super.retrieve(id);
	}

	@Override
	public void update(Employee entity) {
		super.update(entity);
	}
}
