[#assign path = content.@handle /]
Path: ${path}<br/>
Content: ${content}<br/>

[#if content.text?has_content]
RichText: ${cmsfn.decode(content)[commonfn.propertyNameByLocale("text")]}
[/#if]