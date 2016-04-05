[#-------------- ASSIGNMENTS --------------]
[#assign homeLink = cmsfn.link(cmsfn.siteRoot(content))!"/" /]
[#assign pages = model.rootPages! /]
[#assign childPages = model.childPages! /]
[#assign spaceClass = "navbar-spacer" /]
[#if childPages?has_content]
    [#assign spaceClass = "navbar-spacer-children"]
[/#if]

[#assign searchProperty = cmsfn.siteRoot(content).searchResultPage! /]
[#if searchProperty?has_content]
    [#assign searchResultPage = cmsfn.link(cmsfn.contentById(searchProperty)) /]
[/#if]

[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#assign aboutPage=model.aboutDemoLink!]
[#-------------- RENDERING --------------]
<nav class="navbar navbar-default navbar-fixed-top " role="navigation" style="clear:both;">

    <div class="container">
    [#if aboutPage?has_content]<div id="about-link"><a href="${aboutPage}">${i18n['navigation.aboutDemo']}</a></div>[/#if]

    [#assign localizedLinks = cmsfn.localizedLinks()!]
    [#if localizedLinks?has_content]
        [#assign languages = localizedLinks?keys]
        <h2>Multi languages:</h2>
        <div id="language-link">
            <ul>
            [#-- Current locale should not be linked. --]
            [#-- Use "compress" to put "li" and "a" on one line to prevent white spaces from interfering with layout. --]
                [#list languages as lang]
                    [#if lang?has_content]
                        [#assign current = cmsfn.isCurrentLocale(lang)]
                        <li[#if current] class="active"[/#if]>[@compress single_line=true]
                            [#if !current]<a href="${localizedLinks[lang]!'#'}" title="${model.getLocale(lang).getDisplayName()!lang!}">[#else]<span>[/#if]${model.getLocale(lang).getDisplayLanguage()!lang?upper_case!}[#if !current]</a>[#else]</span>[/#if]
                        [/@compress]</li>
                    [/#if]
                [/#list]
            </ul>
        </div>
    [/#if]

        <h2>Navigation:</h2>
    [#if childPages?has_content]
        <div id="navbar-secondary" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                [#list childPages as childPage]
                    <li class="${childPage.cssClass!}"><a href="${childPage.link}">${childPage.name}</a></li>
                [/#list]
            </ul>
        </div>
    [/#if]
    </div>
</nav>

[#include "/projectdemo-main/templates/macros/searchForm.ftl"]

[#assign searchProperty = content.searchResultPage! /]
[#if searchProperty?has_content]
    [#assign searchResultPage = cmsfn.link(cmsfn.contentById(searchProperty)) /]
[/#if]

[#assign aboutPage=model.aboutDemoLink!]
[#-- Only when the search result page was set should the form be displayed --]
<div>
<br/>
[#if searchResultPage?exists]
    [@searchForm action=searchResultPage! inputName="queryStr" placeholder="SEARCH" /]
[/#if]
</div>