package com.projectdemo.basic.common.util;

/**
 * This class is used combined with {@link JcrQueryHelper}.
 *
 * @author khoi.tran (2015-Aug-06)
 */
public class QueryPageOffset implements QueryOffset {
    /**
     * It's a long number because this class may be used in a very large storage.
     */
    private Long _pageOffset;
    /**
     * This number should be int only. It should not be long because you should never get a huge number of items in a single page.
     * That's the reason why you need paging.
     */
    private Integer _limitSize;
    //TODO I just keep this field to compatible with old code. Actually, we should not put this field here. It's not belong to paging logic.
    private String _status;

    public QueryPageOffset(Integer pageSize) {
        construct(0L, pageSize, null);
    }

    public QueryPageOffset(Long pageOffset, Integer pageSize) {
        construct(pageOffset, pageSize, null);
    }

    //TODO I just keep this constructor because of compatible with other code. Should refactor it in the future.
    public QueryPageOffset(Integer pageOffset, Integer pageSize, String status) {
        Long longQueryOffset = null;
        if (pageOffset != null) {
            longQueryOffset = pageOffset.longValue();
        }
        construct(longQueryOffset, pageSize, status);
    }

    private void construct(Long pageOffset, Integer pageSize, String status) {
        _pageOffset = pageOffset;
        _limitSize = pageSize;
        _status = status;
    }

    /**
     * @return the index of asset. Note: this is different from index of page.
     */
    @Override
    public Long getItemOffset() {
        Long result;
        if (_pageOffset != null) {
            if (_limitSize == null) {
                result = _pageOffset;
            } else {
                result = _pageOffset * _limitSize;
            }
        } else {
            result = null;
        }
        return result;
    }

    public Long getPageOffset() {
        return _pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        _pageOffset = pageOffset;
    }

    @Override
    public Integer getLimitSize() {
        return _limitSize;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public void setLimitSize(Integer limitSize) {
        _limitSize = limitSize;
    }
}
