package it.unitn.disi.ads.core.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseJPADAO;
import br.com.engenhodesoftware.util.people.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.com.engenhodesoftware.util.people.persistence.exceptions.PersistentObjectNotFoundException;
import it.unitn.disi.ads.core.domain.Employee;
import it.unitn.disi.ads.core.domain.EmployeeType;
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
 * Data access object (DAO) implementation for the domain class Employee.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@Stateless
public class EmployeeJPADAO extends BaseJPADAO<Employee> implements EmployeeDAO {
	/** Serialization id. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger logger = Logger.getLogger(EmployeeJPADAO.class.getCanonicalName());

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
	protected Class<Employee> getDomainClass() {
		return Employee.class;
	}

	/** @see br.com.engenhodesoftware.util.ejb3.persistence.BaseJpaDAO#getOrderList(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root) */
	@Override
	protected List<Order> getOrderList(CriteriaBuilder cb, Root<Employee> root) {
		// Orders by name.
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root.get(EmployeeJPAMetamodel.name)));
		return orderList;
	}

	/** @see it.unitn.disi.ads.core.persistence.EmployeeDAO#retrieveByUsername(java.lang.String) */
	@Override
	public Employee retrieveByUsername(String username) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
		logger.log(Level.INFO, "Retrieving Employee by username: {0}", username);
		
		// Constructs the query over the Employee class.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);

		// Filters the query with the username.
		cq.where(cb.equal(root.get(EmployeeJPAMetamodel.login), username));

		// Looks for a single result.
		return executeSingleResultQuery(cq, username);
	}

	/** @see it.unitn.disi.ads.core.persistence.EmployeeDAO#retrieveDrivers() */
	@Override
	public List<Employee> retrieveDrivers() {
		logger.log(Level.INFO, "Retrieving Employees who are of type DRIVER.");

		// Constructs the query over the Employee class.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);

		// Filters the query with the type: DRIVER.
		cq.where(cb.equal(root.get(EmployeeJPAMetamodel.type), EmployeeType.DRIVER));

		// Returns the list of drivers.
		return em.createQuery(cq).getResultList();
	}
}
