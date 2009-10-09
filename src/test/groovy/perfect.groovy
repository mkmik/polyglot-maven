include com.google.GoogleCode
include org.apache.Licenses

project {
    modelVersion "4.0.0"
    groupId "com.sampullara.dbmigrate"
    artifactId "dbmigrate"
    packaging "jar"
    name "DB Migrate"
    version "1.0-SNAPSHOT"
    url "http://code.google.com/p/dbmigrate"
    description "A database migration system for Java"

    googlecode("dbmigrate")
    asl2()

    distributionManagement {
        repository("javarants", "libs-snapshot", "http://maven.javarants.com/artifactory/libs-snapshots")
    }

    repositories {
        repository("javarants", "repo", "http://maven.javarants.com/artifactory/repo")
    }

    dependencies {
        testdependency("junit", "junit", "4.0")
        testdependency("com.h2database", "h2", "1.0.63")
        dependency("com.sampullara.cli", "clie-parser", "1.0-SNAPSHOT")
        dependency("groovy", "groovy-all", "1.1-rc-1")
    }

    build {
        plugins {
            compiler("1.5")
            executableJar("com.sampullara.db.Migrate")
        }
    }
}