## 게시판 서비스

간단한 게시글 CRUD와 사용자 회원가입을 제공하는 Spring Boot 기반 애플리케이션입니다. 도메인 단위로 `domain / application / infrastructure / interface` 계층을 분리한 DDD 스타일 구조를 적용했습니다.

## 기술 스택

- Kotlin 1.9
- Spring Boot 3.5 (Web, Security, Validation, Data JPA)
- MariaDB 11 (Docker Compose 제공)
- Gradle Kotlin DSL

## 디렉터리 구조

```
src/main/kotlin/com/kisahy/board
├── global               # 공통 설정, 예외 처리
├── post                 # 게시글 도메인 (domain/application/infrastructure/interface)
└── user                 # 사용자 도메인 (domain/application/infrastructure/interface)
```

## 선행 조건

- JDK 17 이상
- Docker & Docker Compose (선택: 로컬 DB 실행용)

## 로컬 실행

1. (선택) 데이터베이스 컨테이너 실행
   ```bash
   docker compose up -d
   ```
2. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

## 주요 기능

- 게시글 생성/수정/삭제 (`/api/posts`)
- 사용자 회원가입 (`/api/users/signup`)
- 비밀번호/아이디 정책 검증 및 중복 체크

## 테스트

```bash
./gradlew test
```

## 추가 참고

- `docker-compose.yml`은 `board` 데이터베이스를 가진 MariaDB 컨테이너를 3307 포트로 노출합니다.
- `local-resources/` 디렉터리에 데이터 퍼시스턴스를 위한 볼륨이 사전 매핑되어 있습니다.

## 의사결정 기록

- [0001 - 비밀번호 해싱 알고리즘 선택](docs/adr/0001-password-hashing.md)
