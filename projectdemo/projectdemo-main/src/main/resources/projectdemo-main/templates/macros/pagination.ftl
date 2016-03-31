[#-- Definition: Pagination --]
[#macro pagination pager position]
    [#if pager?has_content && (pager.position?starts_with(position) || pager.position?starts_with("both"))]
        [#if pager.numPages > 1]
            <div class="b-pagination--search-results" data-css="b-pagination">
                <ul class="pagination__list">
                    [#if pager.currentPage > 1]
                        <li class="pagination__list-item is-inactive">
                            <a href="${pager.getPageLink(pager.currentPage -1)}">
                                <strong class="pagination__list-element is-previous">
                                    <span class="is-hidden">${i18n['search.result.pagination.onePage']}</span>${i18n['search.result.pagination.button.previous']}
                                </strong>
                            </a>
                        </li>
                    [#else]
                        <li class="pagination__list-item">
                            <strong class="pagination__list-element is-previous">
                                <span class="is-hidden">${i18n['search.result.pagination.onePage']}</span>${i18n['search.result.pagination.button.previous']}
                            </strong>
                        </li>
                    [/#if]

                    [#if pager.beginIndex > 1]
                        <li class="pagination__list-item">
                            <a class="pagination__list-element" href="${pager.getPageLink(1)}" >1</a>
                        </li>
                        [#if pager.beginIndex > 2]
                            <li class="pagination__list-item is-more">
                                <a class="pagination__list-element" href="#">...</a>
                            </li>
                        [/#if]
                    [/#if]

                    [#list pager.beginIndex..pager.endIndex as i]
                        [#if i != pager.currentPage]
                            <li class="pagination__list-item">
                                <a class="pagination__list-element" href="${pager.getPageLink(i)}">${i}</a>
                            </li>
                        [#else]
                            <li class="pagination__list-item is-active">
                                <span class="is-hidden">Sie sind hier:</span>
                                <strong class="pagination__list-element">${i}</strong>
                            </li>
                        [/#if]
                    [/#list]

                    [#if pager.endIndex < pager.numPages ]
                        [#if pager.endIndex < pager.numPages - 1]
                            <li class="pagination__list-item is-more">
                                <a class="pagination__list-element" href="#">...</a>
                            </li>
                        [/#if]
                        <li class="pagination__list-item">
                            <a class="pagination__list-element" href="${pager.getPageLink(pager.numPages)}">${pager.numPages}</a>
                        </li>
                    [/#if]

                    [#if pager.currentPage < pager.numPages]
                        <li class="pagination__list-item">
                            <a class="pagination__list-element is-next" href="${pager.getPageLink(pager.currentPage + 1)}">
                                <span class="is-hidden">${i18n['search.result.pagination.onePage']}</span>Forward
                            </a>
                        </li>
                    [#else]
                        <li class="pagination__list-item">
                            <strong class="pagination__list-element is-next">
                                <span class="is-hidden">${i18n['search.result.pagination.onePage']}</span>${i18n['search.result.pagination.button.forward']}
                            </strong>
                        </li>
                    [/#if]
                </ul>
            </div><!-- end pager -->
        [/#if]
    [/#if]
[/#macro]