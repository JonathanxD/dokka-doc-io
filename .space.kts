job("Build and run tests") {
   gradlew("openjdk:15", "build") {
      env["ORG_GRADLE_PROJECT_signingKey"] = Secrets("org_gradle_project_signingkey")
      env["ORG_GRADLE_PROJECT_signingPassword"] = Secrets("org_gradle_project_signingpassword")
      env["OSSRH_USERNAME"] = Secrets("ossrh_username")
      env["OSSRH_PASSWORD"] = Secrets("ossrh_password")
      env["GRADLE_PUBLISH_KEY"] = Secrets("gradle_publish_key")
      env["GRADLE_PUBLISH_SECRET"] = Secrets("gradle_publish_secret")
   }
}

job("Publish plugin to Gradle and Maven") {
   startOn {
      gitPush {
         branchFilter {
            +Regex("version\\/(.*)")
         }
      }
   }

   gradlew("openjdk:15", "gradlePublish", "mavenPublish") {
      env["ORG_GRADLE_PROJECT_signingKey"] = Secrets("org_gradle_project_signingkey")
      env["ORG_GRADLE_PROJECT_signingPassword"] = Secrets("org_gradle_project_signingpassword")
      env["OSSRH_USERNAME"] = Secrets("ossrh_username")
      env["OSSRH_PASSWORD"] = Secrets("ossrh_password")
      env["GRADLE_PUBLISH_KEY"] = Secrets("gradle_publish_key")
      env["GRADLE_PUBLISH_SECRET"] = Secrets("gradle_publish_secret")
   }
}