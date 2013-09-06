package fi.bagroup.hierarchy.test

import org.specs2.mutable.Specification
import org.specs2.specification.{Step, Fragments}
import fi.bagroup.hierarchy.dao._
import scala.slick.session.Database
import scala.slick.jdbc.{StaticQuery => Q}

/**
 * @author tjjalava
 * @since 6.9.2013 
 */
trait DaoSpec extends Specification {
  def startup() : Unit
  def shutdown() : Unit

  override def map(fs: => Fragments) = Step(startup()) ^ fs ^ Step(shutdown())
}
