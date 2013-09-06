package fi.bagroup.hierarchy.dao

import scala.slick.driver.ExtendedProfile
import fi.bagroup.hierarchy.model.Entity
import scala.slick.lifted.ForeignKeyAction._
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation

/**
 * @author tjjalava
 * @since 6.9.2013 
 */
class EntityDaoLift(val driver: ExtendedProfile) {
  import driver.simple._

  object Entities extends Table[Entity]("ENTITY") {
    def _id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")
    def * = _id.? ~ name ~ description <> (Entity, Entity.unapply _)
    def autoInc = * returning _id
  }

  object EntityHierarchy extends Table[(Long, Long, Int)]("ENTITY_HIERARCHY") {
    def ancestor = column[Long]("ANCESTOR")
    def descendant = column[Long]("DESCENDANT")
    def depth = column[Int]("DEPTH")
    def pk = primaryKey("pk", (ancestor, descendant))
    def fkAncestor = foreignKey("fk_an", ancestor, Entities)(_._id, Restrict, Restrict)
    def fkDecendant = foreignKey("fk_de", descendant, Entities)(_._id, Restrict, Restrict)
    def * = ancestor ~ descendant ~ depth
  }

  private val ddl = Entities.ddl ++ EntityHierarchy.ddl

  def create(implicit session: Session) = ddl.create

  def addEntity(entity: Entity, parentId:Long = -1) = {
    val newId = Entities.autoInc insert entity
    EntityHierarchy.insert((newId, newId, 0))

    if (parentId > 0) {
      (Q.u +
        "insert into ENTITY_HIERARCHY " +
          "select p.ANCESTOR, c.DESCENDANT, p.DEPTH+c.DEPTH+1 " +
          "from ENTITY_HIERARCHY p, ENTITY_HIERARCHY c " +
          "where p.DESCENDANT = " +? parentId + " and c.ANCESTOR = " +? newId
        ).execute()
    }

    Entity(Some(newId), entity.name, entity.description)
  }

  def getEntity(id:Long) = {
    Query(Entities).filter(_._id === id).firstOption() match {
      case Some(e) => e
      case None => throw new Exception("Not found")
    }
  }

}