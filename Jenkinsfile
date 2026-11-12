pipeline {

    // Run on any available Jenkins agent.
    // Replace 'any' with a specific label (e.g., agent { label 'windows' })
    // if your Jenkins controller manages multiple nodes.
    agent any

    // -------------------------------------------------------------------------
    // Global environment variables available to every stage
    // -------------------------------------------------------------------------
    environment {
        // Ensures Gradle does not prompt for user input during the build
        GRADLE_OPTS = '-Dorg.gradle.daemon=false'
    }

    // -------------------------------------------------------------------------
    // Global options
    // -------------------------------------------------------------------------
    options {
        // Keep logs and artifacts for the last 10 builds only
        buildDiscarder(logRotator(numToKeepStr: '10'))

        // Fail the build if it runs for more than 15 minutes
        timeout(time: 15, unit: 'MINUTES')

        // Add timestamps to every line in the console log
        timestamps()
    }

    // =========================================================================
    // Stages
    // =========================================================================
    stages {

        // ---------------------------------------------------------------------
        // Stage 1 – Test
        // Run all JUnit tests via the Gradle wrapper before attempting a build.
        // Failing fast here saves time: no point building if tests are broken.
        // ---------------------------------------------------------------------
        stage('Test') {
            steps {
                echo 'Running JUnit tests...'
                bat 'gradlew.bat test'
            }
            post {
                always {
                    // Publish JUnit XML results so Jenkins shows a test trend graph
                    junit testResults: 'build/reports/tests/test/**/*.xml',
                          allowEmptyResults: false
                }
            }
        }

        // ---------------------------------------------------------------------
        // Stage 2 – Build
        // Compile and package the application into an executable JAR.
        // '-x test' skips tests here because Stage 1 already ran them.
        // ---------------------------------------------------------------------
        stage('Build') {
            steps {
                echo 'Building the application...'
                bat 'gradlew.bat build -x test'
            }
        }

        // ---------------------------------------------------------------------
        // Stage 3 – Archive
        // Save the generated JAR as a Jenkins build artifact so it can be
        // downloaded from the Jenkins UI or used by downstream jobs.
        // ---------------------------------------------------------------------
        stage('Archive') {
            steps {
                echo 'Archiving build artifacts...'
                archiveArtifacts artifacts: 'build/libs/*.jar',
                                 fingerprint: true,
                                 allowEmptyArchive: false
            }
        }
    }

    // =========================================================================
    // Post Actions
    // Run after all stages, regardless of outcome.
    // =========================================================================
    post {

        success {
            echo '============================================='
            echo ' BUILD SUCCEEDED'
            echo ' The application was built and tested successfully.'
            echo ' Artifacts are available in the Jenkins build page.'
            echo '============================================='
        }

        failure {
            echo '============================================='
            echo ' BUILD FAILED'
            echo ' Check the console output above for details.'
            echo ' Common causes:'
            echo '   - Compilation errors in the source code'
            echo '   - Failing JUnit tests'
            echo '   - Missing JAVA_HOME environment variable on the agent'
            echo '============================================='
        }

        always {
            echo "Build finished with status: ${currentBuild.currentResult}"
        }
    }
}
