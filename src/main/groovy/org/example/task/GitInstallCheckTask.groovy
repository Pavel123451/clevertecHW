package org.example.task

import org.example.exception.GitRemoteNotConfiguredException
import org.example.service.GitService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GitInstallCheckTask extends DefaultTask {

    @TaskAction
    void checkGitInstall() {
        def gitVersion = GitService.getGitVersion()
        println "Git Version: $gitVersion"

        def remotes = GitService.getRemoteRepositories()
        if (remotes.isEmpty()) {
            throw new GitRemoteNotConfiguredException("No remote repositories are configured.")
        }

        println "Configured remote repositories: $remotes"
    }
}
