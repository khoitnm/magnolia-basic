package com.projectdemo.basic.common.util;

/**
 * This class is used combined with {@link JcrQueryHelper}.
 *
 * @author khoi.tran (2015-Aug-06)
 */
public class QueryItemOffset implements QueryOffset {
    /**
     * It's a long number because this class may be used in a very large storage.
     */
    private Long _itemOffset;
    /**
     * This number should be int only. It should not be long because you should never get a huge number of items in a single page.
     * That's the reason why you need paging.
     */
    private Integer _pageSize;

    public QueryItemOffset(Long itemOffset, Integer pageSize) {
        construct(itemOffset, pageSize);
    }

    public QueryItemOffset(Integer itemOffset, Integer pageSize) {
        Long longQueryOffset = null;
        if (itemOffset != null) {
            longQueryOffset = itemOffset.longValue();
        }
        construct(longQueryOffset, pageSize);
    }

    private void construct(Long pageOffset, Integer pageSize) {
        _itemOffset = pageOffset;
        _pageSize = pageSize;
    }

    @Override
    public Long getItemOffset() {
        return _itemOffset;
    }

    public void setItemOffset(Long itemOffset) {
        _itemOffset = itemOffset;
    }

    @Override
    public Integer getLimitSize() {
        return _pageSize;
    }

    public void setPageSize(Integer pageSize) {
        _pageSize = pageSize;
    }
}
