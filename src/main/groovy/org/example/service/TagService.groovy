package org.example.service

class TagService {

    static String getLastTag() {
        def tags = GitService.executeCommand("git tag --sort=-v:refname")
        def tagList = tags.split("\n")
        return tagList ? tagList[0] : "v0.0"
    }

    static void createTag(String tag) {
        GitService.executeCommand("git tag $tag")
    }

    static void pushTag(String tag) {
        GitService.executeCommand("git push origin $tag")
    }

    static boolean currentBuildTagExists() {
        def tag = GitService.executeCommand("git describe --tags --exact-match")
        return !tag.isEmpty()
    }

    static String generateNewTag(String currentBranch, String lastTag) {
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
}

