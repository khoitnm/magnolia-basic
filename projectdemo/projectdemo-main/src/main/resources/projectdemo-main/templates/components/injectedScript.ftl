[#if cmsfn.isEditMode()]
<div style="border: 1px solid #AAA;">
<span style="background-color: rgba(255, 255, 255, 0.1); color:#888; padding: 10px; line-height: 22px;">
    &lt;script block&gt;
</span>
${cmsfn.decode(content).text!""}
</div>
[#else]
${cmsfn.decode(content).text!""}
[/#if]

