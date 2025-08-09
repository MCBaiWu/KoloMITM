# KoloMITM
An experiment MITM implementation for Minecraft: Bedrock

### Introduction
Kolo MITM is a mitm framework for Minecraft: Bedrock. It can intercept client-side and server-side packets and modify them with simple steps. It uses Cloudburst protocol and network libraries. These approaches can help you to observe the packet flow easily.

### Notice
If you try to use KoloMITM on Windows, you will need to disable loopback restrictions. Use PowerShell and execute these commands.

```ps1
# Release Version
CheckNetIsolation LoopbackExempt -a -n="Microsoft.MinecraftUWP_8wekyb3d8bbwe"
# Preview Version
CheckNetIsolation LoopbackExempt -a -n="Microsoft.MinecraftWindowsBeta_8wekyb3d8bbwe"
```

### Build & Running
This project uses Git submodules. To clone:

```shell
# Clone with submodules
git clone https://github.com/LibKolo/KoloMITM.git --recursive
```

If you have cloned it without "--recursive" parameter:

```shell
git submodule update --init --recursive
```

To build a jar file, run `./gradlew build` in the root directory. This will produce a jar file in the `build/libs` directory.

If you wonder to run the project from source, run `./gradlew run` in the project root directory.

### Reference
You can use KoloMITM in custom projects. Just clone this project and run `./gradlew publishMavenPublicationToMavenLocal` command. Then change build.gradle.kts with:

```kotlin
repositories {
    // ... (Your other maven resolutions)
    mavenLocal()
    maven { url = uri("https://repo.opencollab.dev/maven-snapshots") }
    maven { url = uri("https://repo.opencollab.dev/maven-releases") }
}
```

Finally, include KoloMITM on your custom project:
```kotlin
dependencies {
    // ... (Your other dependencies)
    implementation("io.github.mucute.qwq.kolomitm:KoloMITM:1.0-SNAPSHOT")
}
```

### Acknowledgements
[Protocol](https://github.com/CloudburstMC/Protocol.git)
| [Network](https://github.com/CloudburstMC/Network.git)
| [ProxyPass](https://github.com/CloudburstMC/ProxyPass.git)