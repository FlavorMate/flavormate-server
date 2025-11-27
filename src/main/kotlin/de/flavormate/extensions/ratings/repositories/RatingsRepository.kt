/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.ratings.repositories

import de.flavormate.extensions.ratings.daos.RatingEntity
import de.flavormate.extensions.ratings.dtos.RatingsDto
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RatingsRepository : PanacheRepositoryBase<RatingEntity, String> {
  fun findByRecipeId(accountId: String, recipeId: String): RatingsDto? {

    val hql =
      """
            SELECT NEW de.flavormate.extensions.ratings.dtos.RatingsDto(
                r.id.recipeId,
                COUNT(CASE WHEN r.rating = 1 THEN 1 END),
                COUNT(CASE WHEN r.rating = 2 THEN 1 END),
                COUNT(CASE WHEN r.rating = 3 THEN 1 END),
                COUNT(CASE WHEN r.rating = 4 THEN 1 END),
                COUNT(CASE WHEN r.rating = 5 THEN 1 END),
                COUNT(r.rating),
                AVG(r.rating),
                (SELECT rr.rating FROM RatingEntity rr WHERE rr.id.recipeId = :recipeId AND rr.id.accountId = :accountId)
            )
            FROM RatingEntity r
            WHERE r.id.recipeId = :recipeId
            GROUP BY r.id.recipeId
        """

    val query = this.getEntityManager().createQuery(hql, RatingsDto::class.java)
    query.setParameter("accountId", accountId)
    query.setParameter("recipeId", recipeId)

    return query.singleResultOrNull
  }
}
