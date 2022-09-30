import org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate;
import org.csanchez.jenkins.plugins.kubernetes.PodVolumes;

def withContainers(Closure body) {
    String imageAddress = Config.imageRepoSettings.get("maven_image_repo")
    boolean skip_test =  Config.generalSettings.get("skip_test")
    podTemplate(containers: [
        containerTemplate(name: 'maven', image: imageAddress, command: 'sleep', args: '99d'),
    ], volumes: [
    ]) {
       body()
    }
}
