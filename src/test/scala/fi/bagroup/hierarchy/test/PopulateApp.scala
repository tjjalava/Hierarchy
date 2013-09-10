package fi.bagroup.hierarchy.test

import scala.slick.session.Database
import fi.bagroup.hierarchy.dao.{EntityDaoLift, session}
import EntityDaoLift.asHierarchy
import scala.language.implicitConversions
import scala.slick.driver.PostgresDriver
import fi.bagroup.hierarchy.model.Entity
import scala.util.Random

/**
 * @author tjjalava
 * @since 9.9.2013 
 */
object PopulateApp {

  def main(args:Array[String]) {

    //Database.forURL("jdbc:h2:mem:hierarchy", driver = "org.h2.Driver") withSession {
    Database.forURL(url = "jdbc:postgresql://localhost/hierarchy", user = "hierarchy", password = "hierarchy", driver = "org.postgresql.Driver") withSession {
      session = Database.threadLocalSession
      val entityDao = new EntityDaoLift(MyPostgresDriver)
      var rootId = 1l

      if (args.length > 0 && args(0) == "populate") {

        try {
          entityDao.drop
        } catch {
          case _:Exception =>
        }

        entityDao.create

        val maxdepth = 15

        def populate(e:Entity, depth:Int) {
          val childCount = Random.nextInt(8) + 1
          //println("Populate, depth: " + depth + ", adding " + childCount + " children")
          for (i <- 0 until childCount) {
            val child = entityDao.addEntity(Entity(name = e.name + "." + i), e.id)
            if (depth <= maxdepth && Random.nextInt(4) > 0) {
              populate(child, depth + 1)
            }
          }
        }

        val rootNode = entityDao.addEntity(Entity(name = "1"))
        populate(rootNode, 0)
        rootId = rootNode.id
      }

      def p(e:Entity) {
        val indent = (for (i <- 0 until e.depth) yield "\t").foldLeft("")(_ + _)
        println(indent + e.id + ": " + e.name + "\t" + e.parentId + "\t" + e.depth)
        e.getChildren.foreach(p)
      }

      val now = System.currentTimeMillis()
      entityDao.getEntityHierarchy(rootId).asHierarchy.foreach(p)
      println("Printed hierarchy in " + (System.currentTimeMillis() - now) + " ms")
    }
  }
}

trait MyPostgresDriver extends PostgresDriver { driver =>
  override def quoteIdentifier(id:String) = super.quoteIdentifier(id.toLowerCase)
}

object MyPostgresDriver extends MyPostgresDriver
