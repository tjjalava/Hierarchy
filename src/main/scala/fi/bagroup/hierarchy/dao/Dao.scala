package fi.bagroup.hierarchy.dao

import scala.slick.session.Session

/**
 * @author tjjalava
 * @since 4.9.2013 
 */
object Dao {

  implicit var session: Session = _

}
