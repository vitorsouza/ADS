package it.unitn.disi.ads.core.controller;

import br.com.engenhodesoftware.util.ejb3.controller.JSFAction;
import it.unitn.disi.ads.core.application.ReceiveCallService;
import it.unitn.disi.ads.core.application.SessionInformation;
import it.unitn.disi.ads.core.domain.EmergencyCall;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Named
@ConversationScoped
public class ReceiveCallAction extends JSFAction {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ReceiveCallAction.class.getCanonicalName());

	/** Represents the page that shows the form for the operator to input the emergency information. */
	private static final String FORM = "/core/inputInfo/form.xhtml";

	/** The conversation. */
	@Inject
	private Conversation conversation;

	/** The "Receive Call" service. */
	@EJB
	private ReceiveCallService receiveCallService;

	/** Session information. */
	@Inject
	private SessionInformation sessionInformation;

	/** The emergency call being manipulated. */
	private EmergencyCall call = new EmergencyCall();

	/** Getter for call. */
	public EmergencyCall getCall() {
		return call;
	}

	/**
	 * Begins the use case showing the form page, which the operator can use to input the emergency information.
	 * 
	 * @return The path to the form page, which is the first page to be displayed.
	 */
	public String begin() {
		// If the conversation is still transient, begin a long-running one.
		if (conversation.isTransient()) {
			conversation.begin();
			logger.log(Level.FINE, "Beginning conversation with id: {0}", conversation.getId());
		}

		// Shows the form.
		return FORM;
	}

	/**
	 * TODO: documentation.
	 */
	public void inputInfo() {
		// Sends the call to the service bean for saving.
		logger.log(Level.INFO, "Inputting emergency information...");
		receiveCallService.inputInfo(call, sessionInformation.getCurrentUser());

		// Displays an informational message.
		this.addGlobalI18nMessage("msgs", FacesMessage.SEVERITY_INFO, "receiveCall.text.inputInfoSuccess");
	}
}
