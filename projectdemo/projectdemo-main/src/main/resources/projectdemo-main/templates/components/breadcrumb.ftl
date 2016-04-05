<nav class="b-breadcrumb--content" role="navigation" id="breadcrumb" data-css="b-breadcrumb">
    <h2 class="breadcrumb__headline">Visited pages</h2>
    <ol class="breadcrumb__list is-container">
    [#list model.breadcrumb as item]
        [#if item_has_next]
            <li class="breadcrumb__list-item">
                <a class="breadcrumb__list-element" href="${item.href!}">${item.navigationTitle!}</a>
            </li>
        [#else]
            <li class="breadcrumb__list-item">
                <span class="breadcrumb__list-element is-active">${item.navigationTitle!}</span>
            </li>
        [/#if]
    [/#list]
    </ol>
</nav>