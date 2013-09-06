package fi.bagroup.hierarchy.model

/**
 * @author tjjalava
 * @since 3.9.2013 
 */
case class Entity(_id:Option[Long] = None, name:String, description:String = "") {
  def id:Long = _id.getOrElse(-1)
}


