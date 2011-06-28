package it.unitn.disi.ads.core.domain;

import br.com.engenhodesoftware.util.ejb3.persistence.PersistentObjectSupport;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Domain class that represents an ambulance, the core element of the Ambulance Dispatch System.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class Ambulance extends PersistentObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Ambulance identification number. */
	@NotNull
	private Integer number;

	/** License plate number. */
	@NotNull
	@Size(max = 15)
	private String licensePlate;

	/** Empty constructor. */
	public Ambulance() {
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param number
	 *		Ambulance id number.
	 * @param licensePlate
	 *		Ambulance's license plate number.
	 */
	public Ambulance(Integer number, String licensePlate) {
		this.number = number;
		this.licensePlate = licensePlate;
	}

	/** Getter for licensePlate. */
	public String getLicensePlate() {
		return licensePlate;
	}

	/** Setter for licensePlate. */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/** Getter for number. */
	public Integer getNumber() {
		return number;
	}

	/** Setter for number. */
	public void setNumber(Integer number) {
		this.number = number;
	}
}
