package com.example.springdddexample.presentation.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * OpenAPI設定クラス
 * Swagger UIのドキュメント設定を行う
 */
@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Spring DDD Example API")
                    .description("ドメイン駆動設計とヘキサゴナルアーキテクチャを使用したタスク管理サービスAPI")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Spring DDD Example Team")
                            .email("example@example.com"),
                    ).license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT"),
                    ),
            ).servers(
                listOf(
                    Server()
                        .url("http://localhost:8080")
                        .description("開発環境サーバー"),
                ),
            )
}
