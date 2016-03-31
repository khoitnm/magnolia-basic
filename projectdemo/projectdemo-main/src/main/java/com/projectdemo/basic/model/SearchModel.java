package com.projectdemo.basic.model;

import com.projectdemo.basic.pojo.SearchResultItem;
import com.projectdemo.basic.templating.functions.CommonTemplatingFunctions;
import info.magnolia.context.MgnlContext;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.query.Query;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author dat.dang (2016-Mar-29)
 */
public class SearchModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchModel.class);

    @Inject
    private CommonTemplatingFunctions _commonTemplatingFunctions;
    @Inject
    private TemplatingFunctions _templatingFunctions;

    private List<Node> _result = new ArrayList<Node>();
    private int _count;
    private int _maxResultsPerPage;
    private int _currentPage = 1;
    private int _numPages = 0;

    private static final String SEARCH_QUERY_PATTERN = "select * from [mgnl:page] AS t where (ISDESCENDANTNODE([''{0}'']) OR name(t) = ''home'') AND [mgnl:template] NOT LIKE ''{1}'' AND contains(t.*, ''{2}'') order by name(t)";
    private static final String TEMPLATE_SEARCH_RESULT = "projectdemo-main:pages/searchTemplate";

    @Inject
    public SearchModel(Node content, RD definition, RenderingModel<?> parent) {
        super(content, definition, parent);
    }


    @Override
    public String execute() {

        Query query = null;

        String queryString = generateSimpleQuery(getQueryStr());
        if (org.apache.commons.lang.StringUtils.isBlank(queryString)){
            return null;
        }

        try {
            _maxResultsPerPage = getMaxResultsPerPage();
            query = _commonTemplatingFunctions.createQuery(queryString, "website");

            _count = pagedQuery(_commonTemplatingFunctions.search(query), getOffset(), _maxResultsPerPage);

            _numPages = _count / _maxResultsPerPage;
            if ((_count % _maxResultsPerPage) > 0) {
                _numPages++;
            }

        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("{0} caught while parsing query for search term [{1}] - query is [{2}]: {3}", e.getClass().getName(), query, queryString, e.getMessage()), e);
        }

        return "";

    }

    protected int getMaxResultsPerPage() {

        _maxResultsPerPage = Integer.MAX_VALUE;
        try {
            if (content.hasProperty("maxResultsPerPage")) {
                _maxResultsPerPage = Integer.parseInt(content.getProperty("maxResultsPerPage").getString());
            }
        } catch (Exception e) {
            LOGGER.error("could not get max results per page", e);
        }
        return _maxResultsPerPage;
    }

    protected int getOffset() {
        return ((getCurrentPage() - 1) * _maxResultsPerPage);

    }

    protected int pagedQuery(Collection<Node> queryResult, int offset, int limit) throws Exception {
        int total = queryResult.size();
        int newLimit = limit;
        if (total > offset) {
            if (total < offset + limit) {
                newLimit = total - offset;
            }

            _result = ((List<Node>) queryResult).subList(offset, offset + newLimit);
        }

        return total;
    }

    protected String generateSimpleQuery(String input) {
        if (org.apache.commons.lang.StringUtils.isBlank(input)){
            return null;
        }

        //escape single quote
        String searchString = input.replace("'", "''");

        return MessageFormat.format(SEARCH_QUERY_PATTERN, new Object[]{getPath(), TEMPLATE_SEARCH_RESULT, searchString});
    }

    public String getPath() {
        try {
            return _templatingFunctions.siteRoot(content).getPath();

        } catch (Exception e) {
            LOGGER.warn("no site");
        }
        return "";
    }

    public Collection<SearchResultItem> getResult() {
        final Collection<SearchResultItem> searchResults = new ArrayList<SearchResultItem>();
        for (Node content : _result) {
            searchResults.add(new SearchResultItem(content, _templatingFunctions));
        }
        return searchResults;
    }

    public String getQueryStr() {
        return MgnlContext.getParameter("field_search");
    }


    public int getCurrentPage() {
        if (MgnlContext.getParameter("currentPage") != null) {
            _currentPage = Integer.parseInt(MgnlContext.getParameter("currentPage"));

        }

        return _currentPage;
    }

    public Node getSiteRoot() {
        return _templatingFunctions.siteRoot(content, "home");
    }

    public int getBeginIndex() {
        if (_currentPage - 2 <= 1) {
            return 1;
        } else {
            return _currentPage - 2;
        }
    }

    public int getEndIndex() {
        if (_currentPage + 2 >= _numPages) {
            return _numPages;
        } else {
            return _currentPage + 2;
        }
    }

    public String getPosition() {
        try {
            if (content.hasProperty("pager")) {
                return content.getProperty("pager").getString();
            }
        } catch (Exception e) {
            LOGGER.debug("no pagination position found");
        }
        return "";

    }

    public int getFrom(){
        return 1 + (_currentPage - 1) * _maxResultsPerPage;
    }

    public int getTo(){
        return _currentPage * _maxResultsPerPage > _count ? _count : _currentPage * _maxResultsPerPage;
    }

    public int getCount() {
        return _count;
    }

    public int getNumPages() {
        return _numPages;
    }
}
