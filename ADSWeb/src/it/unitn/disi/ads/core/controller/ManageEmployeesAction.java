package it.unitn.disi.ads.core.controller;

import br.com.engenhodesoftware.util.TextUtils;
import br.com.engenhodesoftware.util.ejb3.application.CrudServiceLocal;
import br.com.engenhodesoftware.util.ejb3.application.filters.EnumMultipleChoiceFilter;
import br.com.engenhodesoftware.util.ejb3.application.filters.LikeFilter;
import br.com.engenhodesoftware.util.ejb3.controller.CrudAction;
import it.unitn.disi.ads.core.application.ManageEmployeesService;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.domain.EmployeeType;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 * Controller class responsible for mediating the communication between user interface and application service for the
 * use case "Manage Employees". This use case is a CRUD.
 * 
 * @author Vitor Souza (vitorsouza@gmail.com)
 */
@Named
@SessionScoped
public class ManageEmployeesAction extends CrudAction<Employee> {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ManageEmployeesAction.class.getCanonicalName());

	/** The list of employee types to be used in the form. */
	private List<SelectItem> types;

	/** The "Manage Employee" service. */
	@EJB
	private ManageEmployeesService manageEmployeesService;

	/** Input: the new password to set. */
	private String newPassword;

	/** Getter for newPassword. */
	public String getNewPassword() {
		return newPassword;
	}

	/** Setter for newPassword. */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#getCrudService() */
	@Override
	protected CrudServiceLocal<Employee> getCrudService() {
		return manageEmployeesService;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#getBundleName() */
	@Override
	public String getBundleName() {
		// Our application uses a single bundle for the whole system, called 'msgs'.
		return "msgs";
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#createNewEntity() */
	@Override
	protected Employee createNewEntity() {
		logger.log(Level.INFO, "Initializing an empty employee");

		// Create an empty entity.
		Employee newEntity = new Employee();

		return newEntity;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#checkSelectedEntity() */
	@Override
	protected void checkSelectedEntity() {
		logger.log(Level.INFO, "Checking selected employee: {0} - do nothing!", selectedEntity);
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#initFilters() */
	@Override
	protected void initFilters() {
		logger.log(Level.INFO, "Initializing filter types");

		// Filter by name.
		addFilter(new LikeFilter("manageEmployees.filter.byName", "name", getI18nMessage("msgs", "manageEmployees.text.filter.byName")));

		// Filter by type.
		List<EmployeeType> employeeTypes = new ArrayList<EmployeeType>();
		Map<String, String> labels = new HashMap<String, String>();
		for (EmployeeType type : EmployeeType.values()) {
			employeeTypes.add(type);
			labels.put(getI18nMessage("msgs", "enum.employeeType." + type), "" + type);
		}
		addFilter(new EnumMultipleChoiceFilter<EmployeeType>("manageEmployees.filter.byType", "type", getI18nMessage("msgs", "manageEmployees.text.filter.byType"), employeeTypes, labels, EmployeeType.class));
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#prepEntity() */
	@Override
	protected void prepEntity() {
		logger.log(Level.INFO, "Preparing employee for storage: {0} - do nothing!", selectedEntity);

		// Checks if the password has changed.
		if ((newPassword != null) && (newPassword.length() > 0)) {
			try {
				String md5pwd = TextUtils.produceMd5Hash(newPassword);
				selectedEntity.setPassword(md5pwd);
			}
			catch (NoSuchAlgorithmException e) {
				// No MD5 hash generation algorithm found by the JVM.
				logger.log(Level.SEVERE, "Changing password for user \"" + selectedEntity.getLogin() + "\" triggered an exception during MD5 hash generation.", e);
				
				// FIXME: add an exception, make CrudAction catch it and display the same page with the error...
			}
		}
	}

	/** @see br.com.engenhodesoftware.util.ejb3.controller.CrudAction#listTrash() */
	@Override
	protected String listTrash() {
		// List the usernames of the deleted employees.
		StringBuilder usernames = new StringBuilder();
		for (Employee entity : trashCan) usernames.append(entity.getLogin()).append(", ");

		// Removes the final comma and returns the string.
		int length = usernames.length();
		if (length > 0) usernames.delete(length - 2, length);

		logger.log(Level.INFO, "Listing the employees in the trash can: {0}", usernames.toString());
		return usernames.toString();
	}

	/** Getter for types. */
	public List<SelectItem> getTypes() {
		// Lazily initialize the types list.
		if (types == null) {
			String bundle = getBundleName();
			types = new ArrayList<SelectItem>();
			StringBuilder builder = new StringBuilder();
			for (EmployeeType type : EmployeeType.values()) {
				builder.append("" + type + "; ");
				types.add(new SelectItem(type, getI18nMessage(bundle, "enum.employeeType." + type)));
			}

			logger.log(Level.INFO, "Initializing types list: {0}", builder.toString());
		}

		return types;
	}
}
