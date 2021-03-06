package br.com.engenhodesoftware.util.ejb3.persistence;

import br.com.engenhodesoftware.util.ejb3.application.filters.Filter;
import java.io.Serializable;
import java.util.List;

/**
 * Base interface for all DAO classes in the system. Instances manipulated by DAOs that implement this interface must
 * belong to a subclass of PersistentObject.
 * 
 * A base DAO implementation is also provided, integrating with the Seam framework: SeamBaseDAO.
 * 
 * For more information on the DAO pattern, visit: <a href=
 * "http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html"
 * target="_blank">http://java.sun.com/blueprints/corej2eepatterns/Patterns/ DataAccessObject.html</a>.
 * 
 * <i>This class is part of the Engenho de Software CRUD framework for EJB3 (Java EE 6).</i>
 * 
 * @see br.com.engenhodesoftware.util.ejb3.persistence.JpaBaseDAO
 * @see br.com.engenhodesoftware.util.ejb3.persistence.PersistentObject
 * @author Vitor Souza (vitorsouza@gmail.com)
 * @version 1.1
 */
public interface BaseDAO<T extends PersistentObject> extends Serializable {
	/**
	 * Returns the number of persistent objects of this class.
	 * 
	 * @return Number of existing persistent objects of this class.
	 */
	long retrieveCount();

	/**
	 * Returns the number of persistent objects of this class that match the specified filter.
	 * @param filter
	 *		The specification that filters the objects from the persistent class.
	 * @param value
	 *		The filter's input.
	 *
	 * @return Number of existing persistent objects of this class that match the given filter.
	 */
	long retrieveFilteredCount(Filter<?> filter, String value);

	/**
	 * Retrieves all objects from the persistent class.
	 * 
	 * @return List with all objects from the persistent domain class.
	 */
	List<T> retrieveAll();

	/**
	 * Retrieve all objects from the persistent class that match the specified filter.
	 *
	 * @param filter
	 *		The specification that filters the objects from the persistent class.
	 * @param value
	 *		The filter's input.
	 *
	 * @return List with all objects from the persistent domain class.
	 */
	List<T> retrieveWithFilter(Filter<?> filter, String value);

	/**
	 * Retrieves some objects from the persistent class.
	 * 
	 * @param interval
	 *		Array of size 2 with the interval [a, b) (retrieves objects from index a through b-1).
	 *
	 * @return List with some objects (according to the given interval) from the persistent domain class.
	 */
	List<T> retrieveSome(int[] interval);

	/**
	 * Retrieve some objects from the persistent class that match the specified filter.
	 *
	 * @param filter
	 *		The specification that filters the objects from the persistent class.
	 * @param value
	 *		The filter's input.
	 * @param interval
	 *		Array of size 2 with the interval [a, b) (retrieves objects from index a through b-1).
	 *
	 * @return List with some objects (according to the given interval) from the persistent domain class.
	 */
	List<T> retrieveSomeWithFilter(Filter<?> filter, String value, int[] interval);

	/**
	 * Obtains a persistent object given its id.
	 * 
	 * @param id
	 *		The persistent object's id.
	 * 
	 * @return The persistent object that has the given id.
	 */
	T retrieveById(Long id);

	/**
	 * Stores an object in the persistent media.
	 * 
	 * @param object
	 *		The object to store.
	 */
	void save(T object);

	/**
	 * Removes an object from the persistent media.
	 * 
	 * @param object
	 *		The object to delete.
	 */
	void delete(T object);
}
