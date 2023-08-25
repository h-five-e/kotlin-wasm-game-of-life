plugins {
    kotlin("multiplatform") version "1.9.0"
}

group = "me.hanneke"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    wasm {
        binaries.executable()
        browser {

        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val wasmMain by getting
        val wasmTest by getting
    }
}
