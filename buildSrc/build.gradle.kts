plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}



dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.6.4")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
}
