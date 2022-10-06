def call(Map config=[:]) {
    try {
        setting.configGeneral(config)
        pod.templates {
            node(POD_LABEL) {
                controller.cloneCode()
                controller.testCode()
                controller.sonarScan()
                controller.pushCodeImage()
            }
        }
    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException err) {
        post.postResult("aborted")
        throw err
    } catch (Exception err) {
        post.postResult("failure")
        throw err
    }
    post.postResult("success")
}
