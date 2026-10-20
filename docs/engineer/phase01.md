# Phase 01. 대규모 트래픽 시스템 구축 로드맵

## 1. 목표

시나브로 한정판 굿즈 판매 시나리오를 기반으로 대규모 트래픽 환경에서 정합성, 처리량, 응답시간을 보장하는 시스템을 설계하고 검증한다.

최소한의 인프라로 시작하여 병목을 발견하고 문제 해결에 필요한 기술을 단계적으로 도입한다.

최종 목표는 **100,000명의 구매 요청을 10초 동안 처리하면서 정확히 1,000건의 주문을 성공시키고 모든 사용자에게 10초 이내 최종 결과를 표시하는 것**이다.

### 핵심 원칙

1. 처음부터 복잡한 분산 아키텍처를 도입하지 않는다.
2. 실제 문제가 확인되었을 때 개선한다.
3. 각 단계에서 하나의 주요 설계 변경을 적용한다.
4. 변경 전후를 동일한 조건으로 비교한다.
5. 처리량뿐 아니라 정합성과 응답시간을 함께 검증한다.
6. 모든 실험은 Local Docker 환경에서 진행한다.
7. 목표를 충족하는 구성 중 리소스 사용량이 가장 적은 구성을 선택한다.

---

## 2. 실험 환경

### 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Kotlin |
| Framework | Spring Boot |
| ORM | JPA / Hibernate |
| Database | MySQL 8 |
| Cache | Redis (필요 시 도입) |
| Message Broker | Kafka (필요 시 도입) |
| Load Balancer | Nginx |
| Container | Docker / Docker Compose |
| Load Test | k6 |
| Monitoring | Prometheus / Grafana |
| Payment | Local Payment Mock |

### 환경 제약

- Cloud 환경을 사용하지 않는다.
- 모든 서비스는 Docker Container로 실행한다.
- 인프라 확장은 Docker Compose 기반으로 진행한다.
- 컨테이너별 CPU 및 Memory 제한을 설정한다.
- 모든 단계에서 동일한 리소스 조건으로 비교한다.
- 부하 발생기는 별도 프로세스 또는 컨테이너로 실행하고 리소스를 별도 집계한다.
- 결제는 정상 승인과 고정된 지연시간을 제공하는 Mock으로 대체한다.
- 각 실험 후 DB와 Redis 상태를 초기화한다.

### Local 환경에서의 비용 기준

클라우드 요금 대신 리소스 사용량을 비용의 대체 지표로 활용한다.

- CPU 사용량 및 CPU 사용 시간
- Memory 사용량 및 점유 시간
- Disk 사용량 및 I/O
- Network I/O
- 컨테이너 수
- 전체 실험 수행 시간

각 Stage에서 실행하는 컨테이너의 CPU·Memory 제한과 호스트 장비 사양을 기록한다.

---

## 3. 공통 부하 조건

### 구매 요청

| Level | 구매자 | 지속 시간 | 최초 요청 RPS |
|---|---:|---:|---:|
| 1 | 1,000명 | 10초 | 100 |
| 2 | 10,000명 | 10초 | 1,000 |
| 3 | 100,000명 | 10초 | 10,000 |

- 초기 재고는 1,000개로 고정한다.
- 사용자당 구매 가능한 수량은 1개다.
- 모든 구매자는 서로 다른 사용자 ID를 가진다.
- 각 사용자는 최초 구매 요청을 1회 전송한다.
- 응답 지연에 따른 재시도는 최초 요청과 별도로 측정한다.
- 동일 구매 의도의 재시도는 최초 처리 기한을 연장하지 않는다.
- 재고 조회 트래픽은 별도로 측정한 뒤 통합 부하 테스트를 진행한다.

#### 성공 조건

- Level 1에서는 1,000명 모두 성공
- Level 2에서는 1,000명 성공 / 9,000명 실패
- Level 3에서는 1,000명 성공 / 99,000명 실패

### 공통 성공 조건

- 초과 판매 0건
- 중복 결제 0건
- 사용자당 중복 성공 주문 0건
- 재고 음수 0건
- 결제 승인·확정 주문·판매 수량 일치
- 모든 구매 의도의 최종 상태 확정
- 서버 수신 후 9초 이내 최종 확정
- 최종 확정 후 1초 이내 클라이언트 표시
- 사용자 요청 후 10초 이내 최종 결과 표시

Level 3에서는 추가로 다음을 검증한다.

- 확정 주문 정확히 1,000건
- 성공 구매자 정확히 1,000명
- 최종 실패 구매자 정확히 99,000명
- 결제 승인 정확히 1,000건
- 남은 재고 0개

---

# 4. 단계별 구축 계획

## Stage 0. 실험 기반 구축

**구성**
- Application 1
- MySQL 1
- Payment Mock 1
- k6
- Monitoring

### 목표

동일한 조건에서 반복 가능한 부하 실험 환경을 구축한다.

### 구현

- 상품, 재고, 주문, 결제 데이터 모델 정의
- 주문 API 구현
- 재고 조회 API 구현
- 구매 결과 조회 API 구현
- Payment Mock 구현
- k6 테스트 시나리오 작성
- Prometheus / Grafana 모니터링 환경 구성
- 초기 데이터 생성 및 초기화 스크립트 작성

### 검증

- 100,000명의 사용자 데이터를 생성할 수 있는가?
- 설정한 RPS를 정확하게 발생시킬 수 있는가?
- DB와 애플리케이션 지표를 수집할 수 있는가?
- 사용자별 최종 결과와 처리시간을 측정할 수 있는가?
- 결제 Mock이 정해진 지연시간을 제공하는가?

### 결과물

- Docker Compose 실행 환경
- k6 부하 테스트 스크립트
- 모니터링 대시보드
- 초기 성능 측정 결과

---

## Stage 1. Single Application + No Explicit Lock

**구성**
- Application 1
- MySQL 1
- 명시적 Lock 없음
- Redis / Kafka 없음

### 목표

가장 단순한 구조에서 동시성 문제를 재현하고 처리 성능을 확인한다.

### 구현

1. 재고 조회
2. 재고 존재 여부 확인
3. 재고 차감
4. 결제 승인
5. 주문 확정

JPA 엔티티를 조회하고 수량을 변경하는 기본적인 Read-Modify-Write 방식을 사용한다.

### 검증

- 동시 요청에서 Lost Update가 발생하는가?
- 실제 성공 주문 수와 재고 차감 수량이 일치하는가?
- 초과 판매가 발생하는가?
- 동일 사용자의 중복 요청이 처리되는가?
- 처리량이 증가하면서 응답시간은 어떻게 변화하는가?

### 핵심 질문

> 별도의 동시성 제어 없이 단일 Application과 MySQL만으로 재고 정합성을 보장할 수 있는가?

### 완료 기준

정합성 문제와 성능 측정 결과를 기록한다.

이 단계는 의도적인 문제 재현 단계이므로 전체 성공 조건을 충족하지 않아도 된다.

---

## Stage 2. Inventory Concurrency Control

**구성**
- Application 1
- MySQL 1
- DB 동시성 제어

### 목표

재고 초과 판매를 방지하고 동시성 제어 방식별 성능을 비교한다.

### 실험 대상

**2-1. Pessimistic Lock**

- SELECT FOR UPDATE
- 재고 행에 대한 배타적 잠금

**2-2. Optimistic Lock**

- JPA @Version
- 버전 충돌 감지
- 충돌 시 재시도 정책

**2-3. Atomic Update**

```sql
UPDATE inventory
SET quantity = quantity - 1
WHERE product_id = ?
  AND quantity > 0;
```

- 조건부 UPDATE
- 영향받은 행 수로 재고 확보 여부 판단

### 검증

- 초과 판매가 발생하는가?
- 재고가 음수가 되는가?
- Lock Wait Time은 얼마인가?
- Deadlock이 발생하는가?
- 재시도 횟수는 얼마인가?
- 동시성 제어 방식별 TPS와 응답시간 차이는 얼마인가?

### 핵심 질문

> 동일한 재고에 대한 높은 경쟁 상황에서 어떤 동시성 제어 방식이 가장 효율적인가?

### 완료 기준

- 초과 판매 0건
- 재고 음수 0건
- 재고 차감 정합성 확보
- 세 가지 방식의 성능 비교 완료

이후 단계에서는 가장 적절한 방식을 선택한다.

---

## Stage 3. Idempotency & Purchase Consistency

**구성**
- Application 1
- MySQL 1
- 재고 동시성 제어
- 멱등성 처리

### 목표

동일 사용자의 재시도로 인한 중복 주문과 중복 결제를 방지한다.

### 구현

- 사용자·이벤트 단위 구매 의도 식별
- Idempotency Key 처리
- DB Unique Constraint 적용
- 구매 처리 상태 관리
- 기존 요청 처리 결과 재사용
- 구매 결과 조회 API

구매 상태 예시:

```text
PENDING
RESERVED
PAYMENT_APPROVED
CONFIRMED
FAILED
```

주문·결제의 중복 방지와 재고 확보·주문 확정 간 트랜잭션 경계를 정의한다.

### 검증

- 동일 사용자가 동시에 10번 요청하면?
- 요청 처리 중 재시도하면?
- 결제가 승인된 이후 동일 요청을 전송하면?
- 페이지를 새로고침하면?
- 동일 구매 의도에 대해 최종 결과가 달라질 수 있는가?

### 핵심 질문

> At-Least-Once 요청 환경에서도 사용자에게 하나의 구매 결과만 보장할 수 있는가?

### 완료 기준

- 중복 성공 주문 0건
- 중복 결제 승인 0건
- 기존 구매 결과 조회 가능
- 동일 구매의 최종 상태 일관성 확보

---

## Stage 4. Single Server Optimization

**구성**
- Application 1
- MySQL 1
- Redis / Kafka 없음

### 목표

추가 인프라 없이 단일 Application과 MySQL의 처리 성능을 최대한 개선한다.

### 실험 대상

**Application**
- Thread Pool
- DB Connection Pool
- JVM Heap / GC
- 불필요한 객체 생성
- Transaction 범위

**Database**
- Index
- Slow Query
- SQL 실행 횟수
- Lock Wait
- Connection 수
- 불필요한 조회 제거
- Batch 처리 가능성

### 검증

- Application CPU가 병목인가?
- MySQL CPU가 병목인가?
- Connection Pool 대기가 발생하는가?
- 특정 재고 Row에서 요청이 직렬화되는가?
- 트랜잭션 시간이 증가하는 원인은 무엇인가?
- 최적화 전후 최대 처리량은 얼마인가?

### 핵심 질문

> 새로운 인프라를 도입하지 않고 얼마나 많은 구매 요청을 처리할 수 있는가?

### 완료 기준

- 주요 병목 확인
- 개선 전후 성능 비교
- 단일 서버 최대 처리량 측정
- 정합성 유지 검증

---

## Stage 5. Inventory Read Optimization

**구성**
- Application 1
- MySQL 1
- Application Local Cache
- 필요 시 Redis 비교 실험

### 목표

재고 조회 부하가 실제 구매 트랜잭션에 영향을 주지 않도록 한다.

### 구현

- 재고 조회 결과 캐싱
- 1초 주기 재고 수량 갱신
- 조회 결과와 실제 구매 가능 여부 분리
- 품절 확정 이후 신규 구매 요청의 빠른 실패 처리

### 실험

1. 모든 재고 조회를 MySQL에서 처리
2. Application Local Cache 적용
3. 필요 시 Redis Cache와 비교

### 검증

- 재고 조회 RPS가 증가하면 구매 TPS가 감소하는가?
- DB Read 부하가 얼마나 감소하는가?
- 캐시 데이터가 얼마나 오래된 상태로 표시되는가?
- 품절 이후 DB 접근을 얼마나 줄일 수 있는가?

### 핵심 질문

> 재고 조회와 구매 처리를 분리하면 실제 구매 성능이 얼마나 향상되는가?

### 완료 기준

- 재고 조회 1초 주기 충족
- 구매 정합성 유지
- 조회 최적화 전후 성능 비교

---

## Stage 6. Application Scale Out

**구성**
- Application 2 → 4
- MySQL 1
- Nginx 1

### 목표

애플리케이션 수평 확장을 통해 처리량과 응답시간을 개선한다.

### 실험

1. Application 1
2. Application 2 + Nginx
3. Application 4 + Nginx

### 검증

- 인스턴스 증가에 따라 TPS가 증가하는가?
- 각 인스턴스에 트래픽이 균등하게 분산되는가?
- MySQL Connection 수가 증가하는가?
- DB Lock 경합이 심화되는가?
- 기존 멱등성 처리가 다중 인스턴스에서도 유지되는가?
- Local Cache 사용 시 재고 조회 결과가 허용 범위를 충족하는가?

### 핵심 질문

> Application을 확장하면 처리량이 계속 증가하는가, 아니면 DB가 새로운 병목이 되는가?

### 완료 기준

- 인스턴스별 처리량 비교
- Scale Out 효율 측정
- DB 병목 여부 확인
- 다중 인스턴스 정합성 검증

---

## Stage 7. Redis-based Inventory Reservation

**구성**
- Application N
- MySQL 1
- Redis 1
- Nginx 1

### 목표

단일 재고 Row에 집중되는 경쟁을 줄이고 재고 확보 처리를 분산한다.

### 구현

- Redis를 통한 재고 선점
- Lua Script 기반 원자적 처리
- 사용자당 1회 선점 제한
- 선점 성공자만 주문·결제 진행
- Redis 선점 상태와 MySQL 확정 상태의 일치 검증

### 실험

1. MySQL Atomic Update
2. Redis Atomic Reservation

### 검증

- 재고 선점 TPS는 얼마인가?
- MySQL에 도달하는 요청 수가 감소하는가?
- Redis CPU와 Memory 사용량은 얼마인가?
- Redis 처리 지연이 발생하는가?
- 정확히 1,000명만 재고를 확보하는가?
- Redis와 DB의 최종 상태가 일치하는가?

### 핵심 질문

> Redis를 통한 재고 선점이 MySQL의 Lock 경합보다 실제로 효율적인가?

### 완료 기준

- 재고 선점 최대 1,000명
- 중복 선점 0건
- DB 최종 정합성 확보
- Redis 도입 전후 성능 비교

**주의:** Redis 선점과 MySQL 주문 확정은 서로 다른 저장소에서 수행되므로 원자적 트랜잭션으로 묶이지 않는다. 선점 상태의 영속화, 재처리, 복구 정책을 설계해야 한다. 첫 실험은 서버·Redis 장애가 없는 조건으로 제한하고 장애 복구는 후속 시나리오에서 검증한다.

---

## Stage 8. Asynchronous Purchase Processing

**구성**
- Application N
- MySQL 1
- Redis 1
- Worker N
- Nginx 1
- 필요 시 Message Broker

### 목표

구매 요청 수신과 실제 주문·결제 처리를 분리하여 순간적인 대규모 요청을 처리한다.

### 구현

- 구매 요청 수신
- 재고 선점
- 구매 작업 큐 등록
- Worker에서 주문·결제 처리
- 최종 결과 저장
- 클라이언트에 처리 결과 전달

### 실험 대상

**8-1. DB 기반 작업 큐**
- MySQL을 이용한 비동기 작업 처리

**8-2. Message Broker 기반 큐**
- Kafka 등을 통한 비동기 작업 처리
- 필요성이 확인된 경우에만 도입

### 결과 전달 방식

- HTTP 동기 응답
- Polling
- Server-Sent Events (SSE)

각 방식에서 서버 부하와 실제 사용자 결과 표시시간을 비교한다.

### 검증

- 요청 급증 시 큐 대기시간은 얼마인가?
- Worker 수를 증가시키면 처리량이 증가하는가?
- 사용자별 최종 결과가 10초 안에 표시되는가?
- 동일 작업의 중복 실행이 발생하는가?
- 결과 전달이 실패해도 구매 상태가 유지되는가?
- 비동기화 전후 리소스 사용량은 어떻게 변하는가?

### 핵심 질문

> 동기 처리에서 비동기 처리로 전환하면 전체 구매 완료시간과 시스템 효율이 실제로 개선되는가?

### 완료 기준

- 정확히 1,000건 주문 확정
- 중복 결제 0건
- 사용자별 처리 기한 충족
- 최종 결과 표시 기한 충족
- 기존 동기 방식과 성능 비교 완료

---

## Stage 9. Resource & Architecture Optimization

### 목표

모든 요구사항을 충족한 구성 중 가장 적은 리소스를 사용하는 아키텍처를 선정한다.

### 비교 대상

- 단일 Application + MySQL
- 최적화된 단일 Application + MySQL
- 다중 Application + MySQL
- 다중 Application + MySQL + Redis
- 다중 Application + MySQL + Redis + Worker
- 필요 시 Kafka를 추가한 구성

### 평가 항목

| 항목 | 기준 |
|---|---|
| 정합성 | 모든 요구사항 충족 |
| 처리량 | 목표 10,000 RPS 수용 |
| 서버 확정 시간 | 최대 9초 |
| 결과 전달 시간 | 최대 1초 |
| 사용자 전체 소요시간 | 최대 10초 |
| CPU | 사용량과 사용 시간 |
| Memory | 사용량과 점유 시간 |
| Disk | 저장소 및 I/O |
| Network | 데이터 전송량 |
| 복잡도 | 서비스 수 및 운영 요소 |

### 핵심 질문

> 요구사항을 만족시키기 위해 정말 Redis와 Kafka, 다중 Application이 필요한가?

### 완료 기준

- 최종 아키텍처 선정
- 모든 요구사항 검증
- 다른 구성 대비 장단점 정리
- 리소스 사용량 비교
- 병목 및 기술 선택 근거 문서화

---

# 5. 공통 성능 측정 지표

모든 Stage는 동일한 지표로 비교한다.

## 5.1. Application

- Request RPS
- Throughput
- Response Time (P50 / P95 / P99 / Max)
- HTTP Error Rate
- CPU Usage
- Memory Usage
- JVM GC
- Thread Pool
- DB Connection Pool

## 5.2. Database

- Queries Per Second
- Transactions Per Second
- Active Connections
- Row Lock Wait
- Deadlock
- Slow Query
- CPU Usage
- Memory Usage
- Disk I/O

## 5.3. Business

- 최초 구매 요청 수
- 재시도 요청 수
- 성공 구매자 수
- 실패 구매자 수
- 품절 실패 수
- 구매 제한 실패 수
- 주문 확정 수
- 결제 승인 수
- 재고 잔량
- 중복 주문 수
- 중복 결제 수
- 미확정 구매 수

## 5.4. User Experience

- 최초 요청부터 최종 확정까지 소요시간
- 최종 확정부터 화면 표시까지 소요시간
- 최초 요청부터 화면 표시까지 소요시간
- 9초 / 10초 초과 요청 수
- 결과 중복 표시 건수
- 새로고침 이후 결과 조회 성공 여부

서버 API 성능 측정과 클라이언트 화면 표시 측정은 별도로 수행한다. k6 HTTP 테스트만으로는 실제 화면 표시시간을 증명할 수 없으므로 테스트 클라이언트 또는 브라우저 기반 검증을 추가한다.

---

# 6. 실험 결과 기록 템플릿

각 Stage마다 동일한 형태로 기록한다.

## Stage N. 실험명

### 1. Hypothesis

무엇을 개선할 것으로 예상하는가?

### 2. Architecture

- Application:
- MySQL:
- Redis:
- Kafka:
- Worker:
- Load Balancer:

### 3. Environment

- Host CPU / Memory:
- Container CPU / Memory Limit:
- JVM Configuration:
- MySQL Configuration:
- Payment Mock Latency:
- Load Profile:

### 4. Result

| 지표 | 측정 결과 |
|---|---|
| 최초 요청 RPS | |
| 처리량 | |
| P50 | |
| P95 | |
| P99 | |
| Max Latency | |
| 성공 주문 수 | |
| 최종 실패 구매자 수 | |
| 중복 결제 | |
| 초과 판매 | |
| 10초 초과 구매 수 | |
| CPU Usage | |
| Memory Usage | |

### 5. Bottleneck

어떤 병목이 발견되었는가?

### 6. Root Cause

병목의 근본 원인은 무엇인가?

### 7. Improvement

어떤 기술적 변경으로 개선할 것인가?

### 8. Conclusion

- 가설 검증 결과
- 개선 효과
- 새롭게 발견한 문제
- 다음 Stage 진행 필요성

---

# 7. 최종 결과물

Phase 01 완료 시 다음 결과물을 확보한다.

1. 단계별 아키텍처 구성도
2. 단계별 부하 테스트 결과
3. 동시성 제어 방식별 성능 비교
4. 단일 서버와 다중 서버 확장 효율 분석
5. Redis 도입 전후 성능 비교
6. 동기·비동기 주문 처리 비교
7. 사용자 결과 전달 방식 비교
8. Local 리소스 사용량 비교
9. 최종 아키텍처 및 기술 의사결정 근거
10. 다음 장애 시나리오에서 검증할 한계와 위험 목록

## 최종 성공 조건

```text
[Traffic]
최초 구매 요청       = 100,000
부하 지속 시간       = 10초
평균 최초 요청 RPS   = 10,000

[Consistency]
확정 주문 수         = 1,000
성공 구매자 수       = 1,000
최종 실패 구매자 수  = 99,000
결제 승인 수         = 1,000
남은 재고            = 0
초과 판매            = 0
중복 결제            = 0
중복 성공 주문       = 0

[Latency]
서버 확정 시간       <= 9초
결과 표시 지연       <= 1초
전체 사용자 대기시간 <= 10초
10초 초과 구매       = 0
기한 초과 미확정 구매 = 0

[Environment]
Execution           = Local
Container           = Docker
Cloud               = None

[Optimization]
정합성과 시간 조건을 만족하는
아키텍처 중 최소 리소스 구성 선정
```

---

## 8. 진행 원칙

**Stage의 완료와 요구사항 달성은 구분한다.**

문제 재현을 목적으로 하는 Stage에서는 실패 자체가 의미 있는 실험 결과가 될 수 있다.

반면 최종 아키텍처는 모든 비즈니스 정합성과 사용자 응답시간 조건을 반드시 통과해야 한다.

모든 개선에는 측정 가능한 근거가 있어야 한다.

**복잡한 시스템을 만드는 것이 목적이 아니라 필요한 복잡도만으로 대규모 트래픽을 안정적으로 처리하는 시스템을 만드는 것이 목적이다.**
