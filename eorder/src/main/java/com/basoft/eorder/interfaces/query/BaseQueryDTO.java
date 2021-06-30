package com.basoft.eorder.interfaces.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseQueryDTO {
    private Integer page;

    private Integer size;

    private Integer start;

    @Override
    public String toString() {
        return "BaseQueryDTO{" +
                ", page=" + page +
                ", size=" + size +
                ", start=" + start +
                '}';
    }
}
