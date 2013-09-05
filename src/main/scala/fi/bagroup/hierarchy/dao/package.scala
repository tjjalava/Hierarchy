package fi.bagroup.hierarchy

import scala.slick.session.Session

/**
 * @author tjjalava
 * @since 5.9.2013 
 */
package object dao {
  implicit var session: Session = _
}
