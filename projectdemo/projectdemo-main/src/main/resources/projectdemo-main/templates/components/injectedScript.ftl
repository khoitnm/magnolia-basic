[#assign path = content.@handle /]
Path: ${path}<br/>
Content: ${content}<br/>
RichText: ${cmsfn.decode(content).text!""}
