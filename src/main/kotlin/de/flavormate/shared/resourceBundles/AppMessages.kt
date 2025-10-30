/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.resourceBundles

import io.quarkus.qute.i18n.MessageBundle

@MessageBundle
interface AppMessages {
  // Error page
  fun error_title(): String

  fun error_hint(): String

  fun error_openFlavormate(): String

  // Bring page
  fun bring_title(): String

  fun bring_hint1(): String

  fun bring_hint2(): String

  fun bring_promotion(input: String): String

  fun bring_promotionUrl(): String

  // Password Recovery
  fun passwordRecovery_title(): String

  fun passwordRecovery_hint1(): String

  fun passwordRecovery_hint2(): String

  fun passwordRecovery_requirement1(): String

  fun passwordRecovery_requirement2(): String

  fun passwordRecovery_requirement3(): String

  fun passwordRecovery_requirement4(): String

  fun passwordRecovery_requirement5(): String

  fun passwordRecovery_newPassword(): String

  fun passwordRecovery_showPassword(): String

  fun passwordRecovery_savePassword(): String

  // Password Recovery - OK
  fun passwordRecoverySuccess_title(): String

  fun passwordRecoverySuccess_hint1(): String

  fun passwordRecoverySuccess_hint2(): String

  // Password Recovery - Failed
  fun passwordRecoveryFailed_title(): String

  fun passwordRecoveryFailed_hint1(): String

  fun passwordRecoveryFailed_hint2(): String

  // Password Recovery - Mail
  fun passwordRecoveryMail_subject(): String

  fun passwordRecoveryMail_greeting1(input: String): String

  fun passwordRecoveryMail_greeting2(input: String): String

  fun passwordRecoveryMail_username(input: String): String

  fun passwordRecoveryMail_notYou(): String

  fun passwordRecoveryMail_hint1(): String

  fun passwordRecoveryMail_hint2(input: Long): String

  fun passwordRecoveryMail_hint3(): String

  fun passwordRecoveryMail_hint4(): String

  fun passwordRecoveryMail_resetPassword(): String

  // Public Recipe
  fun recipe_prepTime(): String

  fun recipe_cookTime(): String

  fun recipe_restTime(): String

  fun recipe_durationDays(): String

  fun recipe_durationHours(): String

  fun recipe_durationMinutes(): String

  fun recipe_durationSeconds(): String

  fun recipe_ingredientsTitle(): String

  fun recipe_instructionsTitle(): String

  fun recipe_informationTitle(): String

  fun recipe_categoriesTitle(): String

  fun recipe_tagsTitle(): String

  fun recipe_publishedTitle(): String

  fun recipe_versionTitle(): String

  fun recipe_openOriginal(): String

  fun recipe_appHint1(): String

  fun recipe_appHint2(): String

  fun recipe_appHint3(): String

  // Course
  fun course_appetizer(): String

  fun course_mainDish(): String

  fun course_dessert(): String

  fun course_sideDish(): String

  fun course_bakery(): String

  fun course_drink(): String

  // Diet
  fun diet_meat(): String

  fun diet_fish(): String

  fun diet_vegetarian(): String

  fun diet_vegan(): String

  // Verify Mail
  fun verifyMail_subject(): String

  fun verifyMail_greeting1(input: String): String

  fun verifyMail_greeting2(input: String): String

  fun verifyMail_hint1(): String

  fun verifyMail_hint2(input: Long): String

  fun verifyMail_hint3(): String

  fun verifyMail_hint4(): String

  fun verifyMail_verifyAccount(): String

  // Verify Mail - OK
  fun verifyMailSuccess_title(): String

  fun verifyMailSuccess_hint1(): String

  fun verifyMailSuccess_hint2(): String
}
