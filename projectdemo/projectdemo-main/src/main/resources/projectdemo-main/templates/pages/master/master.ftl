[#assign title = content.title!"Eric's Classic Cars"]
[#assign site = sitefn.site()!]
[#assign theme = sitefn.theme(site)!]

<!DOCTYPE html>
<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js" xml:lang="${cmsfn.language()}" lang="${cmsfn.language()}"><!--<![endif]-->
<head>
    [#--Initialize page--]
    [@cms.page /]

    [#--Html Header Area--]
    [@cms.area name="htmlHeader"/]
</head>
<body>
    [#--Header Area--]
    <header role="banner">
        [@cms.area name="header" /]
    </header>

    [#--Stage Area--]
    [@cms.area name="stage" /]

    [#--Navigation Area--]
    [@cms.area name="navigation"/]

    [#--Main Content Area--]
    [@cms.area name="main"/]

    [#--Footer Area--]
    [@cms.area name="footer" /]

    [#--Html Footer Area--]
    [@cms.area name="htmlFooter" /]
</body>
</html>