package br.com.engenhodesoftware.util.people.persistence;

import br.com.engenhodesoftware.util.ejb3.persistence.BaseDAO;
import br.com.engenhodesoftware.util.people.domain.Telephone;
import javax.ejb.Local;

/**
 * TODO: document this type.
 *
 * @author Vitor Souza (vitorsouza@gmail.com)
 */
@Local
public interface TelephoneDAO extends BaseDAO<Telephone> {
}
