package fi.bagroup.hierarchy.test

import fi.bagroup.hierarchy.dao.{Dao, EntityDao}
import scala.slick.session.Database
import scala.slick.jdbc.{StaticQuery => Q}
import org.specs2.mutable.Specification
import fi.bagroup.hierarchy.model.Entity
import Dao.{session => session}
import org.specs2.specification.{Step, Fragments}

/**
 * @author tjjalava
 * @since 3.9.2013 
 */
object databaseSetup {
  lazy val createdb = {
    session = Database.forURL("jdbc:h2:mem:hierarchy", driver = "org.h2.Driver").createSession()
    Q.updateNA("create table entity ( id int primary key, name varchar not null)").execute()
  }
  lazy val shutdown = {
    session.close()
  }
}

trait DaoSpec extends Specification {
  override def map(fs: => Fragments) = Step(databaseSetup.createdb) ^ fs ^ Step(databaseSetup.shutdown)
}

class EntityDaoSpec extends DaoSpec {
  sequential

  val entityDao = new EntityDao

  "'EntityDaoSpec'" should {
    "create a new Entity-instance in database" in {
      entityDao.addEntity(Entity(1, "Entity1"))
      success
    }
    "fetch the same instance from database" >> {
      val entity = entityDao.getEntity(1)
      entity.id  === 1
      entity.name === "Entity1"
    }
  }
}
