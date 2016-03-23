[#assign path = content.@handle /]
[#assign descriptionPropertyName = commonfn.propertyNameByLocale("description")/]

[#assign productList = cmsfn.children(cmsfn.contentByPath(content.@handle + "/productList")) /]
[#list productList as item]
    [#assign nodeProduct = model.getProductInfo(item.productId!)! /]
Path: ${path}
Title: ${i18n[nodeProduct.title!]}<br/>
Image: <img src="${damfn.getAssetLink(nodeProduct.image!)!}" alt=""><br/>
Property: ${descriptionPropertyName}
Description: '${nodeProduct[descriptionPropertyName]!}<br/>'
[/#list]