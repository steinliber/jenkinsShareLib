// Config is a global config for jenkins pipeline
class Config implements Serializable {
    static Map data     = [
        locale          : 'zh_CN'
    ]

    static Map settings = [:]

    static Map notifySettings = [:]
}
