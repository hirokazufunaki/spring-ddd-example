# CLAUDE.md

このファイルは、このリポジトリでコードを操作する際のClaude Code (claude.ai/code) へのガイダンスを提供します。

## 言語設定

**重要**: このプロジェクトでは、原則として日本語でドキュメントを生成してください。コメント、README、設計書などすべてのドキュメントは日本語で作成します。

## プロジェクト概要

ドメイン駆動設計（DDD）とヘキサゴナルアーキテクチャの原則を使用したシンプルなタスク管理サービスAPIのSpring Bootアプリケーションです。ユーザーとタスクの基本的なCRUD操作を管理します。

## 開発コマンド

### ビルドとテスト
- `./gradlew build` - プロジェクト全体をビルド
- `./gradlew test` - すべてのテストを実行
- `./gradlew bootRun` - アプリケーションをローカルで実行
- `./gradlew clean` - ビルド成果物をクリーン

### 単体テスト実行
- `./gradlew test --tests "ClassName"` - 特定のテストクラスを実行
- `./gradlew test --tests "ClassName.methodName"` - 特定のテストメソッドを実行

### カバレッジレポート
- `./gradlew jacocoTestReport` - JaCoCoカバレッジレポートを生成
- `./gradlew jacocoTestCoverageVerification` - カバレッジ検証を実行
- `./gradlew check` - テスト実行とカバレッジ検証を含む品質チェック
- レポート場所: `build/reports/jacoco/test/html/index.html`

### コード品質
- `./gradlew ktlintCheck` - Ktlintによるコードスタイルチェック
- `./gradlew ktlintFormat` - Ktlintによるコード自動フォーマット
- `./gradlew ktlintApplyToIdea` - IntelliJ IDEAにKtlint設定を適用
- Ktlintレポート場所: `build/reports/ktlint/`

## アーキテクチャ概要

コードベースは関心の分離を厳密に行う階層化されたヘキサゴナルアーキテクチャに従います：

### ドメイン層 (`domain/`)
- **集約**: `User`と`Task` - 独自の境界を持つコアビジネスエンティティ
- **値オブジェクト**: `UserId`, `UserName`, `Email`, `TaskId`, `TaskName`, `TaskStatus` - 不変なドメイン概念
- **リポジトリインターフェース**: 実装詳細なしでデータ永続化の契約を定義
- **共有カーネル**: 共通のドメイン例外とユーティリティ

### アプリケーション層 (`application/`)
- **アプリケーションサービス**: ユースケースを統制 (`UserApplicationService`, `TaskApplicationService`)
- **DTO**: 入出力用のCommandとResultオブジェクト (`CreateUserCommand`, `UserResult`など)
- **ビジネスロジックなし** - ドメインオブジェクトに委譲

### インフラストラクチャ層 (`infrastructure/`)
- **JPA永続化**: エンティティマッピング、リポジトリ実装、アダプター
- **マッパー**: ドメインオブジェクトとJPAエンティティ間の変換
- **アダプター**: Spring Data JPAを使用してドメインリポジトリインターフェースを実装

### プレゼンテーション層 (`presentation/`)
- **RESTコントローラー**: ユーザーとタスク用のHTTPエンドポイント
- **リクエスト/レスポンスDTO**: 変換メソッド付きのWebレイヤーデータ契約
- **例外処理**: 一貫したエラーレスポンスのためのグローバル例外ハンドラー

## 主要パターン

### パッケージ構造
- 各集約（User、Task）はすべての層で独自のパッケージ構造を持つ
- ドメインでインターフェース、インフラストラクチャで実装を行うリポジトリパターン
- 層間のオブジェクト変換用のマッパーパターン

### データフロー
1. **RESTリクエスト** → **コントローラー** → **アプリケーションサービス** → **ドメインリポジトリインターフェース** → **リポジトリアダプター** → **JPAリポジトリ**
2. ドメインオブジェクトはインフラストラクチャ境界でのみJPAエンティティと相互変換される
3. プレゼンテーションDTOは変換用の拡張関数（`toCommand()`）とファクトリ関数（`from()`）を持つ

## 重要な規約

- Kotlin with Spring Boot 3.5.6とJava 21を使用
- すべてのドメインロジックは集約ルートと値オブジェクトに配置
- リポジトリインターフェースはドメイン層で定義し、インフラストラクチャで実装
- コマンドは内向きに、結果は外向きに層を通って流れる
- JPAエンティティはドメインエンティティとは分離し、明示的なマッピングを行う

## パッケージ構造

プロジェクトのパッケージは `com.example.springdddexample` をベースとしています：

```
com.example.springdddexample/
├── domain/
│   ├── model/
│   │   ├── user/          # User集約
│   │   │   ├── User.kt
│   │   │   ├── UserId.kt
│   │   │   ├── UserName.kt
│   │   │   ├── Email.kt
│   │   │   └── UserRepository.kt
│   │   └── task/          # Task集約
│   │   │   ├── Task.kt
│   │   │   ├── TaskId.kt
│   │   │   ├── TaskName.kt
│   │   │   ├── TaskStatus.kt
│   │   │   └── TaskRepository.kt
│   └── shared/            # 共有カーネル
│       └── DomainException.kt
├── application/
│   ├── service/
│   │   ├── user/          # ユーザーアプリケーションサービス
│   │   │   └── UserApplicationService.kt
│   │   └── task/          # タスクアプリケーションサービス
│   │       └── TaskApplicationService.kt
│   └── dto/
│       ├── user/          # ユーザーコマンド・結果DTO
│       │   ├── CreateUserInput.kt
│       │   ├── UpdateUserInput.kt
│       │   └── UserOutput.kt
│       └── task/          # タスクコマンド・結果DTO
│           ├── CreateTaskInput.kt
│           ├── UpdateTaskInput.kt
│           ├── CompleteTaskInput.kt
│           └── TaskOutput.kt
├── infrastructure/
│   └── persistence/jpa/
│       ├── entity/
│       │   ├── user/
│       │   │   └── UserJpaEntity.kt
│       │   └── task/
│       │       └── TaskJpaEntity.kt
│       ├── repository/
│       │   ├── user/
│       │   │   └── UserJpaRepository.kt
│       │   └── task/
│       │       └── TaskJpaRepository.kt
│       ├── adapter/
│       │   ├── user/
│       │   │   └── UserRepositoryAdapter.kt
│       │   └── task/
│       │       └── TaskRepositoryAdapter.kt
│       ├── mapper/
│       │   ├── user/
│       │   │   └── UserMapper.kt
│       │   └── task/
│       │       └── TaskMapper.kt
│       └── config/
│           └── JpaConfig.kt
└── presentation/
    ├── rest/
    │   ├── user/          # ユーザーRESTコントローラー
    │   │   └── UserController.kt
    │   └── task/          # タスクRESTコントローラー
    │       └── TaskController.kt
    ├── dto/
    │   ├── user/          # ユーザーリクエスト・レスポンスDTO
    │   │   ├── CreateUserRequest.kt
    │   │   ├── UpdateUserRequest.kt
    │   │   └── UserResponse.kt
    │   └── task/          # タスクリクエスト・レスポンスDTO
    │       ├── CreateTaskRequest.kt
    │       ├── UpdateTaskRequest.kt
    │       ├── TaskResponse.kt
    │       └── TaskListResponse.kt
    ├── exception/
    │   ├── GlobalExceptionHandler.kt
    │   └── ErrorResponse.kt
    └── config/
        ├── WebConfig.kt
        └── OpenApiConfig.kt
```

## Task 集約の実装内容

### Task ドメイン層
- **Task.kt**: 集約ルート。タスク情報とステート遷移を管理
  - メソッド: `complete()`, `start()`, `cancel()`, `updateTask()`, `updateName()`, `updateDescription()`
  - ビジネスルール: 状態遷移の検証（完了済みから進行中への遷移は不可など）
- **TaskId.kt**: ULID形式のタスクID（26文字の不変値オブジェクト）
- **TaskName.kt**: 1～255文字のタスク名（不変値オブジェクト）
- **TaskStatus.kt**: タスク状態列挙型
  - 値: `NOT_STARTED`（未着手）, `IN_PROGRESS`（進行中）, `COMPLETED`（完了）, `CANCELLED`（キャンセル）
- **TaskRepository.kt**: リポジトリインターフェース
  - メソッド: `save()`, `findById()`, `findByUserId()`, `findByUserIdAndStatus()`, `findAll()`, `delete()`, `deleteByUserId()`, `existsById()`

### Task アプリケーション層
- **TaskApplicationService.kt**: ユースケース統制
  - メソッド: `createTask()`, `updateTask()`, `getTask()`, `getAllTasks()`, `getTasksByUserId()`, `getTasksByUserIdAndStatus()`, `completeTask()`, `startTask()`, `cancelTask()`, `deleteTask()`, `deleteTasksByUserId()`
- **DTOクラス**:
  - `CreateTaskInput`: タスク作成コマンド
  - `UpdateTaskInput`: タスク更新コマンド
  - `CompleteTaskInput`: タスク完了コマンド
  - `TaskOutput`: タスク出力DTO

### Task インフラストラクチャ層
- **TaskJpaEntity.kt**: JPA永続化エンティティ
- **TaskStatusEntity.kt**: JPA用の状態列挙型
- **TaskJpaRepository.kt**: Spring Data JPA リポジトリインターフェース
- **TaskRepositoryAdapter.kt**: ドメインリポジトリの実装
- **TaskMapper.kt**: ドメインオブジェクト⇔JPAエンティティ間の変換

### Task プレゼンテーション層
- **TaskController.kt**: REST API エンドポイント（10個）
  - 基本CRUD: `POST /api/tasks`, `GET /api/tasks/{id}`, `PUT /api/tasks/{id}`, `DELETE /api/tasks/{id}`
  - リスト取得: `GET /api/tasks`, `GET /api/tasks/user/{userId}` (ステータスフィルタリング対応)
  - アクション: `POST /api/tasks/{id}/complete`, `POST /api/tasks/{id}/start`, `POST /api/tasks/{id}/cancel`
- **リクエスト/レスポンスDTO**:
  - `CreateTaskRequest`: バリデーション付きリクエスト
  - `UpdateTaskRequest`: バリデーション付きリクエスト
  - `TaskResponse`: レスポンスDTO
  - `TaskListResponse`: タスク一覧レスポンスDTO