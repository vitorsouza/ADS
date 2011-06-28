package it.unitn.disi.ads.core.application;

import br.com.engenhodesoftware.util.ejb3.application.CrudException;
import br.com.engenhodesoftware.util.ejb3.application.CrudOperation;
import br.com.engenhodesoftware.util.ejb3.application.CrudService;
import br.com.engenhodesoftware.util.ejb3.application.filters.Filter;
import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasMultipleCurrentDeactivationsException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasMultipleCurrentDriverAssignmentsException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasNoCurrentDeactivationException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasNoCurrentDriverAssignmentsException;
import it.unitn.disi.ads.core.application.exceptions.EmployeeIsNotDriverException;
import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.AmbulanceDeactivation;
import it.unitn.disi.ads.core.domain.DriverAssignment;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.domain.EmployeeType;
import it.unitn.disi.ads.core.persistence.AmbulanceDAO;
import it.unitn.disi.ads.core.persistence.AmbulanceDeactivationDAO;
import it.unitn.disi.ads.core.persistence.DriverAssignmentDAO;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NonUniqueResultException;

/**
 * Stateless EJB (service/application class) that implements the use case "Manage Ambulances". This class extends and gets most of its
 * methods from the CrudService abstract class from the Engenho de Software CRUD framework for EJB3. It implements a basic CRUD of Ambulance
 * objects and some extra methods specific for this use case.
 *
 * @see it.unitn.disi.ads.core.application.ManageAmbulancesService
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@RolesAllowed({"ADMIN"})
@Stateless
public class ManageAmbulancesServiceBean extends CrudService<Ambulance> implements ManageAmbulancesService {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ManageAmbulancesServiceBean.class.getCanonicalName());

	/** The DAO for Ambulance objects. */
	@EJB
	private AmbulanceDAO ambulanceDAO;

	/** The DAO for AmbulanceDeactivation objects. */
	@EJB
	private AmbulanceDeactivationDAO ambulanceDeactivationDAO;

	/** The DAO for DriverAssignment objects. */
	@EJB
	private DriverAssignmentDAO driverAssignmentDAO;

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#createNewEntity() */
	@Override
	protected Ambulance createNewEntity() {
		return new Ambulance();
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#getDAO() */
	@Override
	protected BaseDAO<Ambulance> getDAO() {
		return ambulanceDAO;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#validateCreate(br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	public void validateCreate(Ambulance entity) throws CrudException {
		// Possibly throwing a CRUD Exception to indicate validation error.
		CrudException crudException = null;
		String crudExceptionMessage = "The ambulance # " + entity.getNumber() + " cannot be created due to validation errors.";

		// Validates business rules.
		// Rule 1: cannot have two ambulances with the same number.
		try {
			Ambulance anotherEntity = ambulanceDAO.retrieveByNumber(entity.getNumber());
			if (anotherEntity != null) {
				logger.log(Level.WARNING, "Creation of ambulance # {0} violates validation rule: ambulance with id {1} has same number", new Object[] {entity.getNumber(), anotherEntity.getId()});
				crudException = addValidationError(crudException, crudExceptionMessage, "number", "manageAmbulances.error.repeatedNumber");
			}
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.FINE, "Rule 1 OK - there's no other ambulance with the same number: {0}", entity.getNumber());
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Creation of ambulance with number \"" + entity.getNumber() + "\" threw an exception: a query for ambulances with this number returned multiple results!", e);
			crudException = addValidationError(crudException, crudExceptionMessage, "name", "manageAmbulances.error.multipleInstancesError");
		}

		// Validates business rules.
		// Rule 2: cannot have two ambulances with the same license plate.
		try {
			Ambulance anotherEntity = ambulanceDAO.retrieveByLicensePlate(entity.getLicensePlate());
			if (anotherEntity != null) {
				logger.log(Level.WARNING, "Creation of ambulance # {0} violates validation rule: ambulance with id {1} has same license plate", new Object[] {entity.getNumber(), anotherEntity.getId()});
				crudException = addValidationError(crudException, crudExceptionMessage, "licensePlate", "manageAmbulances.error.repeatedLicensePlate", anotherEntity.getNumber());
			}
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.FINE, "Rule 1 OK - there's no other ambulance with the same license plate: {0}", entity.getLicensePlate());
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Creation of ambulance with license plate \"" + entity.getLicensePlate() + "\" threw an exception: a query for ambulances with this plate returned multiple results!", e);
			crudException = addValidationError(crudException, crudExceptionMessage, "name", "manageAmbulances.error.multipleInstancesError");
		}

		// If one of the rules was violated, throw the exception.
		if (crudException != null)
			throw crudException;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#validateUpdate(br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	public void validateUpdate(Ambulance entity) throws CrudException {
		// Possibly throwing a CRUD Exception to indicate validation error.
		CrudException crudException = null;
		String crudExceptionMessage = "The ambulance # " + entity.getNumber() + " cannot be created due to validation errors.";

		// Validates business rules.
		// Rule 1: cannot have two ambulances with the same number.
		try {
			Ambulance anotherEntity = ambulanceDAO.retrieveByNumber(entity.getNumber());
			if ((anotherEntity != null) && (!anotherEntity.getId().equals(entity.getId()))) {
				logger.log(Level.WARNING, "Update of ambulance # {0} violates validation rule: ambulance with id {1} has same number", new Object[] {entity.getNumber(), anotherEntity.getId()});
				crudException = addValidationError(crudException, crudExceptionMessage, "number", "manageAmbulances.error.repeatedNumber");
			}
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.FINE, "Rule 1 OK - there's no other ambulance with the same number: {0}", entity.getNumber());
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Creation of ambulance with number \"" + entity.getNumber() + "\" threw an exception: a query for ambulances with this number returned multiple results!", e);
			crudException = addValidationError(crudException, crudExceptionMessage, "name", "manageAmbulances.error.multipleInstancesError");
		}

		// Validates business rules.
		// Rule 2: cannot have two ambulances with the same license plate.
		try {
			Ambulance anotherEntity = ambulanceDAO.retrieveByLicensePlate(entity.getLicensePlate());
			if ((anotherEntity != null) && (!anotherEntity.getId().equals(entity.getId()))) {
				logger.log(Level.WARNING, "Update of ambulance # {0} violates validation rule: ambulance with id {1} has same license plate", new Object[] {entity.getNumber(), anotherEntity.getId()});
				crudException = addValidationError(crudException, crudExceptionMessage, "number", "manageAmbulances.error.repeatedLicensePlate", anotherEntity.getNumber());
			}
		}
		catch (PersistentObjectNotFoundException e) {
			logger.log(Level.FINE, "Rule 1 OK - there's no other ambulance with the same license plate: {0}", entity.getLicensePlate());
		}
		catch (MultiplePersistentObjectsFoundException e) {
			logger.log(Level.WARNING, "Creation of ambulance with license plate \"" + entity.getLicensePlate() + "\" threw an exception: a query for ambulances with this plate returned multiple results!", e);
			crudException = addValidationError(crudException, crudExceptionMessage, "name", "manageAmbulances.error.multipleInstancesError");
		}

		// If one of the rules was violated, throw the exception.
		if (crudException != null)
			throw crudException;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#validateDelete(br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	public void validateDelete(Ambulance entity) throws CrudException {
		// Possibly throwing a CRUD Exception to indicate validation error.
		CrudException crudException = null;
		// String crudExceptionMessage = "The ambulance \"" + entity.getLogin() + "\" cannot be deleted because of validation errors.";

		// FIXME: what are the validation rules for deletion?

		// If one of the rules was violated, throw the exception.
		if (crudException != null)
			throw crudException;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#log(br.com.engenhodesoftware.util.ejb3.application.CrudOperation, br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject) */
	@Override
	protected void log(CrudOperation operation, Ambulance entity) {
		switch (operation) {
			case CREATE:
			case UPDATE:
				logger.log(Level.INFO, "Storing ambulance: # {0} ({1})", new Object[] {entity.getNumber(), entity.getLicensePlate()});
				break;
			case RETRIEVE:
				logger.log(Level.INFO, "Loading ambulance with id {0}: # {1}", new Object[] {entity.getId(), entity.getNumber()});
				break;
			case DELETE:
				logger.log(Level.INFO, "Deleting ambulance with id {0}: # {1}", new Object[] {entity.getId(), entity.getNumber()});
				break;
		}
	}

	/** @see br.com.engenhodesoftware.util.ejb3.application.CrudService#log(br.com.engenhodesoftware.util.ejb3.application.CrudOperation, java.util.List, int[]) */
	@Override
	protected void log(CrudOperation operation, List<Ambulance> entities, int... interval) {
		switch (operation) {
			case LIST:
				logger.log(Level.INFO, "Listing ambulances in interval [{0}, {1}): {2} ambulance(s) loaded.", new Object[] {interval[0], interval[1], entities.size()});
		}
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#retrieveActiveDeactivation(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public AmbulanceDeactivation retrieveActiveDeactivation(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDeactivationsException {
		// Tries to retrieve a single active deactivation.
		AmbulanceDeactivation deactivation = null;
		try {
			logger.log(Level.INFO, "Retrieving active deactivation for ambulance # {0} (id {1}).", new Object[] {ambulance.getNumber(), ambulance.getId()});
			deactivation = ambulanceDeactivationDAO.retrieveActiveByAmbulance(ambulance);
		}
		
		// An ambulance shouldn't have two active deactivations.
		catch (NonUniqueResultException e) {
			throw new AmbulanceHasMultipleCurrentDeactivationsException(e);
		}

		return deactivation;
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#retrieveAllDeactivations(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public List<AmbulanceDeactivation> retrieveAllDeactivations(Ambulance ambulance) {
		// Retrieves all deactivations for a given ambulance.
		logger.log(Level.INFO, "Retrieving all deactivations for ambulance # {0} (id {1}).", new Object[] {ambulance.getNumber(), ambulance.getId()});
		return ambulanceDeactivationDAO.retrieveByAmbulance(ambulance);
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#deactivateAmbulance(it.unitn.disi.ads.core.domain.Ambulance, java.lang.String) */
	@Override
	public AmbulanceDeactivation deactivateAmbulance(Ambulance ambulance, String reason) {
		// Creates a new deactivation for the ambulance.
		AmbulanceDeactivation deactivation = new AmbulanceDeactivation();
		deactivation.setAmbulance(ambulance);
		deactivation.setBeginDate(new Date(System.currentTimeMillis()));
		deactivation.setReason(reason);

		// Saves the deactivation.
		logger.log(Level.INFO, "Saving deactivation for ambulance # {0} (id {1}). Begin date: {2}, end date: {3}, reason: {4}.", new Object[] {ambulance.getNumber(), ambulance.getId(), deactivation.getBeginDate(), deactivation.getEndDate(), deactivation.getReason()});
		ambulanceDeactivationDAO.save(deactivation);
		return deactivation;
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#endCurrentDeactivation(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public AmbulanceDeactivation endCurrentDeactivation(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDeactivationsException, AmbulanceHasNoCurrentDeactivationException {
		// Retrieves the current deactivation.
		AmbulanceDeactivation deactivation = retrieveActiveDeactivation(ambulance);

		// To execute this scenario, the ambulance has to be currently deactivated.
		if (deactivation == null) throw new AmbulanceHasNoCurrentDeactivationException();

		// Ends the deactivation now and saves it.
		deactivation.setEndDate(new Date(System.currentTimeMillis()));
		logger.log(Level.INFO, "Saving deactivation for ambulance # {0} (id {1}). Begin date: {2}, end date: {3}, reason: {4}.", new Object[] {ambulance.getNumber(), ambulance.getId(), deactivation.getBeginDate(), deactivation.getEndDate(), deactivation.getReason()});
		ambulanceDeactivationDAO.save(deactivation);
		return deactivation;
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#retrieveActiveAssignment(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public DriverAssignment retrieveActiveAssignment(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDriverAssignmentsException {
		// Tries to retrieve a single active assignment.
		DriverAssignment assignment = null;
		try {
			logger.log(Level.INFO, "Retrieving active assignment for ambulance # {0} (id {1}).", new Object[] {ambulance.getNumber(), ambulance.getId()});
			assignment = driverAssignmentDAO.retrieveActiveByAmbulance(ambulance);
		}

		// An ambulance shouldn't have two active assignments.
		catch (NonUniqueResultException e) {
			throw new AmbulanceHasMultipleCurrentDriverAssignmentsException(e);
		}

		return assignment;
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#retrieveAllAssignments(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public List<DriverAssignment> retrieveAllAssignments(Ambulance ambulance) {
		// Retrieves all assignments for a given ambulance.
		logger.log(Level.INFO, "Retrieving all assignments for ambulance # {0} (id {1}).", new Object[] {ambulance.getNumber(), ambulance.getId()});
		return driverAssignmentDAO.retrieveByAmbulance(ambulance);
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#assignAmbulance(it.unitn.disi.ads.core.domain.Ambulance, it.unitn.disi.ads.core.domain.Employee) */
	@Override
	public DriverAssignment assignAmbulance(Ambulance ambulance, Employee driver) throws AmbulanceHasMultipleCurrentDriverAssignmentsException, EmployeeIsNotDriverException {
		Date today = new Date(System.currentTimeMillis());

		// Makes sure that the employee is a driver.
		if (driver.getType() != EmployeeType.DRIVER) throw new EmployeeIsNotDriverException();

		// Checks if the ambulance has a current assignment and, if so, end it.
		DriverAssignment currentAssignment = retrieveActiveAssignment(ambulance);
		if (currentAssignment != null) {
			currentAssignment.setEndDate(today);
			driverAssignmentDAO.save(currentAssignment);
		}

		// Creates a new assignment for the ambulance.
		DriverAssignment assignment = new DriverAssignment();
		assignment.setAmbulance(ambulance);
		assignment.setBeginDate(today);
		assignment.setDriver(driver);

		// Saves the deactivation.
		logger.log(Level.INFO, "Saving assignment for ambulance # {0} (id {1}). Begin date: {2}, end date: {3}, driver: {4}.", new Object[] {ambulance.getNumber(), ambulance.getId(), assignment.getBeginDate(), assignment.getEndDate(), assignment.getDriver().getName()});
		driverAssignmentDAO.save(assignment);
		return assignment;
	}

	/** @see it.unitn.disi.ads.core.application.ManageAmbulancesService#endCurrentAssignment(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public DriverAssignment endCurrentAssignment(Ambulance ambulance) throws AmbulanceHasMultipleCurrentDriverAssignmentsException, AmbulanceHasNoCurrentDriverAssignmentsException {
		// Retrieves the current assignment.
		DriverAssignment assignment = retrieveActiveAssignment(ambulance);

		// To execute this scenario, the ambulance has to be currently assigned to a driver.
		if (assignment == null) throw new AmbulanceHasNoCurrentDriverAssignmentsException();

		// Ends the assignment now and saves it.
		assignment.setEndDate(new Date(System.currentTimeMillis()));
		logger.log(Level.INFO, "Saving assignment for ambulance # {0} (id {1}). Begin date: {2}, end date: {3}, driver: {4}.", new Object[] {ambulance.getNumber(), ambulance.getId(), assignment.getBeginDate(), assignment.getEndDate(), assignment.getDriver().getName()});
		driverAssignmentDAO.save(assignment);
		return assignment;
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
	public void create(Ambulance entity) {
		super.create(entity);
	}

	@Override
	public void delete(Ambulance entity) {
		super.delete(entity);
	}

	@Override
	public List<Ambulance> filter(Filter<?> filter, String filterParam, int... interval) {
		return super.filter(filter, filterParam, interval);
	}

	@Override
	public List<Ambulance> list(int... interval) {
		return super.list(interval);
	}

	@Override
	public Ambulance retrieve(Long id) {
		return super.retrieve(id);
	}

	@Override
	public void update(Ambulance entity) {
		super.update(entity);
	}
}
