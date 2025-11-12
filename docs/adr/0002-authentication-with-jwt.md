## 0002 - 인증 방식으로 JWT Access/Refresh 토큰 사용

- 상태: 초안
- 날짜: 2025-11-12

### 배경

사용자 개념을 추가하며 게시글 작성/수정/삭제 시 작성자를 확인하기 위한 인증을 필수 요구사항으로 선택했다. 세션을 도입하거나 JWT 기반 토큰을 도입하는 등의 여러 선택지 중 RESTful API를 제공하면서 SPA 또는 모바일 클라이언트를 지원하기 위한 무상태(stateless)한 인증 흐름을 선택하게 되었다.

### 고려한 옵션

1. **서버 세션 + 쿠키**
   - 장점: Spring Security 기본 전략, 구현 용이.
   - 단점: 서버 확장 시 세션 스티키니스 또는 세션 저장소 필요, 쿠키 기반이라 CORS/CSRF 관리 복잡.
2. **Opaque 토큰 + 중앙 인증 서버**
   - 장점: 토큰 무효화, 롤링이 용이.
   - 단점: 토큰 검증 시마다 중앙 서버 조회가 필요, 운영 복잡도 상승.
3. **JWT Access Token + Refresh Token**
   - 장점: 자체 검증 가능, 무상태 아키텍처와 호환, 다양한 클라이언트에서 사용하기 쉬움.
   - 단점: 토큰 탈취 시 만료 전까지 악용 가능, 유지보수 시 키 로테이션/블랙리스트 전략 필요.

### 결정

Access Token과 Refresh Token을 모두 JWT로 발급하는 방식을 채택한다.

### 근거

- 게시판 API는 여러 클라이언트에서 호출될 가능성이 있어 쿠키/세션보다 헤더 기반 토큰이 편리하다.
- Refresh Token을 두어 Access Token 만료 이후에도 재발급 흐름을 제공, 장기 세션을 유지할 수 있다.
- 서버 확장 시 세션 공유 없이 수평 확장이 가능하다.

### 구현 개요

- 로그인 성공 시 `JwtTokenProvider`가 Access Token과 Refresh Token을 생성한다.
- Access Token은 사용자 ID를 subject로 담고 짧은 만료 시간을 가진다.
- Refresh Token은 더 긴 만료 시간을 갖고 재발급 엔드포인트에서 사용한다.
- `JwtAuthenticationFilter`가 요청 헤더의 Access Token을 검증하고, SecurityContext에 사용자 정보를 로드한다.

### 후속 작업

- Refresh Token 저장소(예: DB, Redis) 및 무효화 전략 확립.
- 토큰 재발급 API 구현 및 만료/재발급 정책 문서화.
- Access Token 탈취 대응(블랙리스트, 디바이스 로그아웃 등) 검토.
- 키 로테이션과 환경별 비밀 관리 프로세스 확정.
