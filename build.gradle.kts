import java.time.LocalDate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.all.open)
  alias(libs.plugins.quarkus)
  alias(libs.plugins.spotless)
  alias(libs.plugins.versions)
}

repositories {
  mavenCentral()
  gradlePluginPortal()
  mavenLocal()
}

dependencies {
  implementation(enforcedPlatform(libs.quarkus.platform))
  implementation("io.quarkus:quarkus-config-yaml")
  implementation("io.quarkus:quarkus-container-image-jib")
  implementation("io.quarkus:quarkus-scheduler")
  implementation("io.quarkus:quarkus-hibernate-orm")
  implementation("io.quarkus:quarkus-hibernate-validator")
  implementation("io.quarkus:quarkus-jdbc-postgresql")
  implementation("io.quarkus:quarkus-smallrye-jwt")
  implementation("io.quarkus:quarkus-smallrye-jwt-build")
  implementation("io.quarkus:quarkus-kotlin")
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-rest")
  implementation("io.quarkus:quarkus-rest-qute")
  implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
  implementation("io.quarkus:quarkus-rest-jackson")
  implementation("io.quarkus:quarkus-elytron-security-common")
  implementation("io.quarkus:quarkus-flyway")
  implementation("io.quarkus:quarkus-oidc")
  implementation("io.quarkus:quarkus-qute")
  implementation("io.quarkus:quarkus-mailer")
  implementation("io.quarkus:quarkus-oidc-client")
  implementation(libs.kotlin.stdlib.jdk8)
  implementation(libs.apache.commons.io)
  implementation(libs.apache.commons.lang3)
  implementation(libs.jackson.datatype.jsr310)
  implementation(libs.jackson.module.kotlin)
  implementation(libs.nanoid)
  implementation(libs.ksoup.general)
  implementation(libs.ksoup.network)
  implementation(libs.apache.http.client5)

  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.quarkus:quarkus-junit5-mockito")
  testImplementation("io.rest-assured:rest-assured")
  testImplementation(libs.assertj)
}

group = "de.flavormate"

version = "3.5.1"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

allOpen {
  annotation("jakarta.ws.rs.Path")
  annotation("jakarta.enterprise.context.ApplicationScoped")
  annotation("jakarta.persistence.Entity")
  annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
  compilerOptions {
    jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    javaParameters = true
  }
}

spotless {
  ratchetFrom("HEAD")
  format("misc") {
    // define the files to apply `misc` to
    target(".gitattributes", ".gitignore")

    // define the steps to apply to those files
    trimTrailingWhitespace()
    leadingSpacesToTabs()
    endWithNewline()
  }
  java {
    cleanthat()
    googleJavaFormat(libs.versions.google.java.format.get())
      .reflowLongStrings()
      .formatJavadoc(true)
      .reorderImports(true)
    formatAnnotations()
    removeUnusedImports()
    licenseHeader("/* Licensed under AGPLv3 2024 - ${LocalDate.now().year} */")
  }
  flexmark {
    target("**/*.md")
    flexmark()
  }
  format("styling") {
    target("**/*.css", "**/*.js", "**/*.json", "**/*.yaml")
    prettier()
  }
  kotlin {
    // by default the target is every '.kt' and '.kts' file in the java source sets
    ktfmt().googleStyle()
    licenseHeader(
      "/* Licensed under AGPLv3 2024 - ${LocalDate.now().year} */"
    ) // or licenseHeaderFile
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt().googleStyle()
  }
}

apply(from = "$projectDir/gradle/preCommit.gradle")

val compileKotlin: KotlinCompile by tasks

compileKotlin.compilerOptions {
  freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
}
