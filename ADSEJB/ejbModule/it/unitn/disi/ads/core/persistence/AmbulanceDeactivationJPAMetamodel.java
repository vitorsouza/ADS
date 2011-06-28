package it.unitn.disi.ads.core.persistence;

import it.unitn.disi.ads.core.domain.Ambulance;
import it.unitn.disi.ads.core.domain.AmbulanceDeactivation;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * TODO: documentation pending.
 *
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 */
@StaticMetamodel(AmbulanceDeactivation.class)
public class AmbulanceDeactivationJPAMetamodel {
	public static volatile SingularAttribute<AmbulanceDeactivation, Date> beginDate;
	public static volatile SingularAttribute<AmbulanceDeactivation, Date> endDate;
	public static volatile SingularAttribute<AmbulanceDeactivation, String> reason;
	public static volatile SingularAttribute<AmbulanceDeactivation, Ambulance> ambulance;
}
