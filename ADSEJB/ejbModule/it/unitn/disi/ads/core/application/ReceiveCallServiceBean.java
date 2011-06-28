package it.unitn.disi.ads.core.application;

import it.unitn.disi.ads.core.domain.EmergencyCall;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.persistence.EmergencyCallDAO;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Stateless
public class ReceiveCallServiceBean implements ReceiveCallService {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ReceiveCallServiceBean.class.getCanonicalName());

	/** The DAO for EmergencyCall objects. */
	@EJB
	private EmergencyCallDAO emergencyCallDAO;

	/** @see it.unitn.disi.ads.core.application.ReceiveCallService#inputInfo(it.unitn.disi.ads.core.domain.EmergencyCall, it.unitn.disi.ads.core.domain.Employee) */
	@Override
	public void inputInfo(EmergencyCall call, Employee operator) {
		// Sets the time and operator that received the call.
		call.setTimeOfCall(new Date(System.currentTimeMillis()));
		call.setOperator(operator);

		// Saves the emergency information.
		logger.log(Level.INFO, "Saving emergency: address: \"{0}\"; caller: \"{1}\"; time: {2}, operator: {3}", new Object[] {call.getAddress(), call.getCallerInfo(), call.getTimeOfCall(), call.getOperator().getName()});
		emergencyCallDAO.save(call);
	}
}
