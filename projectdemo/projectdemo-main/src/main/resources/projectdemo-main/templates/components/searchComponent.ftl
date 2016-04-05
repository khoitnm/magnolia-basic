[#-------------- ASSIGNMENTS --------------]
[#assign queryStr = ctx.getParameter('queryStr')!?html]

[#-------------- RENDERING --------------]
[#if queryStr?has_content]
    [#assign searchResults = searchfn.searchPages(queryStr, '/home') /]
    [#assign recordsFound = searchResults?size /]
[#if recordsFound == 0]
<h3>Page not found</h3>
[/#if]
<div class="list-group">
    [#if searchResults?has_content]
        [#list searchResults as item]
            <a href="${cmsfn.link(item)}" class="list-group-item">
                <h4 class="list-group-item-heading">${item.title!}</h4>
                <p class="list-group-item-text">${item.excerpt!}</p>
            </a>
        [/#list]
    [/#if]
</div>

[#else]
    [#if cmsfn.isEditMode()]
    No result
    [/#if]
[/#if]
