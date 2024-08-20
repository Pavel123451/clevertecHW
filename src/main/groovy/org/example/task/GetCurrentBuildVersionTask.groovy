package org.example.task

import org.example.exception.CurrentBuildTagExistsException
import org.example.exception.UncommitedChangesException
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class GetCurrentBuildVersionTask extends DefaultTask {

    @InputFile
    def inputFile = new File(project.projectDir, "currentBranch.txt")

    @OutputFile
    def outputFile = new File(project.projectDir, "log.txt")

    private boolean uncommittedChanges = false
    private String lastTag = "v0.0"

    @Input
    String getUncommittedStatus() {
        checkForUncommittedChanges()
        return uncommittedChanges ? "uncommitted" : "committed"
    }

    @Input
    String getLastTag() {
        findLastTag()
        return lastTag ?: "v0.0"
    }

    private void checkForUncommittedChanges() {
        def uncommittedChangesExec = "git status --porcelain --untracked-files=no".execute()
        uncommittedChangesExec.waitFor()
        uncommittedChanges = !uncommittedChangesExec.text.trim().isEmpty()
    }

    private void findLastTag() {
        def tagListExec = "git tag --sort=-v:refname".execute()
        tagListExec.waitFor()
        def tagList = tagListExec.text.trim().split("\n")
        lastTag = tagList ? tagList[0] : "v0.0"
    }

    private void checkForCurrentBuildGitTagExists() {
        def currentBuildTagExec = "git describe --tags --exact-match".execute()
        currentBuildTagExec.waitFor()
        def currentBuildTag = currentBuildTagExec.text
        if (!currentBuildTag.trim().isEmpty()) {
            throw new CurrentBuildTagExistsException(
                    "Git tag for the current state of the project already exists: ${currentBuildTag.trim()}"
            )
        }
    }

    private void handleUncommittedChanges() {
        if (uncommittedChanges) {
            def uncommittedTag = "${lastTag}.uncommitted"
            outputFile.text = "\n${new Date()} ${uncommittedTag}"
            throw new UncommitedChangesException("Uncommitted changes detected")
        }
    }

    private String generateNewTag(String currentBranch) {
        def cleanedTag = lastTag.replaceAll("[^\\d.]", "")
        def parts = cleanedTag.split("\\.")*.toInteger()
        def major = parts[0]
        def minor = parts[1]
        switch (currentBranch) {
            case "dev":
            case "qa":
                minor += 1
                return "v${major}.${minor}"
            case "stage":
                minor += 1
                return "v${major}.${minor}-rc"
            case "master":
                major += 1
                minor = 0
                return "v${major}.${minor}"
            default:
                return "${lastTag}-SNAPSHOT"
        }
    }

    private void createAndPushTag(String newTag) {
        def tagExec = "git tag ${newTag}".execute()
        tagExec.waitFor()
        println "Tag '${newTag}' has been created."

        def pushTagExec = "git push origin ${newTag}".execute()
        pushTagExec.waitFor()
        println "Tag '${newTag}' has been pushed to origin."
    }

    @TaskAction
    void perform() {
        checkForUncommittedChanges()
        handleUncommittedChanges()

        checkForCurrentBuildGitTagExists()
        findLastTag()

        println "Last tag: ${lastTag}"

        def currentBranch = inputFile.text.trim()
        def newTag = generateNewTag(currentBranch)

        outputFile.text = "\n${new Date()} ${newTag}"
        println "New tag: ${newTag}"

        createAndPushTag(newTag)
    }
}

