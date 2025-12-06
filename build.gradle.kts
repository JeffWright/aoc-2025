plugins {
  kotlin("jvm") version "2.2.20"
  id("com.ncorti.ktfmt.gradle") version "0.25.0"
  application
}

group = "dev.jtbw.aoc2025"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

ktfmt { googleStyle() }

dependencies {
  implementation(libs.bundles.coroutines)
  implementation(libs.bundles.retrofit)

  testImplementation(kotlin("test"))
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.bundles.kotest)
}

tasks.test {
  useJUnitPlatform()
  testLogging { showStandardStreams = true }
}

tasks.withType<Test> { testLogging { showStandardStreams = true } }

kotlin { jvmToolchain(17) }

application { mainClass.set("dev.jtbw.aoc2025.MainKt") }

tasks.register<JavaExec>("generateStatus") {
  group = "documentation"
  description = "Generate README.md from metrics.json"
  classpath = sourceSets["main"].runtimeClasspath
  mainClass.set("dev.jtbw.aoc2025.GenerateStatusKt")
}
