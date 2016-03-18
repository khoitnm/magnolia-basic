[#assign pageModel = model.root!]
[#assign pageDef = pageModel.definition!]

<!--[if gt IE 8]><!-->
[#list stkfn.theme(pageModel.site).jsFiles as jsFile]
<script src="${jsFile.link}" type="text/javascript" defer="defer"></script>
[/#list]

[#list pageModel.site.jsFiles as jsFile]
<script src="${jsFile.link}" type="text/javascript" defer="defer"></script>
[/#list]

[#list pageDef.jsFiles as jsFile]
<script src="${jsFile.link}" type="text/javascript" defer="defer"></script>
[/#list]
<!--<![endif]-->
