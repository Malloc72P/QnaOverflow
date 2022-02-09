# QnaBoard

> ** Q&A 게시판 프로젝트**
>
> demo : https://qnaoverflow.dase.me

- Stack Overflow를 따라서 만든 질문게시판 서비스입니다 😁
- 질문글을 작성하고 답변글을 작성하는 기능과, 각각의 글에 댓글을 다는 기능을 구현했습니다.
- 질문글의 제목, 답변글 개수, 태그, 추천점수를 사용해서 질문글을 검색할 수 있는 기능도 구현했습니다.

### 1. 제작기간 & 참여인원

<hr/>


- 2021-12-13 ~ 2022-02-11
- 개인 프로젝트



### 2. 사용기술

<hr/>


🔨**Backend**

| 기술명          | Version |
| --------------- | ------- |
| Springboot      | 2.6.1   |
| Gradle          | 7.3.1   |
| Spring Data JPA | 2.6.0   |
| QueryDSL        | 5.0.0   |
| H2              | 1.4.200 |
| MySQL           | 8.0.27  |
| Thymeleaf       | 3.0.12  |

**🎸 기타**

| 기술명  | Version |
| ------- | ------- |
| Jenkins | 2.325   |



### 3. 엔티티 및 ERD 설계

<hr/>

#### 3.1 👓기능 분석

**회원 기능**

- 회원가입 회원탈퇴, 로그인
- 내가 작성한 질문 및 답변 조회

**질문 기능**

- 질문 작성, 수정, 삭제
- 질문 조회
- 검색필터를 사용한 질문 검색
  - 글 제목. 검색태그, 답변존재여부, 질문작성일, 투표점수범위등을 활용한 검색
- 질문이 유용한지에 대한 투표 기능

**답변 기능**

- 답변 작성, 수정, 삭제
- 답변이 유용한지에 대한 투표 기능

**검색태그 기능**

- 질문게시판 관리자에 의한 검색태그 추가, 수정, 삭제
- 검색태그 조회

**대댓글 기능**

- 질문 및 답변에 댓글 작성
- 댓글 수정, 삭제
- 댓글에 댓글을 다는 '대댓글' 기능 구현



#### 3.2 🛠엔티티 설계

![](https://i.imgur.com/MxrEXji.png)

**회원(Member)**

- 이름과 역할(관리자, 일반사용자)을 가집니다

**질문(Question)**

- 질문글은 태그를 사용하는 검색을 지원하기 위해서 태그정보를 가지고 있습니다.
- 질문글은 답변글과 일대다 관계를 맺고 있습니다.
  연관관계의 주인은 외래키를 가지게 될 답변게시글로 선택했습니다.

**질문글에 달린 태그(QuestionTag)**

- 질문글(Question)과 태그(Tag)는 다대다 관계를 가진다.
- @ManyToMany를 사용하기보다 OneToMany, ManyToOne으로 풀어서 표현하는 방법을 선택했다.

**태그(Tag)**

- 질문글과 태그는 다대다 관계를 맺고 있습니다.
  다대다 관계를 표현하기 위해서 중간에 QuestionTag라는 엔티티를 추가했습니다.

- 태그는 작성자와 태그이름, 태그 색상을 가집니다

**답변(Answer)**

- 질문글에 대한 답변에 해당하는 엔티티입니다

**게시글(Post)**

- 질문글(Question)과 답변글(Answer)의 부모클래스입니다
- 두 엔티티의 공통기능인 댓글기능은 Post엔티티에서 제공합니다
  상속을 활용한 덕분에, 같은 기능을 중복해서 구현하는 문제를 피할 수 있었습니다.



#### 3.3 🧰ERD 설계

![](https://i.imgur.com/SIyhe2i.png)

**싱글테이블 전략**

- 답변글(Answer)과 질문글(Question) 엔티티는 하나의 테이블(Post)로 관리하도록 설계했다
- 엔티티에서 사용하지 않는 컬럼에는 null을 허용해야 하고, 테이블이 지나치게 커질 수 있긴 하지만, insert할 때 하나의 테이블에만 insert하면 되고, 조회할때도 join없이 필요한 데이터를 가져올 수 있다는 장점이 있어서 싱글테이블 전략을 사용했다

**인덱스 적용**

- 나중에 성능최적화하면서 인덱스도 사용하면, 이 부분의 내용도 채우자



### 4. 핵심기능

<hr/>


#### 4.1 전체흐름

- 전체흐름을 표현하는 흐름도

#### 4.2 게시글 검색 및 페이지네이션

- 리액트 화면에서 검색조건 넣는 부분

#### 4.3 대댓글 기능

- 효율적으로 대댓글을 가져오기 위한 고민에 대해 작성



### 5. 핵심 기능을 위한 고민

<hr/>


#### 5.1 복잡한 동적쿼리와 페이징 처리

- QueryDSL 사용
  - 컴파일 시점에 쿼리 생성부분에 문제가 없는지 체크해서, 잘못 작성된 JPQL로 인해 런타임에 예외가 발생하는 문제를 최소화 하였음


#### 5.2 N + 1 문제

- `default_batch_fetch_size: 100`설정을 추가해서 1번의 쿼리로 가져온 N개의 엔티티에 대해 단건조회가 나가지 않고,
   in절을 사용해서 최적화된 쿼리가 나갈 수 있도록 함
- 엔티티를 가져오는 경우, 필요한 범위의 연관관계를 미리 파악하고, 패치조인으로 연관관계를 세팅하도록 하였음

#### 5.3 효율적인 테스트

- 테스트 코드는 H2 데이터베이스를 사용해서 테스트함
  - 덕분에 별도로 디비를 설치하지 않아도 메모리상에서 H2 디비를 띄워서 테스트할 수 있었음
  - H2 데이터베이스를 MySQL모드로 실행시켜서 최대한 실제 환경과 비슷하게 조성하였음
- 테스트메서드에서 데이터 초기화 로직을 간소화해서, 테스트 코드를 작성하는데 걸리는 시간을 줄였음
  - 테스트를 위한 테스트 데이터를 생성하는 유틸 클래스 구현함
  - `TestDataInit`로 테스트 데이터를 초기화하고, `TestDataDTO`에 담아서 반환하도록 구현함
  - 테스트 메서드는 `TestDataDTO`를 받아서 정해진 로직대로 테스트를 수행함
  - 테스트 데이터에서 특정 데이터를 찾아서 반환하는 편의성 메서드도 만들어서 테스트를 최대한 편하게 할 수 있도록 함
    adminMember(), anotherMember()

#### 5.4 대댓글 불러오기

- 계층구조를 가지고 있는 댓글을 디비에서 어떻게 가져올지가 문제였음
- 댓글 엔티티에 ManyToOne 관계로 부모 게시글과 댓글을 추가하였음
- 댓글을 가져올 때 in절을 사용해서 특정 게시글을 부모로 가지고 있는 댓글을 디비에서 한번에 퍼오도록 쿼리를 작성하였음
- 댓글의 계층구조를 세팅하는건 WAS에서 담당하도록 구현하였음
  최대한 효율적으로 세팅할 수 있도록 Map 자료구조를 활용하였음.
  상세코드는 아래와 같음

#### 5.5 대댓글을 정상적으로 가져오는지 테스트

- 대댓글이 계층구조를 가지고 있어서 테스트하는게 쉽지 않았음
- 넓이 우선 탐색 알고리즘을  사용해서 대댓글의 계층구조를 탐색하도록 하였음.
  탐색할때마다 해당 댓글의 자식 댓글이 디비와 같은 상태인지 비교하도록 테스트 코드를 작성하였음
- 상세코드는 아래와 같음

### 6. 그 외의 고민

<hr/>

#### 6.1 상속을 통해 대댓글 기능 재활용

- 질문글과 답변글은 공통점이 많지만 다른 부분이 있어서 같은 엔티티로 처리할 수 없었다
- 어떻게 할까 고민하다가 두 객체의 공통된 부분을 추출해서 게시글(Post)이라는 엔티티를 만들고 상속을 사용해서 공통부분을 재활용할 수 있었다
- 특히 대댓글 기능을 게시글 클래스에서 구현하고 질문글과 답변글 엔티티에게 상속해줘서 중복구현 없이 기능을 재활용할 수 있었다

#### 6.? Post/Redirection/Get 패턴 적용

- Form을 통한 Post  요청에 대해 PRG 패턴 적용함
- 사용자가 새로고침할 때 마다 요청하는 것을 막기 위해서 적용하였음

#### 6.? 페이지 새로고침 최소화

- 질문글 상세보기 페이지는 많은 쿼리를 발생시킴. 해당 페이지를 요청하는 일을 최소화하고 싶었음
- 답변글을 작성하거나 댓글을 작성할때마다 질문글 상세보기 페이지를 재요청하는건 너무 비효율적이라고 생각하였음
- 질문글 상세보기 페이지에서 새로고침 없이 답변을 달거나 댓글을 달 수 있도록, 답변글과 댓글은 API로 구현하였음. 

#### 6.? 메세지 기능

- 메세지 소스를 사용해서 뷰에 들어가는 문자열을 관리하였음
- 덕분에 국제화 기능을 넣을 때 추가구현 없이 properties파일 하나만 추가하는걸로 해결할 수 있었음

#### 6.? 예외처리

- ControllerAdvice를 사용한 예외처리 부분이 전체적으로 어케 생겼는지 소개

#### 6.? 개발환경 구축

- 로컬, 개발서버, 운영서버마다 별도의 설정파일로 관리한다

  ```
  src/main/resources
            |--application.yml
            |--application-local.yml
            |--application-dev.yml
            |--application-prod.yml
  				
  ```

- 로컬환경의 MySQL 데이터베이스는 docker-compose를 사용해서 편하게 구축한다
  개발서버 및 운영서버의 데이터베이스는 AWS의 RDS를 사용해서 구축한다

- 스프링의 ddl-auto: create 설정은 로컬환경에서만 사용하도록 한다

### 7. 배포

<hr/>

#### 7.1 ⚙배포환경

![](https://i.imgur.com/doM8mIi.png)

- 로컬환경에서 도커를 설치하고, 그 안에 젠킨스를 설치하여 배포를 진행했다.
- 젠킨스에서 빌드 및 테스트를 하고 jar파일을 S3에 업로드 한 다음, CodeDeploy에 배포요청을 해서 대상 EC2 인스턴스에서 앱을 실행할 수 있도록 설정했다.
- 데이터베이스의 주소 및 계정정보와 같은 민감한 내용을 담고 있는 설정파일은 Git 리포지토리에 올라가지 않도록 하고, 젠킨스에서 없는 설정파일을 따로 주입해서 빌드하도록 설정했다.
- 배포가 끝나서 EC2에서 앱을 실행시킬 때, 어떤 프로필로 동작할지를 명시해주었다.





