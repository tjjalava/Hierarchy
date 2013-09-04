package fi.bagroup.hierarchy.test

import org.specs2.mutable.Specification

/**
 * @author tjjalava
 * @since 26.8.2013 
 */
class AppSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

}
