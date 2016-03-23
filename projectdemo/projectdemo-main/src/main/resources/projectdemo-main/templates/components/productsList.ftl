[#assign path = content.@handle /]
[#assign lang = cmsfn.language() /]
[#assign descriptionPropertyName = "description_" + lang /]

[#assign productList = cmsfn.children(cmsfn.contentByPath(content.@handle + "/productList")) /]
[#list productList as item]
    [#assign nodeProduct = model.getProductInfo(item.productId!)! /]
    Title: ${i18n[nodeProduct.title!]}<br/>
    Image: <img src="${damfn.getAssetLink(nodeProduct.image!)!}" alt=""><br />
    Description: ${nodeProduct[descriptionPropertyName]!}
    Description 2: ${commonfn.propertyByLocale(item, "description")!}
[/#list]