package it.unitn.disi.ads.core.domain;

import br.com.engenhodesoftware.util.ejb3.persistence.PersistentObjectSupport;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Domain class that represents an employee of the organization that responds to emergency calls. There are also the users of the ADS.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class Employee extends PersistentObjectSupport implements Serializable, Comparable<Employee> {
	private static final long serialVersionUID = 1L;

	/** The full name of the employee. */
	@NotNull
	@Size(max = 50)
	private String name;

	/** The name used by the employee to access the system. */
	@NotNull
	@Size(max = 20)
	private String login;

	/** The password used by the employee to access the system. */
	@NotNull
	@Size(max = 50)
	private String password;

	/** The type of employee (operator, dispatcher, driver). */
	@NotNull
	private EmployeeType type;

	/** Empty constructor. */
	public Employee() {
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param name
	 *		Employee's name.
	 * @param login
	 *		Employee's login.
	 * @param password
	 *		Employee's password.
	 * @param type
	 *		Type of employee.
	 */
	public Employee(String name, String login, String password, EmployeeType type) {
		this.name = name;
		this.login = login;
		this.password = password;
		this.type = type;
	}

	/** Getter for login. */
	public String getLogin() {
		return login;
	}

	/** Setter for login. */
	public void setLogin(String login) {
		this.login = login;
	}

	/** Getter for name. */
	public String getName() {
		return name;
	}

	/** Setter for name. */
	public void setName(String name) {
		this.name = name;
	}

	/** Getter for password. */
	public String getPassword() {
		return password;
	}

	/** Setter for password. */
	public void setPassword(String password) {
		this.password = password;
	}

	/** Getter for type. */
	public EmployeeType getType() {
		return type;
	}

	/** Setter for type. */
	public void setType(EmployeeType type) {
		this.type = type;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(Employee o) {
		// Compare by login. There shouldn't be two employees with the same login.
		return login.compareTo(o.login);
	}
}
