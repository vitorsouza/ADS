package it.unitn.disi.ads.core.controller;

import br.com.engenhodesoftware.util.ejb3.application.filters.LikeFilter;
import br.com.engenhodesoftware.util.ejb3.application.filters.SimpleFilter;
import br.com.engenhodesoftware.util.ejb3.controller.CrudAction;
import it.unitn.disi.ads.core.application.ManageAmbulancesService;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasMultipleCurrentDeactivationsException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasMultipleCurrentDriverAssignmentsException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasNoCurrentDeactivationException;
import it.unitn.disi.ads.core.application.exceptions.AmbulanceHasNoCurrentDriverAssignmentsException;
import it.unitn.disi.ads.core.application.exceptions.EmployeeIsNotDriverException;
import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.AmbulanceDeactivation;
import it.unitn.disi.ads.core.domain.DriverAssignment;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.persistence.EmployeeDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 * Controller class responsible for mediating the communication between user interface and application service for the
 * use case "Manage Ambulances". This use case is a CRUD.
 * 
 * @author Vitor Souza (vitorsouza@gmail.com)
 */
@Named
@SessionScoped
public class ManageAmbulancesAction extends CrudAction<Ambulance> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ManageAmbulancesAction.class.getCanonicalName());

	/** Path for the deactivations page. */
	private static final String DEACTIVATIONS_PAGE = "/core/manageAmbulances/deactivations.xhtml";

	/** Path for the assignments page. */
	private static final String ASSIGNMENTS_PAGE = "/core/manageAmbulances/assignments.xhtml";

	/** The "Manage Ambulance" service. */
	@EJB
	private ManageAmbulancesService manageAmbulancesService;

	/** The Employee DAO for loading drivers. */
	@EJB
	private EmployeeDAO employeeDAO;

	/** Map that holds the maintenance status of each ambulance in the entity list. */
	private Map<Long, String> currentStatuses;

	/** The current deactivation for the selected ambulance (null if ambulance is active). */
	private AmbulanceDeactivation currentDeactivation;

	/** All deactivations for the selected ambulance (lazy loaded). */
	private List<AmbulanceDeactivation> allDeactivations;

	/** Indicates if the current ambulance is active, so the form can enable/disable the correct fields. */
	private boolean active;

	/** Map that holds the current driver of each ambulance in the entity list. */
	private Map<Long, String> currentAssignments;

	/** The current assignment for the selected ambulance (null if ambulance has no driver assigned). */
	private DriverAssignment currentAssignment;

	/** Indicates if the current ambulance is not assigned, so the form can enable/disable the correct fields. */
	private boolean free;

	/** I/O data: the ID of the driver. */
	private Long driverId;

	/** All assignments for the selected ambulance (lazy loaded). */
	private List<DriverAssignment> allAssignments;

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#getCrudService() */
	@Override
	protected ManageAmbulancesService getCrudService() {
		return manageAmbulancesService;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#getBundleName() */
	@Override
	public String getBundleName() {
		// Our application uses a single bundle for the whole system, called 'msgs'.
		return "msgs";
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#createNewEntity() */
	@Override
	protected Ambulance createNewEntity() {
		logger.log(Level.INFO, "Initializing an empty ambulance");

		// Create an empty entity.
		Ambulance newEntity = new Ambulance();

		return newEntity;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#checkSelectedEntity() */
	@Override
	protected void checkSelectedEntity() {
		logger.log(Level.INFO, "Checking selected ambulance: {0} - do nothing!", selectedEntity);
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#initFilters() */
	@Override
	protected void initFilters() {
		logger.log(Level.INFO, "Initializing filter types");

		// Filter by number and license plate.
		addFilter(new SimpleFilter("manageAmbulances.filter.byNumber", "number", getI18nMessage("msgs", "manageAmbulances.text.filter.byNumber")));
		addFilter(new LikeFilter("manageAmbulances.filter.byLicensePlate", "licensePlate", getI18nMessage("msgs", "manageAmbulances.text.filter.byLicensePlate")));
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#prepEntity() */
	@Override
	protected void prepEntity() {
		logger.log(Level.INFO, "Preparing ambulance for storage: {0} - do nothing!", selectedEntity);
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#listTrash() */
	@Override
	protected String listTrash() {
		// List the usernames of the deleted ambulances.
		StringBuilder usernames = new StringBuilder();
		for (Ambulance entity : trashCan)
			usernames.append("# ").append(entity.getNumber()).append(" (").append(entity.getLicensePlate()).append("), ");

		// Removes the final comma and returns the string.
		int length = usernames.length();
		if (length > 0) usernames.delete(length - 2, length);

		logger.log(Level.INFO, "Listing the ambulances in the trash can: {0}.", usernames.toString());
		return usernames.toString();
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#retrieveEntities() */
	@Override
	protected void retrieveEntities() {
		// Override this method so the maintenance statuses and current drivers are reloaded every time the entities are.
		logger.log(Level.INFO, "Clearing the maps of maintenance status and current drivers so they can be reloaded with the new entities.");
		currentStatuses = null;
		currentAssignments = null;

		// Calls the method at the superclass.
		super.retrieveEntities();
	}

	/**
	 * Retrieves the current status (active or inactive) for an ambulance given its id. Gets this status from a map that is lazily 
	 * initialized.
	 * 
	 * This method allows the ambulance data table to fill in the status column.
	 *
	 * @param id
	 *		The id of the ambulance.
	 *
	 * @return
	 *		The status of the ambulance, as registered in the status map.
	 */
	public String retrieveCurrentStatus(Long id) {
		if (currentStatuses == null) initCurrentStatuses();
		String status = currentStatuses.get(id);
		logger.log(Level.INFO, "Retrieving maintenance status for ambulance with ID {0}: {1}.", new Object[] {id, status});
		return status;
	}

	/**
	 * Lazy initialization of the status map. For each ambulance that is currently being displayed (the current CRUD page, represented by the
	 * attribute entities), checks if there's an active deactivation and stores the appropriate status (active or inactive, properly
	 * internationalized) to the status map.
	 */
	private void initCurrentStatuses() {
		logger.log(Level.INFO, "Initializing maintenance status map for {0} entities.", entities.size());
		currentStatuses = new TreeMap<Long, String>();
		for (Ambulance ambulance : entities) {
			// Retrieves the current deactivation (if any) for this ambulance.
			AmbulanceDeactivation deactivation = null;
			try {
				deactivation = getCrudService().retrieveActiveDeactivation(ambulance);
			}
			catch (AmbulanceHasMultipleCurrentDeactivationsException e) {
				logger.log(Level.SEVERE, "There has been an error while retrieving the active deactivation of ambulance # " + ambulance.getNumber() + " (id " + ambulance.getId() + ")", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.deactivationError", ambulance.getNumber());
			}

			// Displays the status depending if the ambulance is active or not.
			currentStatuses.put(ambulance.getId(), getI18nMessage("msgs", (deactivation == null) ? "manageAmbulances.text.status.active" : "manageAmbulances.text.status.inactive"));
		}
	}

	/** Getter for currentDeactivation. */
	public AmbulanceDeactivation getCurrentDeactivation() {
		return currentDeactivation;
	}

	/** Getter for active. */
	public boolean isActive() {
		return active;
	}

	/** Getter for allDeactivations. */
	public List<AmbulanceDeactivation> getAllDeactivations() {
		return allDeactivations;
	}

	/**
	 * Called from a link in the ambulance table, selects an ambulance (through its id) and view the current (active) deactivation, if any.
	 * Redirects to the deactivations page, which allows the administrator to end the current deactivation or start one, if the ambulance is
	 * not currently deactivated.
	 *
	 * This method executes a scenario in the service class.
	 *
	 * @param id
	 *		The id of the selected ambulance.
	 *
	 * @return
	 *		The path to the deactivations page.
	 */
	public String manageDeactivations(Long id) {
		// Retrieve the selected ambulance.
		selectedEntity = getCrudService().retrieve(id);
		logger.log(Level.INFO, "Selected ambulance # {0} (id {1}). Opening deactivations page.", new Object[] {selectedEntity.getNumber(), id});

		// Clear the deactivation history (possibly of another ambulance).
		allDeactivations = null;

		// Retireve the current deactivation (if any) for this ambulance.
		try {
			currentDeactivation = getCrudService().retrieveActiveDeactivation(selectedEntity);
		}
		catch (AmbulanceHasMultipleCurrentDeactivationsException e) {
			logger.log(Level.SEVERE, "There has been an error while retrieving the active deactivation of ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + ")", e);
			addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.deactivationError", selectedEntity.getNumber());
			return null;
		}

		// If there is no current deactivation for the ambulance, create a new one for the new deactivation form.
		active = currentDeactivation == null;
		if (active) {
			currentDeactivation = new AmbulanceDeactivation();
			currentDeactivation.setBeginDate(new Date(System.currentTimeMillis()));
		}

		return DEACTIVATIONS_PAGE;
	}

	/**
	 * Called from the deactivation page's form, sends the data on the end of the current deactivation or the beginning of a new one and
	 * changes the status of the ambulance.
	 *
	 * This method executes a scenario in the service class.
	 */
	public void changeStatus() {
		// If the ambulance is active, create a deactivation. Otherwise, end the current deactivation.
		if (active) {
			logger.log(Level.INFO, "Deactivating ambulance # {0} (id {1}). Reason: {2}.", new Object[] {selectedEntity.getNumber(), selectedEntity.getId(), currentDeactivation.getReason()});
			currentDeactivation = getCrudService().deactivateAmbulance(selectedEntity, currentDeactivation.getReason());
			active = false;
			addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_INFO, "manageAmbulances.info.deactivationStarted");
		}
		else
			try {
				logger.log(Level.INFO, "Ending current deactivation for ambulance # {0} (id {1}).", new Object[] {selectedEntity.getNumber(), selectedEntity.getId()});
				getCrudService().endCurrentDeactivation(selectedEntity);
				currentDeactivation = null;
				active = true;
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_INFO, "manageAmbulances.info.deactivationEnded");
			}
			catch (AmbulanceHasMultipleCurrentDeactivationsException e) {
				logger.log(Level.SEVERE, "There has been an error while changing the status of ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + ")", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.deactivationError", selectedEntity.getNumber());
			}
			catch (AmbulanceHasNoCurrentDeactivationException e) {
				logger.log(Level.SEVERE, "There has been an error while changing the status of ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + ")", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.deactivationError", selectedEntity.getNumber());
			}
	}

	/**
	 * Called from the deactivations page, loads all deactivations associated with a given ambulance to see its history.
	 *
	 * This method is intended to be used with AJAX.
	 * This method executes a scenario in the service class.
	 */
	public void viewDeactivationHistory() {
		// Checks if there is an ambulance selected.
		if (selectedEntity != null)
			// Loads all deactivations for the selected ambulance.
			allDeactivations = getCrudService().retrieveAllDeactivations(selectedEntity);
	}

	/**
	 * Retrieves the current assignment (driver name or "unassigned") for an ambulance given its id. Gets this assignment from a map that is
	 * lazily initialized.
	 *
	 * This method allows the ambulance data table to fill in the status column.
	 *
	 * @param id
	 *		The id of the ambulance.
	 *
	 * @return
	 *		The assignment of the ambulance, as registered in the assignment map.
	 */
	public String retrieveCurrentAssignment(Long id) {
		if (currentAssignments == null) initCurrentAssignments();
		String driver = currentAssignments.get(id);
		logger.log(Level.INFO, "Retrieving current driver for ambulance with ID {0}: {1}", new Object[] {id, driver});
		return driver;
	}

	/**
	 * Lazy initialization of the assignment map. For each ambulance that is currently being displayed (the current CRUD page, represented by
	 * the attribute entities), checks if there's an active assignment and stores the appropriate information (driver name, or "unassigned",
	 * properly internationalized) to the assignment map.
	 */
	private void initCurrentAssignments() {
		logger.log(Level.INFO, "Initializing current drivers map for {0} entities.", entities.size());
		currentAssignments = new TreeMap<Long, String>();
		for (Ambulance ambulance : entities) {
			// Retrieves the current assignment (if any) for this ambulance.
			DriverAssignment assignment = null;
			try {
				assignment = getCrudService().retrieveActiveAssignment(ambulance);
			}
			catch (AmbulanceHasMultipleCurrentDriverAssignmentsException e) {
				logger.log(Level.SEVERE, "There has been an error while retrieving the active assignment of ambulance # " + ambulance.getNumber() + " (id " + ambulance.getId() + ")", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.assignmentError", ambulance.getNumber());
			}

			// Displays the status depending if the ambulance is active or not.
			currentAssignments.put(ambulance.getId(), getI18nMessage("msgs", (assignment == null) ? "manageAmbulances.text.currentDriver.none" : assignment.getDriver().getName()));
		}
	}

	/** Getter for currentAssignment. */
	public DriverAssignment getCurrentAssignment() {
		return currentAssignment;
	}

	/** Getter for free. */
	public boolean isFree() {
		return free;
	}

	/** Getter for driverId. */
	public Long getDriverId() {
		return driverId;
	}

	/** Setter for driverId. */
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	/** Getter for allAssignments. */
	public List<DriverAssignment> getAllAssignments() {
		return allAssignments;
	}

	/**
	 * Return the list of registered drivers for the JSF page.
	 *
	 * @return
	 *		A list of JSF SelectItem objects to fill a page component with the list of drivers.
	 */
	public List<SelectItem> getDriverList() {
		List<SelectItem> driverList = new ArrayList<SelectItem>();
		driverList.add(new SelectItem(null, getI18nMessage("msgs", "manageAmbulances.text.currentDriver.none")));

		List<Employee> drivers = employeeDAO.retrieveDrivers();
		for (Employee driver : drivers) driverList.add(new SelectItem(driver.getId(), driver.getName()));
		
		return driverList;
	}

	/**
	 * Called from a link in the ambulance table, selects an ambulance (through its id) and view the current (active) assignment, if any.
	 * Redirects to the assignments page, which allows the administrator to end the current assignment or change it, or even start a new one
	 * if the ambulance is not currently assigned.
	 *
	 * This method executes a scenario in the service class.
	 *
	 * @param id
	 *		The id of the selected ambulance.
	 *
	 * @return
	 *		The path to the assignments page.
	 */
	public String manageAssignments(Long id) {
		// Retrieve the selected ambulance.
		selectedEntity = getCrudService().retrieve(id);
		logger.log(Level.INFO, "Selected ambulance # {0} (id {1}). Opening assignments page.", new Object[] {selectedEntity.getNumber(), id});

		// Clear the assignment history (possibly of another ambulance).
		allAssignments = null;

		// Retireve the current assignment (if any) for this ambulance.
		try {
			currentAssignment = getCrudService().retrieveActiveAssignment(selectedEntity);
		}
		catch (AmbulanceHasMultipleCurrentDriverAssignmentsException e) {
			logger.log(Level.SEVERE, "There has been an error while retrieving the active assignment of ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + ")", e);
			addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.assignmentError", selectedEntity.getNumber());
			return null;
		}

		// If there is no current assignment for the ambulance, create a new one for the new assignment form.
		free = currentAssignment == null;
		if (free) {
			currentAssignment = new DriverAssignment();
			currentAssignment.setBeginDate(new Date(System.currentTimeMillis()));
		}

		return ASSIGNMENTS_PAGE;
	}

	/**
	 * Called from the assignment page's form, sends the data on the end of the current assignment and/or the beginning of a new one and
	 * changes the driver assigned to the ambulance.
	 *
	 * This method executes a scenario in the service class.
	 */
	public void changeAssignment() {
		// Only change an assignment if the driver actually changed.
		if ((driverId != null) && (currentAssignment.getDriver() != null) && (driverId.equals(currentAssignment.getDriver().getId()))) {
			logger.log(Level.WARNING, "Assigning ambulance # {0} (id {1}) with the same driver. Current assignment's driver's ID: {3}, supplied ID: {4}.", new Object[] {selectedEntity.getNumber(), selectedEntity.getId(), currentAssignment.getDriver().getId(), driverId});
			addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_WARN, "manageAmbulances.warn.sameDriverAssignment", currentAssignment.getDriver().getName());
		}
		else {
			Employee driver = (driverId == null) ? null : employeeDAO.retrieveById(driverId);

			try {
				// If the driver was specified, create a new assignment, possibly ending a current assignment.
				if (driver != null) {
					logger.log(Level.INFO, "Assigning ambulance # {0} (id {1}). Driver: {2}.", new Object[] {selectedEntity.getNumber(), selectedEntity.getId(), driver.getName()});
					currentAssignment = getCrudService().assignAmbulance(selectedEntity, driver);
					free = false;
					addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_INFO, "manageAmbulances.info.assignmentStarted");
				}
				// If not, end the current assignment.
				else {
					logger.log(Level.INFO, "Ending current assignment for ambulance # {0} (id {1}).", new Object[] {selectedEntity.getNumber(), selectedEntity.getId()});
					getCrudService().endCurrentAssignment(selectedEntity);
					currentAssignment = null;
					free = true;
					addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_INFO, "manageAmbulances.info.assignmentEnded");
				}
			}
			catch (AmbulanceHasMultipleCurrentDriverAssignmentsException e) {
				logger.log(Level.SEVERE, "There has been an error while assigning a driver to ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + ")", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.assignmentError", selectedEntity.getNumber());
				driverId = null;
			}
			catch (AmbulanceHasNoCurrentDriverAssignmentsException e) {
				logger.log(Level.SEVERE, "There has been an error while assigning a driver to ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + ")", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.assignmentError", selectedEntity.getNumber());
				driverId = null;
			}
			catch (EmployeeIsNotDriverException e) {
				logger.log(Level.SEVERE, "There has been an error while assigning a driver to ambulance # " + selectedEntity.getNumber() + " (id " + selectedEntity.getId() + "): the specified employee (" + driver.getLogin() + ") is not a driver!", e);
				addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_ERROR, "manageAmbulances.error.assignmentErrorNotDriver", driver.getName(), selectedEntity.getNumber());
				driverId = null;
			}
		}
	}

	/**
	 * Called from the assignments page, loads all driver assignments associated with a given ambulance to see its history.
	 *
	 * This method is intended to be used with AJAX.
	 * This method executes a scenario in the service class.
	 */
	public void viewAssignmentHistory() {
		// Checks if there is an ambulance selected.
		if (selectedEntity != null)
			// Loads all assignments for the selected ambulance.
			allAssignments = getCrudService().retrieveAllAssignments(selectedEntity);
	}
}
