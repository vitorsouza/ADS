package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseJPADAO;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.domain.Ambulance;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * Data access object (DAO) implementation for the domain class Ambulance.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Stateless
public class AmbulanceJPADAO extends BaseJPADAO<Ambulance> implements AmbulanceDAO {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(AmbulanceJPADAO.class.getCanonicalName());

	@PersistenceContext
	private EntityManager em;

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getEntityManager() */
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getDomainClass() */
	@Override
	protected Class<Ambulance> getDomainClass() {
		return Ambulance.class;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getOrderList(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root) */
	@Override
	protected List<Order> getOrderList(CriteriaBuilder cb, Root<Ambulance> root) {
		// Orders by number.
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root.get(AmbulanceJPAMetamodel.number)));
		return orderList;
	}

	/** @see it.unitn.disi.ads.core.persistence.AmbulanceDAO#retrieveByNumber(java.lang.Integer) */
	@Override
	public Ambulance retrieveByNumber(Integer number) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
		logger.log(Level.INFO, "Retrieving Ambulance by number: {0}", number);

		// Constructs the query over an Ambulance object.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Ambulance> cq = cb.createQuery(Ambulance.class);
		Root<Ambulance> root = cq.from(Ambulance.class);

		// Filters the query with the number.
		cq.where(cb.equal(root.get(AmbulanceJPAMetamodel.number), number));

		// Looks for a single result.
		return executeSingleResultQuery(cq, number);
	}

	/** @see it.unitn.disi.ads.core.persistence.AmbulanceDAO#retrieveByLicensePlate(java.lang.String) */
	@Override
	public Ambulance retrieveByLicensePlate(String licensePlate) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
		logger.log(Level.INFO, "Retrieving Ambulance by license plate: {0}", licensePlate);

		// Constructs the query over an Ambulance object.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Ambulance> cq = cb.createQuery(Ambulance.class);
		Root<Ambulance> root = cq.from(Ambulance.class);

		// Filters the query with the username.
		cq.where(cb.equal(root.get(AmbulanceJPAMetamodel.licensePlate), licensePlate));

		// Looks for a single result.
		return executeSingleResultQuery(cq, licensePlate);
	}
}
