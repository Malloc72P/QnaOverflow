package scra.qnaboard.dto.page;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 페이지네이션을 위한 DTO.
 * @param <T> 페이지의 아이템의 타입.(ex: 질문목록조회라면 T는 QuestionSummaryDTO가 된다.)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Paging<T> {
    //초기화 되지 않은 필드의 초기값
    private static final int NOT_INITIALIZED = -1;
    //기본 블록사이즈. 페이지네이션이 (1,2,3,4,5)와 같이 생겼다면, 블록의 크기는 5가 된다.
    private static final int DEFAULT_BLOCK_SIZE = 10;
    //검색창의 입력값.
    private String searchInput = "";
    //검색된 행의 개수
    private long recordCount = 0L;
    //이전 블록의 마지막 페이지
    private int lastPageOfPreviousBlock = NOT_INITIALIZED;
    //다음 블록의 첫 페이지
    private int firstPageOfNextBlock = NOT_INITIALIZED;
    //현재 페이지 번호
    private int currentPageNumber = NOT_INITIALIZED;
    //전체 페이지 개수
    private int totalPage = NOT_INITIALIZED;
    //현재 블록의 페이지 목록
    private List<Integer> pageNumbers = new ArrayList<>();
    //현재 페이지의 컨텐츠
    private List<T> content = new ArrayList<>();

    /**
     * Spring Data의 Page객체를 가지고 DTO를 생성해서 반환함
     * 검색어가 없는 경우 해당 메서드를 사용하면 됨
     */
    public static <T> Paging<T> buildPaging(Page<T> page) {
        return buildPaging(page, "");
    }

    /**
     * Spring Data의 Page객체를 가지고 DTO를 생성해서 반환함
     */
    public static <T> Paging<T> buildPaging(Page<T> page, String searchInput) {
        //DTO 생성
        Paging<T> newPaging = new Paging<>();
        //페이지의 크기를 설정함
        int pageSize = page.getSize();
        //현재 몇번 블록인지 계산함
        int blockNumber = page.getNumber() / DEFAULT_BLOCK_SIZE;
        //현재 블록의 시작 페이지와 마지막 페이지를 계산함.(0번 블록이고 블록크기가 5이면 0 ~ 4가 된다)
        int pageStart = blockNumber * DEFAULT_BLOCK_SIZE;
        int pageEnd = pageStart + DEFAULT_BLOCK_SIZE - 1;
        //마지막 페이지가 전체 페이지의 개수를 넘어가지 않게 한다.
        if (pageEnd >= page.getTotalPages()) {
            pageEnd = page.getTotalPages() - 1;
        }
        //현재 블록의 페이지 리스트를 생성함
        newPaging.pageNumbers = IntStream.rangeClosed(pageStart, pageEnd)
                .boxed()
                .collect(Collectors.toList());
        //현재 페이지의 컨텐츠를 설정함
        newPaging.content = page.getContent();
        //그 외 페이지네이션에 필요한 변수값을 설정함
        newPaging.firstPageOfNextBlock = pageEnd + 1;
        newPaging.lastPageOfPreviousBlock = pageStart - 1;
        newPaging.currentPageNumber = page.getNumber();
        newPaging.totalPage = page.getTotalPages();
        newPaging.searchInput = searchInput;
        newPaging.recordCount = page.getTotalElements();
        return newPaging;
    }

    /**
     * 이전 블록이 있는지 여부를 반환함(타임리프에서 사용함)
     */
    public boolean hasPreviousBlock() {
        return getLastPageOfPreviousBlock() >= 0;
    }

    /**
     * 다음 블록이 있는지 여부를 반환함(타임리프에서 사용함)
     */
    public boolean hasNextBlock() {
        return firstPageOfNextBlock < totalPage;
    }
}
