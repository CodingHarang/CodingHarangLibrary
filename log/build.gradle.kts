plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = "io.github.codingharang"
version = "1.0.1"

android {
    namespace = "com.codingharang.log"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true // Keep BuildConfig enabled for consumers
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }
}

mavenPublishing {
    publishToMavenCentral(
        automaticRelease = false,
        validateDeployment = false
    )
    signAllPublications()
    coordinates(group.toString(), "log", version.toString())
    pom {
        name.set("Log")
        description.set("CodingHarang Log Library")
        url.set("https://github.com/CodingHarang/CodingHarangLibrary")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }
        developers {
            developer {
                id.set("CodingHarang")
                name.set("Harang")
                url.set("https://github.com/CodingHarang")
            }
        }
        scm {
            url.set("https://github.com/CodingHarang/CodingHarangLibrary")
            connection.set("scm:git:git://github.com/CodingHarang/CodingHarangLibrary.git")
            developerConnection.set("scm:git:ssh://github.com/CodingHarang/CodingHarangLibrary.git")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
