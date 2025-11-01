# spring-ddd-example

## 概要

シンプルなタスク管理サービスの API です。ドメイン駆動設計とヘキサゴナルアーキテクチャをベースにしています。

## 機能

- ユーザーの登録、参照、更新、削除
- ユーザーのタスクの登録、参照、更新、削除

## セットアップと実行

### 前提条件
- Java 21
- Gradle 8.x

### ビルドとテスト

```bash
# プロジェクトのビルド
./gradlew build

# テストの実行
./gradlew test

# カバレッジレポートの生成
./gradlew jacocoTestReport

# コードスタイルチェック
./gradlew ktlintCheck

# コード自動フォーマット
./gradlew ktlintFormat

# テスト + カバレッジ検証 + コード品質チェック
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

#### タスク管理
- `GET /api/tasks` - 全タスク取得
- `GET /api/tasks/{id}` - タスク詳細取得
- `POST /api/tasks` - タスク作成
- `PUT /api/tasks/{id}` - タスク更新
- `DELETE /api/tasks/{id}` - タスク削除
- `POST /api/tasks/{id}/complete` - タスク完了
- `POST /api/tasks/{id}/start` - タスク開始
- `POST /api/tasks/{id}/cancel` - タスクキャンセル
- `GET /api/tasks/user/{userId}` - ユーザーのタスク取得（オプション: `?status=NOT_STARTED|IN_PROGRESS|COMPLETED|CANCELLED`）

### レポート

- **テストレポート**: `build/reports/tests/test/index.html`
- **カバレッジレポート**: `build/reports/jacoco/test/html/index.html`
- **Ktlintレポート**: `build/reports/ktlint/`

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
    │               │       │   ├── CreateUserInput.kt
    │               │       │   ├── UpdateUserInput.kt
    │               │       │   └── UserOutput.kt
    │               │       │
    │               │       └── task/
    │               │           ├── CreateTaskInput.kt
    │               │           ├── UpdateTaskInput.kt
    │               │           ├── CompleteTaskInput.kt
    │               │           └── TaskOutput.kt
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
    │                   │   │   ├── CreateUserRequest.kt      # toInput()拡張関数を含む
    │                   │   │   ├── UpdateUserRequest.kt      # toInput()拡張関数を含む
    │                   │   │   └── UserResponse.kt           # from()ファクトリ関数を含む
    │                   │   │
    │                   │   └── task/
    │                   │       ├── CreateTaskRequest.kt      # toInput()拡張関数を含む
    │                   │       ├── UpdateTaskRequest.kt      # toInput()拡張関数を含む
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