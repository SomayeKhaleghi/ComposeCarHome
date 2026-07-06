pluginManagement {
    repositories {
        clear()
        maven { url = uri("https://maven.myket.ir")}
       /* google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()*/
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        clear()
        maven { url = uri("https://maven.myket.ir")}
        /*maven { url = uri("https://maven.myket.ir") }
        google()
        mavenCentral()*/
    }
}

rootProject.name = "ComposeCarHome"
include(":app")
 
