// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.util.Properties

val localProperties = Properties().apply {
    val file = rootDir.resolve("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) } // 중앙 포털/서명 자격증명을 로컬에서만 로드
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    id("com.gradleup.nmcp.aggregation") version "1.3.0" // Maven Central 포털 업로드용 NMCP 집계 플러그인
}

nmcpAggregation {
    centralPortal {
        username = localProperties.getProperty("centralPortalUsername") ?: ""
        password = localProperties.getProperty("centralPortalPassword") ?: ""
        publishingType = "USER_MANAGED" // 검증 후 포털에서 수동 Release (자동은 AUTOMATIC으로 변경)
        publicationName = "CodingHarang Libraries" // 포털에 표시될 릴리스 이름
    }
//    publishAllProjectsProbablyBreakingProjectIsolation() // `maven-publish` 적용 모듈 모두를 집계 대상으로 포함

    dependencies {
        nmcpAggregation(project(":log")) // log만 maven 업로드 대상
    }
}
