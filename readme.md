# compose-multiplatform示例

## 使用[conveyor](https://conveyor.hydraulic.dev/13.1/)打包Mac上app包

1. 第一步定义配置生成generated.conveyor.conf文件的任务，具体参考
    > 整合Gradle项目：https://conveyor.hydraulic.dev/13.1/configs/maven-gradle/
    ```groovy
    
    tasks.register<Exec>("convey") {
        val dir = layout.buildDirectory.dir("packages")
        outputs.dir(dir)
        commandLine("conveyor", "make", "--overwrite", "--output-dir", dir.get(), "site")
        dependsOn("jar", "writeConveyorConfig")
    }
    ```
2. 执行Gradle中的writeConveyorConfig任务生成[generated.conveyor.conf](generated.conveyor.conf)文件
2. 配置[conveyor.conf](conveyor.conf)内容
    ```hocon
    include "#!./gradlew -q printConveyorConfig"
    include required("generated.conveyor.conf")
    
    app {
      display-name = Compose Demo
      site.base-url = "localhost:3000"
    
      icons = "icons/icon-rounded-*.png"
      windows.icons = "icons/icon-square-*.png"
    }
    
    conveyor.compatibility-level = 13
    
    ```

3.配置打包配置内容
```groovy

/**
 *  注册一个Task打包的Task
 */
tasks.register<Exec>("build Mac app") {
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
        "conveyor",
        "-Kapp.machines=mac.aarch64",
        "--conf-dir=$projectDir", // 定义conveyor.conf配置的文件的目录，可按照实际的位置进行调整
        "make",
        "--overwrite=HARD_REPLACE",
        "mac-app"
    )
}
```

其他参考地址：
> conveyor地址：https://conveyor.hydraulic.dev/13.1/
>
> Kotlin开发者：https://mp.weixin.qq.com/s/uLTX-akd7NBgooRDF4xU7w
>
> 整合Gradle项目：https://conveyor.hydraulic.dev/13.1/configs/maven-gradle/
>
> 官方Github介绍打包文档：https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Native_distributions_and_local_execution
>
> 教程目录：https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials
