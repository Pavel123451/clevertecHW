package org.example.task

import org.example.extension.GitPluginExtension
import org.example.service.GitService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GetCurrentBranchTask extends DefaultTask {

    @TaskAction
    void getCurrentBranch() {
        def extension = project.extensions.findByType(GitPluginExtension)
        extension.currentBranch = GitService.getCurrentBranch()

        println "Current Branch: ${extension.currentBranch}"
    }
}
