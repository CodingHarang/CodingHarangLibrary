import java.util.Properties

val localProps = Properties().apply {
    val file = rootDir.resolve("local.properties")
    if (file.exists()) file.inputStream().use { load(it) } // 서명/포털 자격 증명을 로드
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish") // Maven 퍼블리케이션 생성
    id("signing") // 퍼블리케이션 GPG 서명
}

group = "io.github.codingharang" // 중앙 포털 승인 groupId
version = "1.0.1" // 라이브러리 버전

android {
    namespace = "com.codingharang.util"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    publishing { singleVariant("release") { withSourcesJar(); withJavadocJar() } } // release 변형만 퍼블리시, 소스/문서 JAR 포함
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"]) // Android release 컴포넌트를 아티팩트로 사용
                groupId = group.toString() // 상단 group과 동일
                artifactId = "util" // 모듈 artifactId
                version = project.version.toString() // 상단 version과 동일
                pom {
                    name.set("Util") // POM name
                    description.set("CodingHarang Util Library") // POM description
                    url.set("https://github.com/CodingHarang/CodingHarangLibrary") // 프로젝트 URL
                    licenses {
                        license {
                            name.set("Apache-2.0") // 라이선스 이름
                            url.set("https://www.apache.org/licenses/LICENSE-2.0") // 라이선스 URL
                        }
                    }
                    developers {
                        developer {
                            id.set("CodingHarang") // 개발자 ID
                            name.set("Harang") // 개발자 이름
                        }
                    }
                    scm {
                        url.set("https://github.com/CodingHarang/CodingHarangLibrary") // SCM 웹 URL
                        connection.set("scm:git:git://github.com/CodingHarang/CodingHarangLibrary.git") // 읽기용
                        developerConnection.set("scm:git:ssh://github.com/CodingHarang/CodingHarangLibrary.git") // 개발자용
                    }
                }
            }
        }
    }

    signing {
        val signingKeyId = localProps.getProperty("signing.keyId") // 키 ID (하위 8/16자리)
        val signingKey = localProps.getProperty("signing.key") // ASCII-armored 비밀키
        val signingPassword = localProps.getProperty("signing.password") // 패스프레이즈
        if (!signingKey.isNullOrBlank()) {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword) // 메모리 상에서 키 로드
            sign(publishing.publications["release"]) // release 퍼블리케이션 서명
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
