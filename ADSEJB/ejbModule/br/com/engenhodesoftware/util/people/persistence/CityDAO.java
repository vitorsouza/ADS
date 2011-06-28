package br.com.engenhodesoftware.util.people.persistence;

import java.util.List;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import br.com.engenhodesoftware.util.people.domain.City;
import br.com.engenhodesoftware.util.people.persistence.exceptions.CityNotFoundException;
import javax.ejb.Local;

/**
 * TODO: document this type.
 *
 * @author Vitor Souza (vitorsouza@gmail.com)
 */
@Local
public interface CityDAO extends BaseDAO<City> {
	/**
	 * TODO: document this method.
	 * @param name
	 * @return
	 */
	List<City> findByName(String name);
	
	/**
	 * TODO: document this method.
	 * @param cityName
	 * @param stateAcronym
	 * @return
	 */
	City retrieveByNameAndStateAcronym(String cityName, String stateAcronym) throws CityNotFoundException;
}
