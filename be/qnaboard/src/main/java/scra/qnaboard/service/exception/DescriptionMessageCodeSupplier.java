package scra.qnaboard.service.exception;

/**
 * 에러페이지의 설명에 들어갈 메세지의 코드를 반환하는 기능을 가지고 있는 인터페이스.
 * 이걸 구현하는 클래스는 에러페이지의 설명에 들어갈 메세지 코드를 반환할 수 있어야 함
 */
public interface DescriptionMessageCodeSupplier {

    String descriptionMessageCode();
}
