package it.unitn.disi.ads.core.domain;

import br.com.engenhodesoftware.util.ejb3.persistence.PersistentObjectSupport;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Domain class that represents an emergency call. Operators respond to calls and register them in the system, also indicating if they are
 * duplicates of calls that have already been received before.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Entity
public class EmergencyCall extends PersistentObjectSupport implements Serializable, Comparable<EmergencyCall> {
	private static final long serialVersionUID = 1L;

	/** Date and time of the call. */
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date timeOfCall;

	/** Description of the emergency. */
	@Lob
	private String description;

	/** Address where the emergency takes place. */
	@Size(max = 250)
	private String address;

	/** Information on the caller (name, id, etc.). */
	@Size(max = 250)
	private String callerInfo;

	/** Priority level of the call. */
	@NotNull
	private EmergencyCallPriority priority = EmergencyCallPriority.MEDIUM;

	/** Indicates if the call is complete and can be forwarded to the dispatchers. */
	@NotNull
	private Boolean completed = false;

	/** The operator that took the call. */
	@ManyToOne
	@NotNull
	private Employee operator;

	/** The dispatcher who sent the ambulances for this call. */
	@ManyToOne
	private Employee dispatcher;

	/** Ambulances dispatched for this call. */
	@OneToMany(mappedBy = "emergencyCall", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Dispatch> dispatches;

	/** Emergency call of which this call is a duplicate. */
	@ManyToOne
	private EmergencyCall duplicateOf;

	/** Duplicate emergency calls. */
	@OneToMany(mappedBy = "duplicateOf", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<EmergencyCall> duplicates;

	/** Empty constructor. */
	public EmergencyCall() {
	}

	/**
	 * Domain constructor: includes all mandatory fields, uses default values for priority.
	 *
	 * @param timeOfCall
	 *		Time of call.
	 * @param operator
	 *		Operator who took the call.
	 */
	public EmergencyCall(Date timeOfCall, Employee operator) {
		this.timeOfCall = timeOfCall;
		this.operator = operator;
	}

	/**
	 * Domain constructor: includes all mandatory fields.
	 *
	 * @param timeOfCall
	 *		Time of call.
	 * @param operator
	 *		Operator who took the call.
	 * @param priority
	 *		Priority of the call.
	 */
	public EmergencyCall(Date timeOfCall, Employee operator, EmergencyCallPriority priority) {
		this(timeOfCall, operator);
		this.priority = priority;
	}

	/** Getter for address. */
	public String getAddress() {
		return address;
	}

	/** Setter for address. */
	public void setAddress(String address) {
		this.address = address;
	}

	/** Getter for callerInfo. */
	public String getCallerInfo() {
		return callerInfo;
	}

	/** Setter for callerInfo. */
	public void setCallerInfo(String callerInfo) {
		this.callerInfo = callerInfo;
	}

	/** Getter for completed. */
	public Boolean getCompleted() {
		return completed;
	}

	/** Setter for completed. */
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	/** Getter for description. */
	public String getDescription() {
		return description;
	}

	/** Setter for description. */
	public void setDescription(String description) {
		this.description = description;
	}

	/** Getter for priority. */
	public EmergencyCallPriority getPriority() {
		return priority;
	}

	/** Setter for priority. */
	public void setPriority(EmergencyCallPriority priority) {
		this.priority = priority;
	}

	/** Getter for timeOfCall. */
	public Date getTimeOfCall() {
		return timeOfCall;
	}

	/** Setter for timeOfCall. */
	public void setTimeOfCall(Date timeOfCall) {
		this.timeOfCall = timeOfCall;
	}

	/** Getter for dispatcher. */
	public Employee getDispatcher() {
		return dispatcher;
	}

	/** Setter for dispatcher. */
	public void setDispatcher(Employee dispatcher) {
		this.dispatcher = dispatcher;
	}

	/** Getter for dispatches. */
	public Set<Dispatch> getDispatches() {
		return dispatches;
	}

	/** Setter for dispatches. */
	public void setDispatches(Set<Dispatch> dispatches) {
		this.dispatches = dispatches;
	}

	/** Getter for duplicateOf. */
	public EmergencyCall getDuplicateOf() {
		return duplicateOf;
	}

	/** Setter for duplicateOf. */
	public void setDuplicateOf(EmergencyCall duplicateOf) {
		this.duplicateOf = duplicateOf;
	}

	/** Getter for duplicates. */
	public Set<EmergencyCall> getDuplicates() {
		return duplicates;
	}

	/** Setter for duplicates. */
	public void setDuplicates(Set<EmergencyCall> duplicates) {
		this.duplicates = duplicates;
	}

	/** Getter for operator. */
	public Employee getOperator() {
		return operator;
	}

	/** Setter for operator. */
	public void setOperator(Employee operator) {
		this.operator = operator;
	}

	/** @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(EmergencyCall o) {
		// Compare by date. If it's the same date, solve the comparison at the domain object level (UUID).
		int cmp = timeOfCall.compareTo(timeOfCall);
		return (cmp == 0) ? super.compareTo(o) : cmp;
	}
}
