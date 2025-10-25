# spring-ddd-example

## 概要

シンプルなタスク管理サービスの API です。ドメイン駆動設計とヘキサゴナルアーキテクチャをベースにしています。

## 機能

- ユーザーの登録、参照、更新、削除
- ユーザーのタスクの登録、参照、更新、削除

## セットアップと実行

### 前提条件
- Java 21以上
- Gradle 8.x以上（または`./gradlew`を使用）

### ビルドとテスト

```bash
# プロジェクトのビルド
./gradlew build

# アプリケーションの実行
./gradlew bootRun

# テストの実行
./gradlew test

# 特定のテストクラスを実行
./gradlew test --tests "UserTest"

# 特定のテストメソッドを実行
./gradlew test --tests "UserTest.正常な値でUserが作成できること"

# カバレッジレポートの生成
./gradlew jacocoTestReport

# テスト + カバレッジ検証
./gradlew check

# プロジェクトのクリーン
./gradlew clean
```

## アクセス情報

アプリケーション起動後（`./gradlew bootRun`）、以下のURLでアクセスできます：

### API Documentation
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

### Database Console
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:taskdb`
  - User Name: `sa`
  - Password: (空白)

### REST API Endpoints

#### ユーザー管理
- `GET /api/users` - 全ユーザー取得
- `GET /api/users/{id}` - ユーザー詳細取得
- `POST /api/users` - ユーザー作成
- `PUT /api/users/{id}` - ユーザー更新
- `DELETE /api/users/{id}` - ユーザー削除

### レポート

- **テストレポート**: `build/reports/tests/test/index.html`
- **カバレッジレポート**: `build/reports/jacoco/test/html/index.html`

## ディレクトリ・ファイル構成

```
src/
└── main/
    ├── kotlin/
    │   └── com/
    │       └── example/
    │           └── springdddexample/
    │               │
    │               ├── domain/                           # ドメイン層
    │               │   │
    │               │   ├── model/
    │               │   │   │
    │               │   │   ├── user/                     # User集約
    │               │   │   │   ├── User.kt               # 集約ルート
    │               │   │   │   ├── UserId.kt             # 値オブジェクト（ULID）
    │               │   │   │   ├── UserName.kt           # 値オブジェクト
    │               │   │   │   ├── Email.kt              # 値オブジェクト
    │               │   │   │   └── UserRepository.kt     # リポジトリIF（ポート）
    │               │   │   │
    │               │   │   └── task/                     # Task集約
    │               │   │       ├── Task.kt               # 集約ルート
    │               │   │       ├── TaskId.kt             # 値オブジェクト（ULID）
    │               │   │       ├── TaskName.kt           # 値オブジェクト
    │               │   │       ├── TaskStatus.kt         # 値オブジェクト（Enum）
    │               │   │       └── TaskRepository.kt     # リポジトリIF（ポート）
    │               │   │
    │               │   └── shared/                       # 共有カーネル
    │               │       └── DomainException.kt        # ドメイン例外の基底クラス
    │               │
    │               ├── application/                      # アプリケーション層（ユースケース）
    │               │   │
    │               │   ├── service/
    │               │   │   ├── user/
    │               │   │   │   └── UserApplicationService.kt
    │               │   │   │
    │               │   │   └── task/
    │               │   │       └── TaskApplicationService.kt
    │               │   │
    │               │   └── dto/
    │               │       │
    │               │       ├── user/
    │               │       │   ├── CreateUserCommand.kt
    │               │       │   ├── UpdateUserCommand.kt
    │               │       │   └── UserResult.kt
    │               │       │
    │               │       └── task/
    │               │           ├── CreateTaskCommand.kt
    │               │           ├── UpdateTaskCommand.kt
    │               │           ├── CompleteTaskCommand.kt
    │               │           └── TaskResult.kt
    │               │
    │               ├── infrastructure/                   # インフラストラクチャ層
    │               │   │
    │               │   └── persistence/
    │               │       └── jpa/
    │               │           │
    │               │           ├── entity/               # JPAエンティティ
    │               │           │   ├── user/
    │               │           │   │   └── UserJpaEntity.kt
    │               │           │   │
    │               │           │   └── task/
    │               │           │       └── TaskJpaEntity.kt
    │               │           │
    │               │           ├── repository/           # Spring Data JPA
    │               │           │   ├── user/
    │               │           │   │   └── UserJpaRepository.kt
    │               │           │   │
    │               │           │   └── task/
    │               │           │       └── TaskJpaRepository.kt
    │               │           │
    │               │           ├── adapter/              # リポジトリアダプター
    │               │           │   ├── user/
    │               │           │   │   └── UserRepositoryAdapter.kt
    │               │           │   │
    │               │           │   └── task/
    │               │           │       └── TaskRepositoryAdapter.kt
    │               │           │
    │               │           ├── mapper/               # ドメイン⇔JPA変換
    │               │           │   ├── user/
    │               │           │   │   └── UserMapper.kt
    │               │           │   │
    │               │           │   └── task/
    │               │           │       └── TaskMapper.kt
    │               │           │
    │               │           └── config/
    │               │               └── JpaConfig.kt
    │               │
    │               └── presentation/                     # プレゼンテーション層
    │                   │
    │                   ├── rest/
    │                   │   ├── user/
    │                   │   │   └── UserController.kt
    │                   │   │
    │                   │   └── task/
    │                   │       └── TaskController.kt
    │                   │
    │                   ├── dto/
    │                   │   │
    │                   │   ├── user/
    │                   │   │   ├── CreateUserRequest.kt      # toCommand()拡張関数を含む
    │                   │   │   ├── UpdateUserRequest.kt      # toCommand()拡張関数を含む
    │                   │   │   └── UserResponse.kt           # from()ファクトリ関数を含む
    │                   │   │
    │                   │   └── task/
    │                   │       ├── CreateTaskRequest.kt      # toCommand()拡張関数を含む
    │                   │       ├── UpdateTaskRequest.kt      # toCommand()拡張関数を含む
    │                   │       ├── TaskResponse.kt           # from()ファクトリ関数を含む
    │                   │       └── TaskListResponse.kt       # from()ファクトリ関数を含む
    │                   │
    │                   ├── exception/
    │                   │   ├── GlobalExceptionHandler.kt
    │                   │   └── ErrorResponse.kt
    │                   │
    │                   └── config/
    │                       ├── WebConfig.kt
    │                       └── OpenApiConfig.kt
    │
    └── resources/
        ├── application.yml
        ├── application-dev.yml
        └── application-prod.yml
```