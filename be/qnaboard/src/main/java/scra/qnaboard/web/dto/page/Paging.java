package scra.qnaboard.web.dto.page;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class Paging<T> {
    private static final int defaultBlockSize = 5;
    private static final int NOT_INITIALIZED = -1;

    private int lastPageOfPreviousBlock = NOT_INITIALIZED;
    private int firstPageOfNextBlock = NOT_INITIALIZED;
    private int currentPageNumber = NOT_INITIALIZED;
    private int totalPage = NOT_INITIALIZED;
    private List<Integer> pageNumbers = new ArrayList<>();
    private List<T> content = new ArrayList<>();

    private Paging() {
    }

    public static <T> Paging<T> buildPaging(Page<T> page) {
        Paging<T> newPaging = new Paging<>();
        int pageSize = page.getSize();
        int blockNumber = page.getNumber() / pageSize;
        int pageStart = pageSize * blockNumber;
        int pageEnd = pageStart + defaultBlockSize - 1;

        if (pageEnd >= page.getTotalPages()) {
            pageEnd = page.getTotalPages() - 1;
        }

        newPaging.pageNumbers = IntStream.rangeClosed(pageStart, pageEnd)
                .boxed()
                .collect(Collectors.toList());

        newPaging.content = page.getContent();

        newPaging.firstPageOfNextBlock = pageEnd + 1;
        newPaging.lastPageOfPreviousBlock = pageStart - 1;
        newPaging.currentPageNumber = page.getNumber();
        newPaging.totalPage = page.getTotalPages();
        return newPaging;
    }

    public boolean hasPreviousBlock() {
        return getLastPageOfPreviousBlock() >= 0;
    }

    public boolean hasNextBlock() {
        return firstPageOfNextBlock < totalPage;
    }
}
