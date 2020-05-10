import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths
import java.util.stream.Collectors

class PropertyFileGenerator extends DefaultTask {

    @TaskAction
    void generatePropertyFile() {
        def propertiesFilePath = "${System.getProperty("user.dir")}/src/main/resources/application-release.properties"
        update(Paths.get(propertiesFilePath).toFile())
        println "Generating property file is done"
    }

    def update(file) {
        def content = file.readLines().stream()
                .map({ property -> updateProperty property })
                .collect(Collectors.joining('\n'))
        file.text = content
    }

    def updateProperty(String property) {
        def matcher = property =~ /\$\{([^{}]+)}/
        matcher.results()
                .forEach({result -> property = property.replace(result.group(), System.getenv(result.group(1)))})
        property
    }
}