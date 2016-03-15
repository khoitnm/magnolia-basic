[#assign title = content.title!"Eric's Classic Cars"]
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
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/projectdemo-main/webresources/bootstrap-3.3.5/bootstrap.min.css ">
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/projectdemo-main/webresources/bootstrap-3.3.5/bootstrap-theme.min.css ">
[#--Custom CSS--]
    <link rel="stylesheet" href="${ctx.contextPath}/.resources/projectdemo-main/webresources/css/one-pager.css?z=123">
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
                background-size: cover;
            }
            [/#if]
        [/#if][#-- eof: introBgImage --]
    </style>
[@cms.page /]
</head>
<body>
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

[#--jquery and bootstrap js--]
<script src="${ctx.contextPath}/.resources/projectdemo-main/webresources/bootstrap-3.3.5/jquery.js"></script>
<script src="${ctx.contextPath}/.resources/projectdemo-main/webresources/bootstrap-3.3.5/bootstrap.min.js"></script>
<script src="${ctx.contextPath}/.resources/projectdemo-main/webresources/bootstrap-3.3.5/jquery.easing.min.js"></script>
[#--custom js--]
<script src="${ctx.contextPath}/.resources/projectdemo-main/webresources/js/one-pager.js"></script>
</body>
</html>