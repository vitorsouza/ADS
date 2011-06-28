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
 * Domain class that represents the location of an ambulance using latitude and longitude coordinates.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class AmbulanceLocation extends PersistentObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Latitude. */
	@NotNull
	private Long latitude;

	/** Longitude. */
	@NotNull
	private Long longitude;

	/** Update time on the location. */
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date updateTime;

	/** Ambulance this location refers to. */
	@ManyToOne
	@NotNull
	private Ambulance ambulance;

	/** Empty constructor. */
	public AmbulanceLocation() {
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param latitude
	 *		Latitude.
	 * @param longitude
	 *		Longitude.
	 * @param updateTime
	 *		Time of the update on the location.
	 * @param ambulance
	 *		Ambulance this location refers to.
	 */
	public AmbulanceLocation(Long latitude, Long longitude, Date updateTime, Ambulance ambulance) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.updateTime = updateTime;
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

	/** Getter for latitude. */
	public Long getLatitude() {
		return latitude;
	}

	/** Setter for latitude. */
	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	/** Getter for longitude. */
	public Long getLongitude() {
		return longitude;
	}

	/** Setter for longitude. */
	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	/** Getter for updateTime. */
	public Date getUpdateTime() {
		return updateTime;
	}

	/** Setter for updateTime. */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
