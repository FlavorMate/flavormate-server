/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.interfaces

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import kotlin.reflect.KClass

abstract class CRepository<Entity : Any>(val clazz: KClass<Entity>) :
  PanacheRepositoryBase<Entity, String> {

  override fun findById(id: String): Entity? {
    return find("id", id).firstResult()
  }

  fun existsById(id: String): Boolean {
    return count("id = ?1", id) > 0
  }

  fun findIds(): List<String> {
    val entity = clazz.simpleName
    return find("select id from $entity").project(String::class.java).list()
  }

  fun findBySchema(schema: Int): PanacheQuery<Entity> {
    return find("schema = ?1", schema)
  }
}
