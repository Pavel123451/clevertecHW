package org.example.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class GetCurrentBranchTask extends DefaultTask {

    @OutputFile
    def outputFile = new File(project.projectDir, "currentBranch.txt")

    @Input
    String getCurrentBranchName() {
        def showCurrentBranchExec = "git branch --show-current".execute()
        showCurrentBranchExec.waitFor()
        return showCurrentBranchExec.text
    }

    @TaskAction
    void getCurrentBranch() {
        def currentBranch = getCurrentBranchName()

        outputFile.text = currentBranch

        println "Current Branch: $currentBranch"
    }
}
