package com.projectdemo.basic.pojo;

import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;

/**
 * @author dat.dang (2016-Mar-29)
 */
public class SearchResultItem {
    private String _title;
    private String _link;
    private String _text;

    public SearchResultItem(Node node, TemplatingFunctions templatingFunctions) {
        _title = PropertyUtil.getString(node, "title");
        _link = templatingFunctions.link(node);
        _text = PropertyUtil.getString(node, "abstract");
    }

    public String getTitle() {
        return _title;
    }

    public String getLink() {
        return _link;
    }

    public String getText() {
        return _text;
    }
}
