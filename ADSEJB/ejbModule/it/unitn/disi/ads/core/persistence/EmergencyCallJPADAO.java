package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseJPADAO;
import it.unitn.disi.ads.core.domain.EmergencyCall;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * Data access object (DAO) implementation for the domain class EmergencyCall.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Stateless
public class EmergencyCallJPADAO extends BaseJPADAO<EmergencyCall> implements EmergencyCallDAO {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(EmergencyCallJPADAO.class.getCanonicalName());

	/** The entity manager provided by the container. */
	@PersistenceContext
	private EntityManager em;

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getEntityManager() */
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getDomainClass() */
	@Override
	protected Class<EmergencyCall> getDomainClass() {
		return EmergencyCall.class;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getOrderList(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root) */
	@Override
	protected List<Order> getOrderList(CriteriaBuilder cb, Root<EmergencyCall> root) {
		// Orders by the time of the call.
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root.get(EmergencyCallJPAMetamodel.timeOfCall)));
		return orderList;
	}
}
