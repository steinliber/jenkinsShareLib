import org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate;
import org.csanchez.jenkins.plugins.kubernetes.PodVolumes;

def call(Closure body) {
    String imageAddress = Config.imageRepoSettings.get("maven_image_repo")
    boolean skip_test =  Config.generalSettings.get("skip_test")
    podTemplate(containers: [
        containerTemplate(name: 'maven', image: imageAddress, command: 'sleep', args: '99d'),
    ], volumes: [
        secretVolume(secretName: "${dockerImageSecretName}", mountPath: '/root/.docker')
    ]) {
        node(POD_LABEL) {
            body()
        }
    }
}
