package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import it.unitn.disi.ads.core.domain.EmergencyCall;
import javax.ejb.Local;

/**
 * Data access object (DAO) interface for the domain class EmergencyCall.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Local
public interface EmergencyCallDAO extends BaseDAO<EmergencyCall> {
}
