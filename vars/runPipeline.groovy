def call(Map config=[:]) {
  try {
    setting.configGeneral(config)
    util.templates {
      node(POD_LABEL) {
          controller.cloneCode()
          controller.testCode()
          controller.sonarScan()
          controller.pushCodeImage()
      }
    }
  } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException err) {
      post.aborted()
      throw err
  } catch (Exception err) {
      post.failure()
      throw err
  }
  post.success()
}
