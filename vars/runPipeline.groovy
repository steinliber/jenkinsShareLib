def call(Map config=[:]) {
    try {
        setting.configGeneral(config)
        pod.templates {
            entry()
        }
    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException err) {
        post.postResult("aborted")
        throw err
    } catch (Exception err) {
        post.postResult("failure")
        throw err
    }
}

def entry() {
    node(POD_LABEL) {
        try {
            controller.cloneCode()
            controller.testCode()
            controller.sonarScan()
            controller.pushCodeImage()
            post.postResult("success")
        } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException err) {
            println(err.getMessage())
            post.aborted()
        } catch (Exception err) {
            println(err.getMessage())
            post.failure()
        }
    }
}
