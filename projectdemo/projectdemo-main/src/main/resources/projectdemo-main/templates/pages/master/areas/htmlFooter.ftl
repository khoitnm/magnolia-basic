[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#list theme.cssFiles as jsFile]
    <script src="${jsFile.link}" type="text/javascript" defer="defer"></script>
[/#list]
