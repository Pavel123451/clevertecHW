package org.example.task

import org.example.exception.UncommittedChangesException
import org.example.service.GitService
import org.example.service.TagService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CheckUncommittedChangesTask extends DefaultTask {

    @OutputFile
    def outputFile = new File(project.projectDir, "log.txt")

    @TaskAction
    void checkForUncommittedChanges() {
        if (GitService.hasUncommittedChanges()) {
            def lastTag = TagService.getLastTag()
            def uncommittedTag = "${lastTag}.uncommitted"
            outputFile.text = "\n${new Date()} ${uncommittedTag}"
            throw new UncommittedChangesException("Uncommitted changes detected.")
        }
    }
}
