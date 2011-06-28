package it.unitn.disi.ads.core.application;

import it.unitn.disi.ads.core.domain.EmergencyCall;
import it.unitn.disi.ads.core.domain.Employee;
import java.io.Serializable;
import javax.ejb.Local;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface ReceiveCallService extends Serializable {
	/**
	 * TODO: documentation.
	 * 
	 * @param call
	 * @param operator
	 */
	void inputInfo(EmergencyCall call, Employee operator);
}
