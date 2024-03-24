import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    // conveyor插件
    id("dev.hydraulic.conveyor") version "1.9"
}

group = "com.example"
version = "1.3"

repositories {
    mavenCentral()
    // conveyor使用的相关仓库
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven("https://maven.hq.hydraulic.software")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    // conveyor定义内容
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "compose-demo"
            packageVersion = "1.0.0"
        }
    }
}

kotlin {
    // 定义Java版本
    jvmToolchain(17)
}

configurations.all {
    attributes {
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}

tasks.register<Exec>("convey") {
    val dir = layout.buildDirectory.dir("packages")
    outputs.dir(dir)
    commandLine("conveyor", "make", "--overwrite", "--output-dir", dir.get(), "site")
    dependsOn("jar", "writeConveyorConfig")
}

/**
 *  注册一个Task
 */
tasks.register<Exec>("buildMacApp") {
    // 定义一Task所属组， 默认定义分组，默认在other分组中
    group = "conveyor Build"

    // 定义Task的描述
    description = "构建Mac App"

    // 定义该Task的依赖关系，每次打包前先执行clean、build操作（非必须操作）
    dependsOn("clean")
    dependsOn("build")
    // 定义依赖执行顺序
    // build任务必须在clean任务之后执行
    tasks.findByName("build")?.mustRunAfter("clean")

//    conveyor -Kapp.machines=mac.aarch64 make mac-app
//    --overwrite=HARD_REPLACE
    // 定义需要执行的命令，将compose的jar包打包成mac的app
    commandLine(
        "conveyor", "-Kapp.machines=mac.aarch64", "--conf-dir=$projectDir", // 定义conveyor.conf配置的文件的目录，可按照实际的位置进行调整
        "make", "--overwrite=HARD_REPLACE", "mac-app"
    )
}

tasks.register<Exec>("conveyorMakeSite") {
    group = "conveyor build"
    commandLine("conveyor make site")
}

tasks.register<Exec>("conveyorMakeCopiedSite") {
    group = "conveyor build"

    // 定义该Task的依赖关系，每次打包前先执行clean、build操作（非必须操作）
    dependsOn("clean")
    dependsOn("build")
    // 定义依赖执行顺序
    // build任务必须在clean任务之后执行
    tasks.findByName("build")?.mustRunAfter("clean")

    commandLine("conveyor", "--conf-dir=$projectDir", "make", "copied-site")
}



