package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseJPADAO;
import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.DriverAssignment;
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
 * Data access object (DAO) implementation for the domain class DriverAssignment.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Stateless
public class DriverAssignmentJPADAO extends BaseJPADAO<DriverAssignment> implements DriverAssignmentDAO {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(DriverAssignmentJPADAO.class.getCanonicalName());

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
	protected Class<DriverAssignment> getDomainClass() {
		return DriverAssignment.class;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getOrderList(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root) */
	@Override
	protected List<Order> getOrderList(CriteriaBuilder cb, Root<DriverAssignment> root) {
		// Orders by the date the assignment began.
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root.get(DriverAssignmentJPAMetamodel.beginDate)));
		return orderList;
	}

	/** @see it.unitn.disi.ads.core.persistence.DriverAssignmentDAO#retrieveByAmbulance(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public List<DriverAssignment> retrieveByAmbulance(Ambulance ambulance) {
		logger.log(Level.INFO, "Retrieving DriverAssignment by ambulance: ambulance # {0} (id {1})", new Object[] {ambulance.getNumber(), ambulance.getId()});

		// Constructs the query over a DriverAssignment object.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DriverAssignment> cq = cb.createQuery(DriverAssignment.class);
		Root<DriverAssignment> root = cq.from(DriverAssignment.class);

		// Filters the query with the ambulance.
		cq.where(cb.equal(root.get(DriverAssignmentJPAMetamodel.ambulance), ambulance));

		// Applies ordering.
		applyOrdering(cb, root, cq);

		// Returns the list of assignments.
		return em.createQuery(cq).getResultList();
	}

	/** @see it.unitn.disi.ads.core.persistence.DriverAssignmentDAO#retrieveActiveByAmbulance(it.unitn.disi.ads.core.domain.Ambulance) */
	@Override
	public DriverAssignment retrieveActiveByAmbulance(Ambulance ambulance) {
		logger.log(Level.INFO, "Retrieving active DriverAssignment by ambulance: ambulance # {0} (id {1})", new Object[] {ambulance.getNumber(), ambulance.getId()});

		// FIXME: check the current status on this bug and go back to Criteria API.
		// Also add  throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException
		
		/* The commented code is NOT WORKING PROPERLY. Although both equal() and isNull() criteria methods are provided, it ignores isNull().

		// Constructs the query over an DriverAssignment object.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DriverAssignment> cq = cb.createQuery(DriverAssignment.class);
		Root<DriverAssignment> root = cq.from(DriverAssignment.class);

		// Filters the query with the ambulance and the end date is not supposed to be null.
		cq.where(cb.equal(root.get(DriverAssignmentJPAMetamodel.ambulance), ambulance),
				cb.isNull(root.get(DriverAssignmentJPAMetamodel.endDate)));

		// Looks for a single result and throws exceptions in case of problems.
		DriverAssignment assignment = null;
		try {
			assignment = em.createQuery(cq).getSingleResult();
		}
		catch (NoResultException e) {
			// It's normal for no assignment to exist for a given ambulance.
			return null;
		}
		catch (RuntimeException e) {
			// Any other exception should be thrown to the application layer.
			throw new DriverAssignmentDAOException(e);
		}

		return assignment;
		 */

		DriverAssignment assignment = null;
		Query q = em.createQuery("select a from DriverAssignment a where a.endDate is null and a.ambulance.id = " + ambulance.getId());
		try {
			assignment = (DriverAssignment)q.getSingleResult();
		}
		catch (NoResultException e) {
			// It's normal for no assignment to exist for a given ambulance.
			return null;
		}

		return assignment;
	}
}
