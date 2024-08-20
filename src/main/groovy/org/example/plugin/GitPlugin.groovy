package org.example.plugin

import org.example.task.GetCurrentBranchTask
import org.example.task.GetCurrentBuildVersionTask
import org.example.task.GitInstallCheckTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class GitPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.tasks.register("gitInstallCheck", GitInstallCheckTask) {
            group = 'git'
        }

        project.tasks.register("getCurrentBranch", GetCurrentBranchTask) {
            group = 'git'
            dependsOn 'gitInstallCheck'
        }

        project.tasks.register("getCurrentBuildVersionTask", GetCurrentBuildVersionTask) {
            group = 'git'
            dependsOn 'getCurrentBranch'
        }


    }
}
