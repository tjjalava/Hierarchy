package fi.bagroup.hierarchy.dao

import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import fi.bagroup.hierarchy.model.Entity
import grizzled.slf4j.Logging

/**
 * @author tjjalava
 * @since 3.9.2013 
 */
class EntityDao extends Logging {

  implicit val getEntityResult = GetResult(r => Entity(r.<<, r.<<))

  def addEntity(entity:Entity) {
    val (id, name) = (entity.id, entity.name)
    val upcount = sqlu"insert into entity (id, name) values ($id, $name)".first()
    debug("Inserted " + upcount + " rows")
  }

  def getEntity(id:Long) = {
    debug("Fetching entity with id " + id)
    sql"select id, name from entity where id = $id".as[Entity].firstOption() match {
      case Some(e) => e
      case None => throw new Exception("Not found")
    }
  }

}
