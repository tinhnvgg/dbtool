package io.github.vatisteve.data_access;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pagination {

    private long totalElement;
    private int page;
    private int size;

    public int getTotalPage() {
        return size <= 0 ? 0 : (int) Math.ceil((double) totalElement/size);
    }

}
