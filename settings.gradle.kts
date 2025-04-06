pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        mavenCentral()
        mavenLocal()
        maven("https://maven.blinkt.de/artifactory/public")
        maven("https://jitpack.io")
    }
}

rootProject.name = "EVPN"
include(":app")
include(":admin")

include(":vpnLib")//old sdk 30

//include(":vpnLib")
//project(":vpnLib").projectDir = File("D:\\new\\NEW\\BKP\\codecanyon\\Cake-VPN\\vpnLib")

//include(":openvpn")//api 33
//project(":openvpn").projectDir = File("D:\\new\\NEW\\BKP\\codecanyon\\ether-vpn\\openvpn")

include(":features:ui")
project(":features:ui").projectDir = File("C:\\src\\Synced\\WalhallaUI\\features\\ui")

include(":c")
project(":c").projectDir = File("C:\\src\\Synced\\WalhallaUI\\kwk\\corelib\\")

//include(":stub_onesignal")
//project(":stub_onesignal").projectDir = File("D:\\kwork\\jakubai\\kgbook\\stub_onesignal\\")
//include(":stub_appsflyer")
//project(":stub_appsflyer").projectDir = File("D:\\kwork\\jakubai\\kgbook\\stub_appsflyer\\")
include(":kwk:StelthCore")
project(":kwk:StelthCore").projectDir = File("C:\\src\\Synced\\WalhallaUI\\kwk\\StelthCore\\")

apply(from="C:\\src\\Synced\\WalhallaUI\\kwk\\corelib\\submodules.gradle")

include(":features:webview")
project(":features:webview").projectDir = File("C:\\src\\Synced\\WalhallaUI\\features\\webview\\")

include(":vpnapp")
include(":Desktop")

include(":features:wads")
project(":features:wads").projectDir = File("C:\\src\\Synced\\WalhallaUI\\features\\wads\\")

include(":threader")
project(":threader").projectDir = File("D:\\walhalla\\sdk\\android\\multithreader\\threader\\")