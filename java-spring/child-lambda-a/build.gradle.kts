import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer

plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.virtuslab"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2024.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.cloud:spring-cloud-function-context")
	implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
	implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
	implementation("com.amazonaws:aws-lambda-java-events:3.11.4")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = false
}

tasks {
    shadowJar {
        // This merges service files (like Spring's META-INF/spring.factories / spring.*).
        // It's often needed so auto-configuration doesn't get lost.
				mergeServiceFiles()
				
				append("META-INF/spring.handlers")
				append("META-INF/spring.schemas")
				append("META-INF/spring.tooling")
				append("META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
				append("META-INF/spring/org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration.imports")

				transform(PropertiesFileTransformer::class.java) {
						paths = listOf("META-INF/spring.factories")
						mergeStrategy = "append"
				}

        manifest {
            attributes(
                "Main-Class" to "org.springframework.cloud.function.adapter.aws.FunctionInvoker",
                "Start-Class" to "com.virtuslab.parent_lambda.ParentLambdaApplication"
            )
        }
    }
}

tasks {
    shadowJar {
        // This merges service files (like Spring's META-INF/spring.factories / spring.*).
        // It's often needed so auto-configuration doesn't get lost.
				mergeServiceFiles()
				
				append("META-INF/spring.handlers")
				append("META-INF/spring.schemas")
				append("META-INF/spring.tooling")
				append("META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
				append("META-INF/spring/org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration.imports")

				transform(PropertiesFileTransformer::class.java) {
						paths = listOf("META-INF/spring.factories")
						mergeStrategy = "append"
				}

				manifest {
            attributes(
                "Main-Class" to "org.springframework.cloud.function.adapter.aws.FunctionInvoker",
                "Start-Class" to "com.virtuslab.child_lambda_a.ChildLambdaAApplication"
            )
        }
    }
}