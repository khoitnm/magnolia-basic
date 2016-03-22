# projectdemo-basic
This is the code for setup a basic magnolia project.
In your real project, just need to replace all projectdemo by your project name (should also refactor folders' name)

Features:
+ Automatically import all static resources from prototype into theme configuration.
+ Home page
+ Product content app (Home page has a component to show products)
+ Product detail page (similar to Job detail)
+ i18n for page content & content app (product will be shown on web page with corresponding language):
	- Assume: pages structures in every language are the same.
	- Create a custom template functions to render items in content app by selecting language.
+ RichTextBox (support link to Page & image to Page, maybe video?)
+ Contact Form:
	- Form module
	- Send email
+ Search Page (search not only in page properties but also in content)