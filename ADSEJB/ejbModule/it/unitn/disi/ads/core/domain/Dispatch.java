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
 * Domain class that represents the dispatching of an ambulance to the location of an emergency. A dispatch is registered in the system
 * when the dispatcher selects the ambulance that is more suitable to respond an emergency.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class Dispatch extends PersistentObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Time of the dispatch. */
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date dispatchTime;

	/** Time of arrival at the site. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date siteTime;

	/** Time of arrival at the hospital. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date hospitalTime;

	/** Emergency call. */
	@ManyToOne
	@NotNull
	private EmergencyCall emergencyCall;

	/** Ambulance dispatched. */
	@ManyToOne
	@NotNull
	private Ambulance ambulance;

	/** Empty constructor. */
	public Dispatch() {
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param dispatchTime
	 *		Time the ambulance was dispatched.
	 * @param emergencyCall
	 *		Emergency call that is being serviced.
	 * @param ambulance
	 *		Ambulance that was dispatched.
	 */
	public Dispatch(Date dispatchTime, EmergencyCall emergencyCall, Ambulance ambulance) {
		this.dispatchTime = dispatchTime;
		this.emergencyCall = emergencyCall;
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

	/** Getter for emergencyCall. */
	public EmergencyCall getEmergencyCall() {
		return emergencyCall;
	}

	/** Setter for emergencyCall. */
	public void setEmergencyCall(EmergencyCall emergencyCall) {
		this.emergencyCall = emergencyCall;
	}

	/** Getter for dispatchTime. */
	public Date getDispatchTime() {
		return dispatchTime;
	}

	/** Setter for dispatchTime. */
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	/** Getter for hospitalTime. */
	public Date getHospitalTime() {
		return hospitalTime;
	}

	/** Setter for hospitalTime. */
	public void setHospitalTime(Date hospitalTime) {
		this.hospitalTime = hospitalTime;
	}

	/** Getter for siteTime. */
	public Date getSiteTime() {
		return siteTime;
	}

	/** Setter for siteTime. */
	public void setSiteTime(Date siteTime) {
		this.siteTime = siteTime;
	}
}
