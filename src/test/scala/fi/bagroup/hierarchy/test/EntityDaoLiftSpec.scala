package fi.bagroup.hierarchy.test

import fi.bagroup.hierarchy.dao._
import fi.bagroup.hierarchy.dao.EntityDaoLift
import fi.bagroup.hierarchy.dao.EntityDaoLift.asHierarchy
import fi.bagroup.hierarchy.model.Entity
import scala.slick.driver.H2Driver
import scala.slick.session.Database
import scala.language.implicitConversions

/**
 * @author tjjalava
 * @since 3.9.2013 
 */
class EntityDaoLiftSpec extends DaoSpec {
  sequential

  val entityDao = new EntityDaoLift(H2Driver)
  var newEntity:Entity = _

  "'EntityDaoLiftSpec'" should {
    "create a new Entity-instance in database" in {
      newEntity = entityDao.addEntity(Entity(name = "Entity1"))
      success
    }
    "create a child entity" in {
      entityDao.addEntity(Entity(name = "Entity2"), newEntity.id)
      success
    }
    "fetch the same instance from database" >> {
      val entity = entityDao.getEntity(newEntity.id)
      entity === newEntity
    }
    "fetch hierarchy as list" in {
      for (entity <- entityDao.getEntityHierarchy(newEntity.id)) {
        println(entity)
      }
      ok
    }
    "fetch hierarchy as hierarchy" in {
      def p(e:Entity) {
        val indent = (for (i <- 0 until e.depth) yield "  ").foldLeft("")(_ + _)
        println(indent + e.name)
        e.getChildren.foreach(p)
      }

      entityDao.getEntityHierarchy(newEntity.id).asHierarchy.foreach(p)
      ok
    }
  }

  def startup() {
    val database = Database.forURL("jdbc:h2:mem:hierarchy", driver = "org.h2.Driver")
    session = database.createSession()
    entityDao.create
  }

  def shutdown() {
    session.close()
  }
}
