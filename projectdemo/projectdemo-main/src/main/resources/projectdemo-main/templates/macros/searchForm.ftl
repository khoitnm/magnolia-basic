[#-- Creates a search form. Only the action parameter is mandatory --]
[#macro searchForm action inputName="queryStr" placeholder="Search" buttonLabel="Submit"]

<form action="${action!}" class="navbar-form" role="search">
  <div class="form-group has-feedback">
    <label for="nav-search" class="sr-only">${placeholder}</label>
    <input id="nav-search" type="text" name="${inputName}" class="form-control" placeholder="${placeholder}">
    <span class="glyphicon glyphicon-search form-control-feedback"></span>
    [#-- Submit button is present to support ENTER key submitting the form. Placed offscreen so it is not visible.--]
    <input type="submit" style="position: absolute; left: -9999px; width: 1px; height: 1px;"/>
  </div>
</form>

[/#macro]
