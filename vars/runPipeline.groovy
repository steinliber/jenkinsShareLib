def call(Map config=[:]) {
    setting.configGeneral(config)
    templates {
        entry()
    }
}

def entry() {
    node(POD_LABEL) {
        try {
            controller.cloneCode()
            controller.testCode()
            controller.sonarScan()
            controller.pushCodeImage()
            post.success()
        } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException err) {
            post.aborted()
            throw err
        } catch (Exception err) {
            post.failure()
            throw err
        }
    }
}



def templates(Closure body) {
    def s = Config.generalSettings
    if (!s.test_enable) {
        if (s.sonarqube_enable) {
            pod.scannerTemplate {
                pod.buildTemplate {
                    body.call()
                }
            }
        } else {
            pod.buildTemplate {
                body.call()
            }
        }
    } else {
        if (s.sonarqube_enable) {
            pod.testTemplate {
                pod.scannerTemplate {
                    pod.buildTemplate {
                        body.call()
                    }
                }
            }
        } else {
            pod.testTemplate {
                pod.buildTemplate {
                    body.call()
                }
            }
        }
    }
}
