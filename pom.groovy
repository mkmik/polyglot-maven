include com.google.GoogleCode
include org.apache.Licenses

project {
    modelVersion "4.0.0"
    groupId "com.googlecode.graven"
    artifactId "graven"
    packaging "jar"
    name "Graven"
    version "1.0-SNAPSHOT"
    url "http://code.google.com/p/graven"
    description "Build your POM files in groovy"

    googlecode("graven")
    asl2()

    distributionManagement {
        repository("javarants", "libs-snapshot", "http://maven.javarants.com/artifactory/libs-snapshots")
    }

    repositories {
        repository("javarants", "repo", "http://maven.javarants.com/artifactory/repo")
    }

    dependencies {
        groovydeps()
    }

    build {
        plugins {
            compiler("1.5")
            executableJar("com.googlecode.graven.GVN")
            groovy()
        }
    }

}