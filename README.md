

# habit_app_back

シンプルな「習慣トラッカー」バックエンド（学習用）。  
Java/Spring Boot と PostgreSQL（Docker）で半年以内に MVP 完成を目指します。

---

## ゴール（半年以内）
- ユーザー登録/ログイン（JWT）
- 習慣のCRUD（作成・取得・更新・削除）
- 1日1回のチェックイン（重複は不可）
- 統一エラー応答（ProblemDetail）と OpenAPI ドキュメント
- Docker で DB 起動、最小デプロイ可能

## スタック
- Java 17 / Spring Boot 3.x
- Spring Data JPA / Bean Validation
- Spring Security（JWT）
- PostgreSQL（Docker / `docker-compose-intel.yml` を使用）
- Flyway（DBマイグレーション）
- springdoc-openapi（/swagger-ui）

## 必要要件
- Docker / Docker Compose
- JDK 17
- Maven（`mvn` もしくは `./mvnw`）

---

## クイックスタート

### 1) データベースを起動（Docker）
> 既存の `docker-compose-intel.yml` を使用します。
```bash
docker compose -f docker-compose-intel.yml up -d db
````

デフォルト（compose）のDB接続情報：

* host: `localhost`
* port: `15432`
* db: `app`
* user: `app`
* pass: `app`

### 2) アプリ設定（application.yml の例）

`src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:15432/app
    username: app
    password: app
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true

server:
  port: 8080

# 開発向けロギング（必要に応じて調整）
logging:
  level:
    root: INFO
    org.springframework.web: INFO
```

### 3) 起動

```bash
# どちらか
mvn spring-boot:run
# or
./mvnw spring-boot:run
```

### 4) 動作確認

* ヘルスチェック: `http://localhost:8080/actuator/health`
* OpenAPI（導入後）: `http://localhost:8080/swagger-ui/index.html`

---

## 予定API（スケッチ）

* `POST /api/auth/signup` … ユーザー作成
* `POST /api/auth/login` … JWT発行
* `GET  /api/habits` … 自分の習慣一覧
* `POST /api/habits` … 習慣作成
* `PUT  /api/habits/{id}` / `DELETE /api/habits/{id}`
* `POST /api/checkins` … 1日1回の記録（同一日ユニーク）

---

## ディレクトリ方針（最小案）

```
src/
  main/
    java/…/habit/
      controller/   # 薄く（入出力/バリデのみ）
      service/      # 業務ロジックの中心
      repository/
      domain/       # Entity/値オブジェクト
      dto/          # View/入出力用DTO
      config/       # Security / OpenAPI / Logging
      exception/    # @ControllerAdvice & ProblemDetail
    resources/
      db/migration/ # Flyway: V1__init.sql など
      application.yml
```

---

## 開発ポリシー（短く）

* Controller は薄く、ロジックは Service に集約
* DTO は View 層でマッピング（Entity を外に出さない）
* 例外は `@ControllerAdvice` + `ProblemDetail` で統一（機密情報を出さない）
* Principal は **typed ID（UUID/Long）** で扱う（username文字列のパースは避ける）
* ログは **traceId（MDC）** を出力、外部I/Oは所要時間ログ
* OpenAPI を常に最新化（springdoc）

---

## よくある操作

```bash
# ビルド
mvn -B -DskipTests clean package

# テスト
mvn test

# DBだけ再起動
docker compose -f docker-compose-intel.yml restart db

# ログ確認（DB）
docker compose -f docker-compose-intel.yml logs -f db
```

---

## トラブルシュート（超要約）

* **DBに繋がらない**: compose のポート（15432）・資格情報（app/app）・`application.yml` のURLを確認
* **ポート使用中**: `lsof -i:8080`（WSL）/ `netstat -ano | findstr :8080`（Windows）でプロセスを停止
* **マイグレーション失敗**: `src/main/resources/db/migration/` のファイル名（`V1__*.sql`）とSQLを確認

---

## ライセンス / クレジット

* 学習個人プロジェクト（必要に応じて追記）

## TODO（ざっくり）

* [ ] Spring Initializr で雛形作成
* [ ] Flyway `V1__init.sql` を作成（users/habits/checkins）
* [ ] `/api/habits` の最小 GET/POST
* [ ] ProblemDetail と OpenAPI
* [ ] JWT 認証

```

必要なら、このまま **V1__init.sql** の最小DDLや、**初回の Controller/Service/Repository 雛形**も続けて用意するよ。
```
