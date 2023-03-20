plugins {
    kotlin("multiplatform")
}

kotlin {
    wasm {
        binaries.executable()
        nodejs()
        applyBinaryen()
    }
    sourceSets {
        val wasmMain by getting {
            dependencies {
                implementation(project(":web"))
                implementation(project(":kotlin-html-wasm"))
                implementation(project(":wasi"))
            }
        }
    }
}
