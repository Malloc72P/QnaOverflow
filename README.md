# 🏹QnaOverflow

> ** Q&A 게시판 프로젝트**
> 
> demo : https://qnaoverflow.dase.me  
> 데모 사이트는 크롬 브라우저에 최적화되어있습니다.  
> 프로젝트 설치방법 : [설치방법 링크](https://github.com/Malloc72P/QnaOverflow/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EC%B9%98-%EB%B0%8F-%EC%8B%A4%ED%96%89%EB%B0%A9%EB%B2%95)  
> 프로젝트 자동배포방법 : [자동배포방법 링크](https://github.com/Malloc72P/QnaOverflow/wiki/%EC%9E%90%EB%8F%99%EB%B0%B0%ED%8F%AC-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)  

# 1. 프로젝트 소개
### 1.1 개요

- 스택 오버플로우의 UI와 기능을 사용자 요구사항으로 가정하고 만든 질문게시판 서비스입니다 😁
- 질문글을 작성해서 궁금한 내용을 다른 사람들에게 물어보고, 다른 사람의 질문글에 대한 답변글을 작성할 수 있으며, 질문글과 답변글에 댓글을 달아서 자신의 의견을 공유할 수 있습니다.
- 많은 질문글 속에서 찾고자 하는 질문글만 찾을 수 있도록, 제목, 답변글 개수, 태그, 추천점수를 사용해서 질문글을 검색할 수 있습니다.

### 1.2 주요 기술 목표

- 객체지향 설계의 주요 목표

  - 공통기능을 추출하여 코드 재사용성을 향상시키기
  - 다형성을 활용하여 하나의 메세지로 객체마다 고유한 방식으로 동작할 수 있게 하기

- 데이터베이스 설계의 주요 목표

  - 무결성 제약조건을 사용해서 데이터 일관성을 지키기
  - 인덱스를 사용하여 조회쿼리의 성능 향상시키기

- 스프링 프레임워크 사용의 주요 목표

  - 복잡한 동적쿼리를 생성하는 코드의 복잡도를 완화하기 위해 QueryDSL 적용하기
  - 사용자 입력 검증을 위해 Bean Validation 기술 적용하기
  - 페이지 새로고침으로 인해 의도하지 않은 Post요청을 재요청하지 않도록 PRG 패턴 적용하기
  - ControllerAdvice를 사용해서 컨트롤러에서 예외처리 코드를 최대한 분리하기

- 배포 및 운영의 주요 목표

  - 서비스 수동배포로 인한 시간낭비를 피하기 위해 자동배포기술 적용하기

  - Let's Encrypt로 SSL 인증서를 받고 HTTPS를 적용하기


### 1.3 제작기간 & 참여인원
- **2021-12-13 ~ 2022-02-11**
- **개인 프로젝트**

### 1.4 사용기술 & 개발환경

🗼**Frontend**

| 기술명          | Version |
| --------------- | ------- |
| Thymeleaf       | 3.0.12  |


🛰**Backend**

| 기술명          | Version |
| --------------- | ------- |
| Springboot      | 2.6.1   |
| Gradle          | 7.3.1   |
| Spring Data JPA | 2.6.0   |
| QueryDSL        | 5.0.0   |
| H2              | 1.4.200 |
| MySQL           | 8.0.27  |
| Thymeleaf       | 3.0.12  |

**🎸 CI/CD**

| 기술명  | Version |
| ------- | ------- |
| Jenkins | 2.325   |
| AWS S3  |         |
| AWS CodeDeploy |  |


**🔨개발환경**
| 도구 이름       | Version    |
| ----------------| ---------- |
| IntelliJ IDEA   | 2021.2.1   |

# 2. 요구사항 분석

**회원 기능**

- 회원가입 회원탈퇴, 로그인

**질문 기능**

- 질문 작성, 수정, 삭제
- 질문 조회
- 검색필터를 사용한 질문 검색
  - 글 제목. 태그, 답변글 개수, 작성자 아이디, 투표점수를 활용한 질문글 검색
- 질문이 유용한지에 대한 추천, 비추천 기능

**답변 기능**

- 답변 작성, 수정, 삭제
- 답변이 유용한지에 대한 투표 기능

**검색태그 기능**

- 질문게시판 관리자에 의한 검색태그 추가, 수정, 삭제
- 검색태그 조회

**대댓글 기능**

- 질문 및 답변글에 댓글 작성
- 댓글 수정, 삭제
- 댓글에 댓글을 다는 '대댓글' 기능 구현

# 3. 클래스 설계

![](https://i.imgur.com/Z8O5MGa.png)

**회원(Member) 클래스**

- 회원 클래스는 이름과 역할(관리자, 일반사용자)을 가집니다.

**질문(Question) 클래스**

- 질문글은 제목(title)과 내용(content)을 가지고 있습니다.
- 질문글은 태그를 사용하는 검색을 지원하기 위해서 태그정보를 가지고 있습니다.
- 질문글은 답변글과 일대다 관계를 맺고 있습니다.
  연관관계의 주인은 외래키를 가지게 될 답변게시글로 선택했습니다.

**답변(Answer)**

- 질문글에 대한 답변글에 해당하는 클래스입니다

**게시글(Post)**

- 질문글(Question)과 답변글(Answer)의 부모 클래스입니다
- 두 클래스의 공통기능인 댓글기능과 삭제기능은, Post클래스에서 제공합니다.
  **상속**을 활용한 덕분에, 같은 기능을 중복해서 구현하는 문제를 피할 수 있었습니다.

**질문글에 달린 태그(QuestionTag)**

- 질문글과 태그는 다대다 관계를 맺고 있습니다.  
  다대다 관계를 일대다, 다대일 관계로 만들기 위해서, 중간에 QuestionTag라는 클래스를 추가했습니다.

**태그(Tag)**

- 태그 클래스는 작성자와 태그이름, 태그에 대한 설명을 가지고 있습니다.

# 4. 데이터베이스 설계

![](https://i.imgur.com/98KifCB.png)

### 4.1 데이터베이스 설계에 대한 주요 고민

**싱글테이블 전략**

- 답변글(Answer)과 질문글(Question)은 하나의 테이블(Post)로 관리하도록 설계했습니다.
- 엔티티에서 사용하지 않는 컬럼에는 null을 허용해야 하고, 테이블이 지나치게 커질 수 있긴 하지만, 
  insert할 때 하나의 테이블에만 insert하면 되고, 조회할때도 join없이 필요한 데이터를 가져올 수 있다는 장점이 있어서 싱글테이블 전략을 사용했습니다.

**기본키 제약조건** 

- 테이블의 레코드를 고유하게 식별할 수 있는 컬럼인 아이디에 기본키 제약조건을 부여했습니다.
- PK를 부여하여 아이디 컬럼은 중복이 될 수 없고, NULL값이 들어갈 수 없도록 하였습니다.
- 테이블을 JOIN할 때 기본키를 사용하도록 해서 JOIN성능을 향상시켰습니다.

**인덱스 추가생성**

- 질문글 검색쿼리의 Where절과 Order by 절에서 사용하는 컬럼인 post_type(게시글유형)과 createdDate(생성일)에 인덱스를 생성하여, 조회 성능을 향상시켰습니다.
  - `create index idx_type_and_date on post (post_type, created_date);`
- 인덱스 추가 전, 질문목록조회 쿼리의 수행시간  
  ![](https://i.imgur.com/uD2PrI9.png)
- 인덱스 적용 후, 질문목록조회 쿼리의 수행시간  
  ![](https://i.imgur.com/rqAhM4q.png)

# 5. JPA 활용

### 5.1 **게시글 설계(상속)의 패러다임 불일치 해결** 

- 객체에는 상속이라는 개념이 있지만, 관계형 데이터베이스엔 상속이라는 개념이 없다보니, 패러다임 불일치 문제가 발생합니다. 이 문제를 해결하기 위해 JPA의 상속관계 매핑을  사용하였습니다  

  ```java
  @Entity
  @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
  @DiscriminatorColumn(name = "post_type")
  public abstract class Post extends BaseTimeEntity {/*...*/}
  
  @Entity
  public class Answer extends Post {/*...*/}
      
  @Entity
  public class Question extends Post {/*...*/}
  ```

- 위와 같이 `@Inheritance`를 사용하여 상속관계를 매핑할 수 있었습니다.

- 게시글(Post) 테이블의 레코드가 Question타입인지 Answer타입인지를 표현하기 위해, 구분 컬럼인 `post_type`을 사용했습니다.

### 5.2 모든 연관관계는 지연로딩으로 설정

```java
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
	/*...생략*/
    @ManyToOne(fetch = FetchType.LAZY)//연관관계를 지연로딩으로 설정
    @JoinColumn(name = "author_id")
    private Member author;
	/*...생략*/
}

//댓글과 작성자 엔티티가 둘 다 필요한 경우, 패치조인을 사용해서 한번의 쿼리로 조회한다.
public Optional<Comment> commentWithAuthor(long commentId) {
    Comment findComment = queryFactory.select(comment)
        .from(comment)
        .innerJoin(comment.author, member).fetchJoin()
        .where(comment.id.eq(commentId))
        .fetchOne();
    return Optional.ofNullable(findComment);
}
```

- 엔티티를 조회할 때마다 연관된 엔티티를 항상 전부 가져오지 않도록, **지연로딩**을 사용했습니다.

- 연관된 엔티티가 필요할때만 **패치조인**등을 활용해서 최적화된 쿼리로 필요한 데이터를 최소한의 쿼리로 가져올 수 있도록 구현했습니다.  

- 그 외의 경우, 지연로딩을 하더라도 필요한 엔티티를 한번에 조회할 수 있도록 아래의 설정을 추가했습니다.  

  ```yaml
  default_batch_fetch_size: 100
  ```

### 5.3 복합키 매핑

- QuestionTag는 question_id와 tag_id를 묶은 복합키를 기본키로 사용하고 있습니다.

- 복합키를 클래스로 매핑하기 위해서`Embeddable`과  `@EmbededId`를 사용했습니다.  

  ```java
  @Getter
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  public class QuestionTagId implements Serializable {
      protected Long questionId;
      protected Long tagId;
      /*...생략*/
  }
  
  @Entity
  public class QuestionTag extends BaseTimeEntity {
      @EmbeddedId
      private QuestionTagId id;
  
      @MapsId("tagId")
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "tag_id")
      private Tag tag;
  
      @MapsId("questionId")
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "question_id")
      private Question question;
      /*...생략*/
  }
  ```

- 또한 `@MapsId`를 사용해서 외래키와 매핑한 연관관계를 기본키에도 매핑할 수 있었습니다.

# 6. 핵심기능


> 이 서비스의 핵심 기능은 질문글 검색기능입니다.  
> 사용자는 검색창에 검색어를 입력하고 엔터를 치기만 하면 끝입니다.  
> 서버는 사용자가 입력한 검색어를 가지고 복잡한 동적쿼리를 만들어서 질문글을 조회합니다.

### 6.1 전체흐름

![](https://i.imgur.com/Cd2SoLr.png)

### 6.2 사용자 요청

![](https://i.imgur.com/4C1sDDp.png)

- **질문글 검색**
  - 사용자는 위의 이미지처럼 검색어를 입력해서 질문글을 검색합니다
  - [직접 검색해보기](https://qnaoverflow.dase.me/questions?searchInput=%22blandit%22+answers%3A2+%5Bdefinition%5D)
- **질문글 검색요청 및 요청 파라미터**
  - 사용자의 브라우저는 GET 요청을 서버로 전송합니다.
  - 사용자가 입력한 검색어는 쿼리스트링으로 URL에 붙어서 서버에 전송됩니다

### 6.3 컨트롤러 계층 - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/4ea1e8260bcce9c27764631df2302db2a80b85c7/be/qnaboard/src/main/java/scra/qnaboard/web/controller/QuestionController.java#L42)

![](https://i.imgur.com/nyQ3uIH.png)

- **요청 처리 1 - 검색어 파싱**
  - 컨트롤러는 검색요청을 받고, 이를 처리하기 위한 작업을 수행합니다
  - 먼저, 사용자가 입력한 검색어를 파싱합니다. 검색어는 한줄짜리 문자열로 구성되어 있기 때문에, 
    파싱을 해서 사용하기 편하게 만들어야 합니다.
  - 검색어 파싱 로직은 `SearchInputParserService`라는 서비스 계층에 위임합니다.
    해당 서비스는 검색어를 파싱하고, 그 결과를 객체에 담아서 반환합니다.
    객체의 타입은 `ParsedSearchQuestionDTO`입니다.
- **요청처리 2 - 질문글 검색**
  - 질문글을 검색하기 위한 로직 처리를 QuestionService라는 서비스 계층에 위임합니다.
- **결과 응답**
  - 타임리프에서 페이지네이션을 구현하는데 필요한 기능을 제공하는 DTO 객체를 생성하고,
    모델에 담아서 뷰에 전달합니다.

### 6.4 서비스 계층

![](https://i.imgur.com/FzV0pZ7.png)

- **검색어 파싱 서비스 : SearchInputParserService** - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/service/SearchInputParserService.java#L12)
  - 정규표현식을 사용해서 사용자가 입력한 검색어를 파싱합니다.
  - 파싱된 검색어는 `ParsedSearchQuestionDTO`타입의 객체로 만들어서 반환합니다.
- **질문글 검색 서비스: QuestionService** - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/service/QuestionService.java#L40)
  - 질문글에 관련된 로직을 처리합니다. 질문글 검색로직도 해당 서비스에서 처리합니다.
  - 페이징 처리를 위해서 파라미터를 사용해 PageRequest객체를 생성하고, 리포지토리 계층을 통해 
    질문글을 조회합니다.

### 6.5 리포지토리 계층

- [질문글을 조회하는 코드 보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/domain/repository/question/QuestionSearchListRepository.java#L41)

![](https://i.imgur.com/HS3rdRL.png)

- **질문글 조회를 위해 복잡한 동적쿼리 생성**

  - QueryDSL의 BooleanBuilder를 사용해서 복잡한 동적쿼리를 생성하도록 구현했습니다.
  - 검색어 DTO인 ParsedSearchQuestionDTO를 가지고, Where절에 조건을 추가할지 여부를 결정합니다.

  [동적으로 Where절 생성하는 부분의 코드](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/domain/repository/question/QuestionBooleanExpressionSupplier.java#L32)

  ```java
  public BooleanBuilder searchQuestions(ParsedSearchQuestionDTO dto) {
  //BooleanBuilder를 사용해서 검색조건을 동적으로 추가함.
  BooleanBuilder booleanBuilder = new BooleanBuilder();
  return booleanBuilder
  	.and(questionIsNotDeleted())
  	.and(questionTitleLike(dto))
  	.and(authorIdLike(dto))
  	.and(answersCountGoe(dto))
  	.and(scoreGoe(dto))
  	.and(tagInRange(dto));
  }
  
  /**
  * 제목이 검색파라미터와 비슷한지 확인하는 조건
  */
  public BooleanExpression questionTitleLike(ParsedSearchQuestionDTO dto) {
      return dto.hasTitle() ? question.title.like("%" + dto.getTitle() + "%") : null;
  }
  /*...생략*/
  ```

  - 만약 검색어 DTO에 제목에 대한 검색어가 없다면, dto.hasTitle()의 결과가 false가 되고, 
    `questionTitleLike`메서드는 null을 반환하게 됩니다.
    그러면 booleanBuilder에 의해 해당 조건은 쿼리에 추가되지 않고 무시됩니다.
    반면, DTO에 제목에 대한 검색어가 있다면 조건이 쿼리에 추가됩니다.
    이러한 방식으로 Where절을 동적으로 생성합니다.
  - 문자열로 JPQL을 
    작성했다면, 상황에 따라 달라지는 Where절을 만드는게 매우 어려웠을 것 같은데, 
    QueryDSL 덕분에 편하게 작성할 수 있었던 것 같습니다.

- **질문글에 달린 태그 조회(N + 1 문제 해결)**  [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/domain/repository/question/QuestionSearchListRepository.java#L70)

  - 질문글과 태그는 N:M 관계로 연관관계를 맺고 있습니다.
    따라서 질문글 목록을 조회할 때 연관된 태그까지 한번에 조회할 수 없었습니다.
    따로 쿼리를 날려서 태그를 조회 해야 했습니다.

  - 그런데, 조회된 질문글 개수만큼 태그를 조회하는 쿼리를 날리면, N + 1 문제가 발생합니다.
    이 문제를 해결하기 위해, In 절을 활용해서 최적화 하는 방법을 선택했습니다.
    아래의 코드는 질문글 목록에 달린 태그를 한번의 쿼리로 조회합니다.

    ```java
    //1. 질문목록 조회( 추가로 질문의 답글개수와 유저 이름을 같이 가져옴 )
    /*...질문목록 조회하는 코드는 생략...*/
    
    //2. 연관된 태그정보 조회쿼리의 In절에서 사용할 ID 컬렉션을 스트림으로 생성한다
    List<Long> questionIds = questions.stream()
    .map(QuestionSummaryDTO::getQuestionId)
    .collect(Collectors.toList());
    
    //3. 질문목록에서 참조하는 태그정보 조회(QuestionTag와 Tag까지 조인해서 가져오되, in 절을 사용해서 최적화함)
    List<QuestionTagDTO> tags = questionTagRepository.questionTagsBy(questionIds);
    
    //4. 태그의 Question ID값을 가지고 Map으로 그룹화 함
    Map<Long, List<QuestionTagDTO>> tagMap = tags.stream()
    .collect(Collectors.groupingBy(QuestionTagDTO::getQuestionId));
    
    //5. 태그정보 입력
    questions.forEach(question -> question.update(tagMap));
    ```

  - 조회된 모든 질문글의 아이디를 List 자료구조에 저장합니다
    이 리스트와 In절을 사용해서 태그를 조회합니다.
    이렇게 되면 질문글에 필요한 모든 태그를 DB로 부터 조회한 상태가 됩니다.

  - 조회한 태그를 알맞은 질문글 객체에 넣어줘야 합니다.
    이 부분을 해결하기 위해, 스트림의 `Collectors.groupingBy()`메서드를 활용했습니다.
    `groupingBy`를 하면 질문글의 아이디를 Key로, 해당 질문글에 연관된 태그List를 Value로 가지는 맵을 만들 수 있습니다. 그 다음은 질문글을 순회하면서 맵에서 태그List를 꺼내서 넣습니다.

  - 이렇게 해서 발생하는 쿼리는 최소한으로 하면서 원하는 기능을 구현할 수 있었습니다.

# 7. 프로젝트 특징

### 7.1 테스트 코드 작성

- **H2 데이터베이스를 사용하여 독립된 테스트 전용 데이터베이스 구축**
  - 데이터베이스를 사용하는 테스트코드를 수행할 때, 데이터베이스는 어떻게 할 지가 문제였습니다.
    운영중인 데이터베이스는 절대 사용해선 안되고, 따로 데이터베이스를 구축하는것도 문제가 있을 것 같았습니다.
  - 방법이 없을까 고민하다가, H2 데이터베이스를 알게 되었습니다.
    테스트코드만을 위한 독립된 인메모리 DB를 만들어줄 수 있는데다가 가벼워서 상당히 유용했습니다.
  - 또한 H2 데이터베이스를 MySQL 호환모드로 실행할 수 있었습니니다. 덕분에 운영환경과  비슷한 상황에서  테스트할 수 있었습니다.

- **단위 테스트와 통합 테스트**   
  [단위테스트 코드 보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/test/java/scra/qnaboard/web/controller/QuestionControllerTest.java#L36)  
  [통합테스트 코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/test/java/scra/qnaboard/web/controller/QuestionControllerIntegrationTest.java#L35)  
  - 단위테스트와 통합테스트 둘 다 작성해서 테스트하였습니다.
  - 컨트롤러와 서비스 계층에 대한 단위테스트를 작성하여, 각 계층에 대한 테스트를 격리해서 할 수 있었습니다. 테스트 격리 덕분에 각 계층에 문제가 없는지를 빠르게 검증할 수 있었습니다.
  - 모든 빈을 올려서 테스트하는 통합테스트도 작성했습니다. 덕분에 운영환경과 유사하게 테스트할 수 있었습니다.


### 7.2 상속을 통해 soft delete 코드 재사용 - [코드보기](https://github.com/Malloc72P/QnaOverflow/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/domain/entity/post/Post.java#L58)

- **공통기능 추출 후 추상 클래스(Post) 생성해서 해결하기**

  - 질문글(Question)과 답변글(Answer)의 공통기능을 추출하여 게시글(Post) 클래스를 만들었습니다.

  - 추출된 공통기능에는 soft delete 기능이 있습니다. 질문글과 답변글 클래스에 삭제 코드를 중복작성하지 않고, 상위클래스인 Post에 작성하고 상속하여, 코드를 재사용할 수 있었습니다.

    ```java
    @Getter
    @Entity
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "post_type")
    public abstract class Post extends BaseTimeEntity {
    	//...생략
    	protected boolean deleted = false;
    	   
         /**
         * 게시글을 삭제함
         */
    	public void delete() {
            deleted = true;
        }
    }
    ```


### 7.3 다형성을 활용한 예외처리 - [코드보기]()

- **다형성을 사용하게 된 계기**

  - 예외마다 상황에 맞는 에러메세지를 만들어서 사용자에게 보여줄 수 있도록 구현하고 싶었습니다.  
    그래야 왜 요청이 실패했는지 등을 사용자에게 알려줄 수 있다고 생각했기 때문입니다.
  - 처음에는 if문이나 switch-case문을 사용해서, 발생한 예외마다 서로 다른 에러메세지를 사용하도록 구현하려고 했습니다. 하지만 이 방법을 사용하면 코드의 가독성도 떨어지고, 무엇보다 새로운 예외를 추가할때마다 분기처리해서 에러메세지를 가져오는 코드를 수정해야 했습니다.
  - 다른 방법이 없나 고민하다가, 객체지향의 다형성을 활용하기로 했습니다.  
    하나의 메세지로, 객체마다 고유한 방식으로 응답할 수 있는 기능인 다형성을 적용하면,  
     중복코드를 최소화하면서 상황에 맞는 에러메세지를 만들 수 있겠다는 생각이 들었습니다.  

- **에러메세지 생성을 위한 인터페이스**

  - [인터페이스 코드보기](https://github.com/Malloc72P/QnaOverflow/blob/b1435a803ccc606cf0ddccf01bf8e81a9af70800/be/qnaboard/src/main/java/scra/qnaboard/service/exception/DescribableException.java#L7)
  - [NoSessionUser 코드보기](https://github.com/Malloc72P/QnaOverflow/blob/b1435a803ccc606cf0ddccf01bf8e81a9af70800/be/qnaboard/src/main/java/scra/qnaboard/configuration/auth/NoSessionUserException.java#L9)

  ```java
  /**
   * 에러페이지의 설명에 들어갈 메세지의 코드를 반환하는 기능을 가지고 있는 인터페이스.
   * 이걸 구현하는 클래스는 에러페이지의 설명에 들어갈 메세지 코드를 반환할 수 있어야 함
   */
  public interface DescribableException {
      String describeMessage();
  }
  
  /**
   * 세션에서 유저DTO를 찾지 못한 경우 발생하는 예외.
   * 로그인되지 않은 상태에서 로그인이 필요한 작업을 요청하는 경우 해당 예외가 발생된다
   */
  public class NoSessionUserException extends RuntimeException 
      implements DescribableException {
  	/*...생략*/
      
      //로그인 한 다음 다시 시도해달라는 메세지코드를 반환함.
      @Override
      public String describeMessage() {
          return "ui.error.page-desc-no-log-in";
      }
  }
  ```

  - `DescribableException`라는 인터페이스를 만들고, 애플리케이션에서 발생시키는 모든 예외 클래스에서 구현하도록 했습니다. 
  - 해당 인터페이스를 구현한 클래스에선, 자신의 예외 상황에 맞는 메세지를 반환하도록 구현했습니다.
  - `NoSessionUserException`에선 로그인 한 다음 다시 시도해달라는 메세지의 코드인 `ui.error.page-desc-no-log-in`를 반환하도록 구현했습니다.

- **인터페이스 구현 및 사용**  - [코드보기](https://github.com/Malloc72P/QnaOverflow/blob/b1435a803ccc606cf0ddccf01bf8e81a9af70800/be/qnaboard/src/main/java/scra/qnaboard/web/exception/GlobalErrorControllerAdvice.java#L103)

  ```java
  private void updateModelByException(Model model, Locale locale,
                                      String titleCode, String reasonCode, 
                                      DescribableException exception) {
      //에러페이지에 필요한 DTO 생성
      ErrorDTO errorDTO = ErrorDTO.builder()
          .title(messageSource.getMessage(titleCode, null, locale))
          .reason(messageSource.getMessage(reasonCode, null, locale))
          .description(messageSource.getMessage(exception.describeMessage(),
                                                null, locale))
          .build();
      //모델에 에러정보를 담은 DTO를 넣는다
      model.addAttribute("error", errorDTO);
  }
  ```

  - 예외처리를 담당하는 `GlobalErrorControllerAdvice`에서는 `updateModelByException`이라는 메서드를 사용해서 `ErrorDTO`를 생성합니다. 
  - `ErrorDTO`에는 왜 에러가 발생했는지에 대한 설명인 `description`이라는 필드가 있습니다. 해당 필드를 채우기 위해, `DescribableException` 인터페이스의 `describeMessage()`메서드를 호출합니다.
  - 애플리케이션 내에서 발생하는 예외 객체들은 `DescribableException`인터페이스를 구현했으므로, 각자의 고유한 방식으로 메세지코드를 생성해서 반환합니다.
  - 반환받은 메세지코드를 가지고 메세지소스에서 에러메세지를 꺼내도록 구현하여, 중복코드나 복잡한 분기처리 없이, 예외 상황에 맞는 에러메세지를 사용자에게 보여줄 수 있었습니다.


### 7.4 Post/Redirection/Get 패턴 적용

- **PRG 패턴 적용을 통해 의도하지 않은 Post요청 방지** 
  [질문글 생성 후 리다이렉트하는 코드](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/web/controller/QuestionController.java#L111)
  - Post 요청에 대한 응답이 페이지인 경우, 브라우저를 새로고침하는 순간, Post요청이 다시 전송되는 문제가 있었습니다. 이렇게 되면 같은 내용의 질문글을 중복해서 생성하게 되므로 막을 방법이 필요했습니다.
  
  - 그래서 PRG 패턴을 적용하였습니다. 페이지를 응답하지 않고, 리다이렉트를 시켰습니다. 리다이렉트 된 페이지에서 새로고침을 해도 Post요청이 아닌 페이지에 대한 Get요청을 날리기 때문에, 중복된 질문글 생성요청을  날리는 문제를 해결할 수 있었습니다.


### 7.5 페이지 새로고침 최소화

- 질문글 상세보기 페이지는 여러 쿼리를 발생시킵니다. 질문글과 관련된 모든 답변글을 불러와야 하고, 각각의 게시글에 달려있는 댓글도 가져와야 합니다. 그런데, 댓글이나 답변글을 작성하거나 수정, 삭제할때마다 질문글 상세보기 페이지를 새로고침해버리면 또 다시 여러 개의 쿼리가 발생하게 됩니다. 
- 질문글 상세보기 페이지의 새로고침 없이, 댓글과 답변글에 대한 생성, 수정, 삭제를 하는게 더 효율적이라는 판단을 했습니다. 그래서 답변글과 댓글 생성 수정 삭제 기능은 API로 개발하였습니다.
  해당 기능을 요청하는 것은 자바스크립트의 fetch()함수를 사용했습니다.
- API 컨트롤러와 자바스크립트 코드는 아래에서 확인할 수 있습니다
  - [API 컨트롤러 코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/web/api/AnswerApiController.java#L26)
  - [자바스크립트 코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/resources/static/js/lib/answer.js#L52)


### 7.6 사용자 입력 검증 및 사용자에게 입력오류 알려주기

- **검증의 필요성**
  - 질문글 생성요청을 할 때, 사용자 입력을 검증해야 했습니다. 그래야 잘못된 값으로 엔티티를 생성하려는 시도를 막을 수 있기 때문입니다.
  - 또한 어디서 오류가 발생했는지를 고객에게 알려줘야 했습니다. 왜 요청이 실패하는지를 알아야 입력을 수정해서 다시 요청할 수 있기 때문입니다.


- **Bean Validation을 사용한 사용자 입력 검증** - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/dto/question/create/CreateQuestionForm.java#L19)

  - 폼 요청에 대해서 DTO를 만들고, 여기에 검증을 위한 애너테이션을 부착했습니다.
  - 엔티티와 같은 도메인 객체를 사용할 수 도 있었지만, 그렇게 구현하지 않았습니다. 생성, 수정 등의 요청마다 검증로직이 달라질 수 있는데다가 다루는 데이터에 차이가 생길 수 도 있으니, DTO를 만들어서 처리하는게 좋다고 생각했기 때문입니다.

- **사용자에게 입력 오류 알리기** - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/resources/templates/question/question-form.html#L23)

  - Bean Validation에 실패하면 `bindingResult.hasErrors()`가 true가 되는 것을 이용해서, 입력에 문제가 있으면 사용자를 입력폼 페이지로 보내도록 구현했습니다.

  - 사용자에게 입력 오류를 알리는 기능은 타임리프를 활용해서 구현했습니다.
    `th:errors`를 사용해서, 오류가 있는 필드에 에러메세지를 랜더링했습니다. 에러메세지는 메세지 소스에서 가져오도록 구현했습니다. 

  - *예시) 질문글 생성폼의 제목필드에 대한 입력오류 처리*

  - ```html
    <!--/*질문 제목*/-->
    <div class="mb-3">
        <label class="form-label" for="title" th:text="#{ui.question.form.input-title}">질문 제목</label>
        <input aria-describedby="titleHelp" class="form-control" id="title" name="title" required
               th:field="*{title}"
               type="text">
        <div class="form-text" id="titleHelp" th:text="#{ui.question.form.input-title-help}">질문의 제목을 적어주세요</div>
        <div class="text-danger" th:errors="*{title}">제목은 6자 이상이어야 합니다</div>
    </div>
    ```


### 7.7 예외처리

- **특정 예외에 대한 처리** - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/web/exception/GlobalErrorControllerAdvice.java#L22)

  ```java
  /**
  * 인증오류에 대한 예외처리
  * 로그인 하지 않고 로그인이 필요한 작업을 요청한 경우에 대해 처리한다
  */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(NoSessionUserException.class)
  public String notLoggedInUser(NoSessionUserException exception, Model model, 
                                Locale locale) {
      updateModelByException(model,
                             locale,
                             "ui.error.page-title-no-log-in",
                             "ui.error.page-reason-no-log-in",
                             exception.descriptionMessageCode());
      return "error/error-page";
  }
  
  private void updateModelByException(Model model, Locale locale,
                                      String titleCode, String reasonCode, 
                                      String descriptionCode) {
      //에러페이지에 필요한 DTO 생성
      ErrorDTO errorDTO = ErrorDTO.builder()
          .title(messageSource.getMessage(titleCode, null, locale))
          .reason(messageSource.getMessage(reasonCode, null, locale))
          .description(messageSource.getMessage(descriptionCode, null, locale))
          .build();
      //모델에 DTO를 넣는다
      model.addAttribute("error", errorDTO);
  }
  ```

  - 컨트롤러에서 발생한 예외를 처리하기 위해서 ControllerAdvice를 사용했습니다
  - 예외가 왜 발생했는지를 사용자에게 알려주기 위해서, 예외가 발생한 상황에 대한 설명을 메세지에 적어두었습니다. 그리고 ExceptionHandler 메서드에서 발생한 예외에 해당하는 메세지를 메세지 소스에서 꺼내와서 모델에 담았습니다. 에러페이지는 모델에서 ErrorDTO를 꺼내서 랜더링하여 사용자에게 현재 상황에 대한 설명을 할 수 있도록 구현했습니다. 아래의 이미지는 로그인 하지 않은 상태로 질문글 생성요청을 했을 때 볼 수 있는 에러페이지입니다.
    ![](https://i.imgur.com/E5S8I0B.png)

- **4xx, 5xx 에러 처리**

  - WAS까지 전파되는 예외를 처리하기 위해 스프링 부트에서 제공하는 기능을 이용했습니다
  - 아래의 이미지와 같이, /template/error밑에 에러페이지를 생성하여 처리했습니다  
    ![](https://i.imgur.com/v3X4Ha2.png)

- **API 예외처리**

  - API 예외처리는 컨트롤러와는 다른 방식으로 처리해야 했습니다. 컨트롤러는 에러페이지를 랜더링해서 응답해주면 됐지만, API의 예외처리는 예외에 대한 내용을 담은 DTO를 만들어서 응답해줘야 했기 때문입니다.
  - 컨트롤러와 API컨트롤러에 ControllerAdvice를 따로 적용하기 위해 패키지를 분리했습니다.
    ![](https://i.imgur.com/4v0zt6R.png)

  - 패키지를 사용해서 사용할 ControllerAdvice를 지정할 수 있었습니다. 
    [코드 보러가기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/web/exception/ApiGlobalErrorControllerAdvice.java#L25)

    ```java
    @RequiredArgsConstructor
    @RestControllerAdvice("scra.qnaboard.web.api")
    public class ApiGlobalErrorControllerAdvice {/*...*/}
    ```

  - 클라이언트에서 에러 응답을 받게 되면 아래의 자바스크립트 코드를 사용해서 사용자에게 어떤 문제가 발생했는지를 보여주도록 구현했습니다 
    [코드 보러가기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/resources/static/js/lib/apiHelper.js#L56)

    ```javascript
    static alertError = (error) => {
        if (error != null && error.description != null && error.description !== "") {
            //서버에서 응답해준 ErrorDTO 안의 description 메세지를 alert을 사용해서 보여줌
            alert(error.description);
        } else {
    	    alert("unknown error");
        }
    };
    ```

  - 로그인하지 않고 질문글 추천기능을 이용하면 예외가 발생하고, 아래와 같이 사용자에게 보여줍니다.
    ![image-20220211225142601](https://i.imgur.com/M15Ynzs.png)

### 7.8 로그인 처리

- **로그인 처리를 위해 세션 사용 & ArgumentResolver 활용** - [코드보기](https://github.com/Malloc72P/QnaBoard/blob/76c4759624f2162745340460390b6882cae2a23e/be/qnaboard/src/main/java/scra/qnaboard/configuration/auth/LoginUserArgumentResolver.java#L20)

  ```java
  //ArgumentResolver에서 사용자DTO를 꺼내는 부분
  @Override
  public Object resolveArgument(MethodParameter parameter,
  ModelAndViewContainer mavContainer,
  NativeWebRequest webRequest,
  WebDataBinderFactory binderFactory) throws Exception {
      //세션에서 사용자 DTO 조회
  	SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
  	//만약 존재하지 않는다면 로그인한 적이 없다는 의미로 예외를 발생시킴
      if (sessionUser == null) {
          throw new NoSessionUserException();
      }
      return sessionUser;
  }
  ```

  - 사용자가 로그인에 성공하면 세션에 사용자 정보를 저장하도록 구현했습니다.

  - 컨트롤러에서 로그인 된 사용자의 정보를 세션에서 꺼내오는 중복코드가 발생하였는데, 이러한 공통 관심사를 대신 해주는 ArgumentResolver를 만들어서 중복을 제거하였습니다. 
  - 컨트롤러의 파라미터에 애너테이션이 달려있으면, 직접 만든 ArgumentResolver가 동작하여 세션에서 사용자 정보를 꺼내고 컨트롤러의 핸들러 메서드에 인자로 넘겨주도록 구현했습니다.

### 7.8 HTTPS 적용

- 웹서버와 웹 클라이언트 사이에 암호화된 채널을 생성하여 전송중인 데이터가 도청되지 않도록 보호하기 위하여 HTTPS를 적용했습니다.
- Certbot을 사용해서 Let's Encrypt SSL 인증서를 발급받아서 사용했습니다.
- 스프링 부트의 톰캣서버 앞단에 Nginx 서버를 두고, Nginx서버에 SSL인증서를 적용하였습니다
```
server {
       listen 80;
       server_name qnaoverflow.dase.me www.qnaoverflow.dase.me;
       return 301 https://qnaoverflow.dase.me$request_uri;   
       # http로 들어오면 https로 redirect 해주는 부분
}
server {
       listen 443 ssl;
server_name qnaoverflow.dase.me www.qnaoverflow.dase.me;
       # Certificate
       ssl_certificate /etc/letsencrypt/live/qnaoverflow.dase.me/fullchain.pem;

       # Private Key
       ssl_certificate_key /etc/letsencrypt/live/qnaoverflow.dase.me/privkey.pem;
       location / {
               proxy_pass http://localhost:8080;
               proxy_set_header Host $host;
               proxy_set_header X-Real-IP $remote_addr;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
               proxy_set_header X-Forwarded-Proto $scheme;
        }
}
```

# 8. 배포 환경

![](https://i.imgur.com/doM8mIi.png)

- **젠킨스 설치와 배포 전 테스트 자동화**

  - 로컬환경에서 도커를 설치하고, 그 안에 젠킨스를 설치하여 자동배포를 구성했습니다.
  - 배포를 시작하면 젠킨스에서 프로젝트의 모든 테스트코드를 실행하도록 했습니다. 131개의 모든 테스트코드를 통과해야만 배포가 가능하기 때문에, 적어도 테스트코드에서 커버해주는 기능은 잘 동작한다는 것이 검증된 상태에서만 배포할 수 있도록 강제할 수 있었던 점이 좋았던 것 같습니다.

- **AWS의 S3와 CodeDeploy를 활용하기**

  - 빌드한  jar파일을 AWS로 업로드하는 일은 S3로 처리했습니다.
  - 업로드가 끝나면 CodeDeploy에 요청을 해서 배포했습니다. CodeDeploy는 미리 작성한 배포스크립트를 실행해서 기존에 동작중인 스프링 애플리케이션을 중지시키고 새로운 jar을 실행합니다.

- **Git에 올라가 있지 않은 설정파일 주입**

  - 데이터베이스의 url 및 아이디와 비밀번호, OAUTH의 ClientID와 Secret과 같은 외부에 노출하면 안되는 설정은 Git에 올라가지 않도록 gitignore에 추가했습니다. 그래서 git clone 하고나서 그대로 빌드하면 필수설정이 없어서 실행되지 않는 문제가 있었습니다.

  - 이 문제를 해결하기 위해서, 젠킨스가 설치된 서버에 설정파일을 따로 업로드했습니다. git clone을 한 다음, 미리 업로드해놓은 설정파일을 프로젝트의 resources 경로에 복사하도록 젠킨스의 배포스크립트를 작성했습니다.

    ```shell
    # git repository에는 없는 설정파일 주입
    cp /var/jenkins_home/workspace/ignored-settings/* /var/jenkins_home/workspace/qnaboard-dev/be/qnaboard/src/main/resources/
    ```

