[#-------------- ASSIGNMENTS --------------]
[#-- Page's model & definition, based on the rendering hierarchy and not the node hierarchy --]
[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#-------------- RENDERING --------------]
<title>${content.windowTitle!content.title!}</title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description" content="${content.description!""}" />
<meta name="keywords" content="${content.keywords!""}" />
<meta name="author" content="Magnolia International Ltd." />
<meta name="generator" content="Powered by Magnolia - Intuitive Opensource CMS" />

<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<![endif]-->

<link rel="icon" href="${ctx.contextPath}/.resources/${theme.name}/favicon.ico" />

[#list theme.cssFiles as cssFile]
    [#if cssFile.conditionalComment?has_content]<!--[if ${cssFile.conditionalComment}]>[/#if]
<link rel="stylesheet" type="text/css" href="${cssFile.link}" media="${cssFile.media}" />
    [#if cssFile.conditionalComment?has_content]<![endif]-->[/#if]
[/#list]

[#if def.cssFiles??]
    [#list def.cssFiles as cssFile]
<link rel="stylesheet" type="text/css" href="${cssFile.link}" media="${cssFile.media}" />
    [/#list]
[/#if]

[#-- jsFiles from the theme are here --]
[#list theme.jsFiles as jsFile]
<script src="${jsFile.link}"></script>
[/#list]

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<script>window.html5 || document.write('<script src="${ctx.contextPath}/.resources/projectdemo-main-theme/js/html5shiv.js"><\/script>')</script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
