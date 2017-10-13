[#if cmsfn.isEditMode()]
[#--<div style="border: 1px solid #AAA;">--]
[#--<span style="background-color: rgba(255, 255, 255, 0.1); color:#888; border: 1px solid #AAA; padding: 10px; margin:5px; line-height: 22px;">
    &lt;script block&gt;
</span>--]
${content.text!""}
${cmsfn.decode(content).text!""}
[#--</div>--]
[#else]
${cmsfn.decode(content).text!""}
[/#if]

