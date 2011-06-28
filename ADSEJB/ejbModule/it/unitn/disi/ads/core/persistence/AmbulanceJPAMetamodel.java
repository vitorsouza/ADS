package it.unitn.disi.ads.core.persistence;

import it.unitn.disi.ads.core.domain.Ambulance;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@StaticMetamodel(Ambulance.class)
public class AmbulanceJPAMetamodel {
	public static volatile SingularAttribute<Ambulance, Integer> number;
	public static volatile SingularAttribute<Ambulance, String> licensePlate;
}
