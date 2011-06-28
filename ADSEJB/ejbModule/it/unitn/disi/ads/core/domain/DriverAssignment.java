package it.unitn.disi.ads.core.domain;

import br.com.engenhodesoftware.util.ejb3.persistence.PersistentObjectSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Domain class that represents the assignment of a driver to an ambulance. Drivers can drive different ambulances at different periods of
 * time and the objects of this class register which driver was driving which ambulance during which period.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class DriverAssignment extends PersistentObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Date the assignment began. */
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date beginDate;

	/** Date the assignment ended (null if it's still effective). */
	@Temporal(TemporalType.DATE)
	private Date endDate;

	/** The ambulance. */
	@ManyToOne
	@NotNull
	private Ambulance ambulance;

	/** The driver. */
	@ManyToOne
	@NotNull
	private Employee driver;

	/** Empty constructor. */
	public DriverAssignment() {
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param beginDate
	 *		Date of the beginning of the assignment.
	 * @param ambulance
	 *		Ambulance assigned.
	 * @param driver
	 *		Employee to which the ambulance is assigned.
	 */
	public DriverAssignment(Date beginDate, Ambulance ambulance, Employee driver) {
		this.beginDate = beginDate;
		this.ambulance = ambulance;
		this.driver = driver;
	}

	/** Getter for ambulance. */
	public Ambulance getAmbulance() {
		return ambulance;
	}

	/** Setter for ambulance. */
	public void setAmbulance(Ambulance ambulance) {
		this.ambulance = ambulance;
	}

	/** Getter for beginDate. */
	public Date getBeginDate() {
		return beginDate;
	}

	/** Setter for beginDate. */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/** Getter for driver. */
	public Employee getDriver() {
		return driver;
	}

	/** Setter for driver. */
	public void setDriver(Employee driver) {
		this.driver = driver;
	}

	/** Getter for endDate. */
	public Date getEndDate() {
		return endDate;
	}

	/** Setter for end. */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
