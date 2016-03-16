[#assign title = content.title!"Eric's Classic Cars"]
[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

[#macro createSectionNav page areaName="content-sections" type="top" ]

    [#assign ulClass = "default-ul-class" ]
    [#assign isTop = false ]
    [#if type=="top"]
        [#assign ulClass = "nav navbar-nav navbar-right" ]
        [#assign isTop = true ]
    [#elseif type=="bottom"]
        [#assign ulClass = "list-inline" ]
    [/#if]

<ul class="${ulClass}">
    [#if isTop]
        <li><a class="page-scroll" href="#intro">Home</a></li>
    [#else]
        <li><a class="page-scroll" href="#intro">Intro</a></li>
    [/#if]

    [#if cmsfn.contentByPath(page.@path+"/"+areaName)?exists]
        [#list cmsfn.children(cmsfn.contentByPath(page.@path+"/"+areaName), "mgnl:component") as component]
            [#assign navTitle = component.sectionName!component.headline!/]
            [#if navTitle?has_content]
                [#if !isTop]<li class="footer-menu-divider">&sdot;</li>[/#if]
                <li><a class="page-scroll" href="#${component.@uuid}">${component.sectionName!component.headline!component.@uuid}</a></li>
            [/#if]
        [/#list]
    [/#if]
</ul>
[/#macro]

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="keywords" content="${content.keywords!""}"/>
    <meta name="description" content="${content.description!""}"/>
    <meta name="author" content="">

    <title>${title}</title>

[#--bootstrap css--]
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/bootstrap.min.css ">
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/bootstrap-theme.min.css ">
[#--Custom CSS--]
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/projectdemo-main-theme/css/one-pager.css?z=123">
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
[#--HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries--]
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style>
        [#if content.introBgImage?has_content]
            [#assign assetRendition = damfn.getRendition(content.introBgImage, "xxlarge")! /]
            [#if assetRendition?has_content]
            .intro-section {
                background: url(${assetRendition.getLink()}) no-repeat center center;
                [#--background: url(${ctx.contextPath}/.resources/projectdemo-main-theme/img/intro-bg.jpg) no-repeat center center;--]
                background-size: cover;
            }
            [/#if]
        [/#if][#-- eof: introBgImage --]
    </style>
[@cms.page /]
</head>
<body>
    [#--top navigation--]
    <nav class="navbar navbar-default navbar-fixed-top topnav" role="navigation">
        <div class="container topnav">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand topnav" href="#intro">Home</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            [#--dynamic part of top-nav--]
                [@createSectionNav page=content areaName="content-sections" type="top" /]
            </div>
        </div>
    </nav>
    [#--eof: top navigation--]
    <div class="intro-section" id="intro">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="intro-message">
                        <h1 class="dark">${title}</h1>
                    [#if content.subTitle?has_content]
                        <h3>${content.subTitle}</h3>
                    [/#if]
                        <hr class="intro-divider">
                    </div>
                </div>
            </div>
        </div>
    </div>
    [#--eof: intro-section--]

    [@cms.area name="content-sections"/]

    [#--Footer--]
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                [#--footer-nav--]
                [@createSectionNav page=content areaName="content-sections" type="bottom" /]
                [#if content.copyrightNote?has_content]<div class="copyright">${cmsfn.decode(content).copyrightNote}</div>[/#if]
                </div>
            </div>
        </div>
    </footer>
    [#--eof: Footer--]

    [#--jquery and bootstrap js--]
    <script src="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/jquery.js"></script>
    <script src="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/bootstrap.min.js"></script>
    <script src="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/jquery.easing.min.js"></script>
    [#--custom js--]
    <script src="${ctx.contextPath}/.resources/projectdemo-main-theme/js/one-pager.js"></script>
</body>
</html>