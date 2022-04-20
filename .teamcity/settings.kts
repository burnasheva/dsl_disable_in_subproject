import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.parallelTests
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    vcsRoot(VersionedSettings_DisableVersionedSettingsInSubprojec_MavenTestsRoot)

    buildType(VersionedSettings_DisableVersionedSettingsInSubprojec_HelloWorld)
    buildType(VersionedSettings_DisableVersionedSettingsInSubprojec_RunParallelTests)
}

object VersionedSettings_DisableVersionedSettingsInSubprojec_HelloWorld : BuildType({
    id("HelloWorld")
    name = "hello world"

    params {
        param("new.parameter", "new.value.5")
    }
})

object VersionedSettings_DisableVersionedSettingsInSubprojec_RunParallelTests : BuildType({
    id("RunParallelTests")
    name = "run parallel tests"

    vcs {
        root(VersionedSettings_DisableVersionedSettingsInSubprojec_MavenTestsRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    features {
        parallelTests {
            numberOfBatches = 2
        }
    }
    
    params {
        param("new.parameter", "new.value.1")
    }
})

object VersionedSettings_DisableVersionedSettingsInSubprojec_MavenTestsRoot : GitVcsRoot({
    id("MavenTestsRoot")
    name = "maven tests root"
    url = "https://github.com/burnasheva/maven_unbalanced_messages.git"
    branch = "refs/heads/master"
    branchSpec = "+:refs/heads/*"
})
