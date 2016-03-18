[#assign title = content.title!"Eric's Classic Cars"]
[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

<!DOCTYPE html>
<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><!--<![endif]-->
<head>
    [@cms.page /]

    [@cms.area name="htmlHeader"/]
</head>
<body>
    [#--Header--]
    <header role="banner">
        [@cms.area name="header" /]
    </header>

    [#--Main content--]
    [@cms.area name="main"/]

[#--jquery and bootstrap js--]
<script src="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/jquery.js"></script>
<script src="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/bootstrap.min.js"></script>
<script src="${ctx.contextPath}/.resources/projectdemo-main-theme/libs/bootstrap-3.3.5/jquery.easing.min.js"></script>
[#--custom js--]
<script src="${ctx.contextPath}/.resources/projectdemo-main-theme/js/one-pager.js"></script>
</body>
</html>