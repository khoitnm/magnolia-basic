[#assign path = content.@handle /]
[#assign descriptionPropertyName = commonfn.propertyNameByLocale("description")/]
[#assign titlePropertyName = commonfn.propertyNameByLocale("title")/]

[#assign productList = cmsfn.children(cmsfn.contentByPath(content.@handle + "/productList")) /]
[#list productList as item]
    [#assign nodeProduct = model.getProductInfo(item.productId!)! /]
Title: ${nodeProduct[titlePropertyName]!}<br/>
Image: <img src="${damfn.getAssetLink(nodeProduct.image!)!}" alt=""><br/>
Description: ${nodeProduct[descriptionPropertyName]!}<br/>
[/#list]