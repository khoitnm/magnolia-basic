[#assign path = content.@handle /]
[#assign productTitlePropertyName = commonfn.propertyNameByLocale("title")/]
[#assign productDescriptionPropertyName = commonfn.propertyNameByLocale("description")/]
[#assign productIds = content.productList]
[#list productIds as productId]
    [#assign nodeProduct = model.getProductInfo(productId!)! /]
<div style="background-color: #DDD; border: 1px solid #CCC; padding: 10px;">
    <h3>${nodeProduct[productTitlePropertyName]!}</h3>
${nodeProduct[productDescriptionPropertyName]!}
</div>
[/#list]
