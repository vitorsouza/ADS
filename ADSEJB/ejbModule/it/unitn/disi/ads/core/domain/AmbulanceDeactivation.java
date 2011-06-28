package it.unitn.disi.ads.core.domain;

import br.com.engenhodesoftware.util.ejb3.persistence.PersistentObjectSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Domain class that represents the deactivation of an ambulance. Is an ambulance is deactivated, it can't be dispatched during the period
 * that it's inactive. Ambulances can be deactivated for many reasons, such as maintenance, shortage of drivers, breakage, etc.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class AmbulanceDeactivation extends PersistentObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Date the deactivation began. */
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date beginDate;

	/** Date the deactivation ended (null if it's still effective). */
	@Temporal(TemporalType.DATE)
	private Date endDate;

	/** Reason for the deactivation. */
	@Size(max = 250)
	private String reason;

	/** The ambulance. */
	@ManyToOne
	@NotNull
	private Ambulance ambulance;

	/** Empty constructor. */
	public AmbulanceDeactivation() {
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param beginDate
	 *		Date of the beginning of the deactivation.
	 * @param ambulance
	 *		Ambulance that has been deactivated.
	 */
	public AmbulanceDeactivation(Date beginDate, Ambulance ambulance) {
		this.beginDate = beginDate;
		this.ambulance = ambulance;
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

	/** Getter for endDate. */
	public Date getEndDate() {
		return endDate;
	}

	/** Setter for end. */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/** Getter for reason. */
	public String getReason() {
		return reason;
	}

	/** Setter for reason. */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
