plugins {
    // Apply the common convention plugin for shared build configuration between library and application projects.
    id("compartor.java-common-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")

}

dependencies{
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-test")
}

extra["springCloudVersion"] = "2021.0.1"
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}