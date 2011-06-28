package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseJPADAO;
import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.AmbulanceDeactivation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * Data access object (DAO) implementation for the domain class AmbulanceDeactivation.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Stateless
public class AmbulanceDeactivationJPADAO extends BaseJPADAO<AmbulanceDeactivation> implements AmbulanceDeactivationDAO {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(AmbulanceDeactivationJPADAO.class.getCanonicalName());

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
	protected Class<AmbulanceDeactivation> getDomainClass() {
		return AmbulanceDeactivation.class;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getOrderList(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root) */
	@Override
	protected List<Order> getOrderList(CriteriaBuilder cb, Root<AmbulanceDeactivation> root) {
		// Orders by the date the deactivation began.
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root.get(AmbulanceDeactivationJPAMetamodel.beginDate)));
		return orderList;
	}

	/** @see it.unitn.disi.ads.core.persistence.AmbulanceDeactivationDAO#retrieveByAmbulance(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public List<AmbulanceDeactivation> retrieveByAmbulance(Ambulance ambulance) {
		logger.log(Level.INFO, "Retrieving AmbulanceDeactivation by ambulance: ambulance # {0} (id {1})", new Object[] {ambulance.getNumber(), ambulance.getId()});

		// Constructs the query over an AmbulanceDeactivation object.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AmbulanceDeactivation> cq = cb.createQuery(AmbulanceDeactivation.class);
		Root<AmbulanceDeactivation> root = cq.from(AmbulanceDeactivation.class);

		// Filters the query with the ambulance.
		cq.where(cb.equal(root.get(AmbulanceDeactivationJPAMetamodel.ambulance), ambulance));

		// Applies ordering.
		applyOrdering(cb, root, cq);

		// Returns the list of deactivations.
		return em.createQuery(cq).getResultList();
	}

	/** @see it.unitn.disi.ads.core.persistence.AmbulanceDeactivationDAO#retrieveActiveByAmbulance(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public AmbulanceDeactivation retrieveActiveByAmbulance(Ambulance ambulance) {
		logger.log(Level.INFO, "Retrieving active AmbulanceDeactivation by ambulance: ambulance # {0} (id {1})", new Object[] {ambulance.getNumber(), ambulance.getId()});

		// FIXME: check the current status on this bug and go back to Criteria API.
		// Also add  throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException

		/* The commented code is NOT WORKING PROPERLY. Althoughboth equal() and isNull() criteria methods are provided, it ignores isNull().

		// Constructs the query over an AmbulanceDeactivation object.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AmbulanceDeactivation> cq = cb.createQuery(AmbulanceDeactivation.class);
		Root<AmbulanceDeactivation> root = cq.from(AmbulanceDeactivation.class);

		// Filters the query with the ambulance and the end date is not supposed to be null.
		cq.where(cb.equal(root.get(AmbulanceDeactivationJPAMetamodel.ambulance), ambulance),
				cb.isNull(root.get(AmbulanceDeactivationJPAMetamodel.endDate)));

		// Looks for a single result and throws exceptions in case of problems.
		AmbulanceDeactivation deactivation = null;
		try {
			deactivation = em.createQuery(cq).getSingleResult();
		}
		catch (NoResultException e) {
			// It's normal for no deactivation to exist for a given ambulance.
			return null;
		}
		catch (RuntimeException e) {
			// Any other exception should be thrown to the application layer.
			throw new AmbulanceDeactivationDAOException(e);
		}

		return deactivation;
		 */

		AmbulanceDeactivation deactivation = null;
		Query q = em.createQuery("select d from AmbulanceDeactivation d where d.endDate is null and d.ambulance.id = " + ambulance.getId());
		try {
			deactivation = (AmbulanceDeactivation)q.getSingleResult();
		}
		catch (NoResultException e) {
			// It's normal for no deactivation to exist for a given ambulance.
			return null;
		}

		return deactivation;
	}
}
