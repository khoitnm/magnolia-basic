package com.projectdemo.basic.common.util;

import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author khoi.tran
 */
public final class JcrPropertyHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(JcrPropertyHelper.class);

    @Inject
    private Provider<I18nContentSupport> i18nContentSupport;


    public Locale getCurrentLocale() {
        return i18nContentSupport.get().getLocale();
    }

    public Locale getDefaultLocale() {
        return i18nContentSupport.get().getDefaultLocale();
    }

    public boolean isDefaultLocale(Locale locale) {
        return locale.equals(getDefaultLocale());
    }

    public Calendar getDate(Node node, String relPath, Locale locale) {
        String propertyName = getPropertyNameByLocale(locale, relPath);
        return PropertyUtil.getDate(node, propertyName);
    }

    public String getString(Node node, String relPath, Locale locale) {
        String propertyName = getPropertyNameByLocale(locale, relPath);
        return PropertyUtil.getString(node, propertyName);
    }

    public List<String> tryGetListStrings(Node node, String relPath, Locale locale) {
        String propertyName = getPropertyNameByLocale(locale, relPath);
        Object objValue = PropertyUtil.getPropertyValueObject(node, propertyName);
        return tryToListString(objValue);
    }

    private static List<String> tryToListString(Object objValue) {
        List<String> result;
        if (objValue instanceof List) {
            result = (List<String>) objValue;
        } else if (objValue instanceof String) {
            result = new ArrayList<>();
            result.add((String) objValue);
        } else {
            result = null;
        }
        return result;
    }

    public static List<String> tryGetListStrings(Node node, String propertyName) {
        Object objValue = PropertyUtil.getPropertyValueObject(node, propertyName);
        return tryToListString(objValue);
    }

    public String getPropertyNameByLocale(Locale locale, String propertyName) {
        String propertyNameByLocale = propertyName;
        if (locale != null && !isDefaultLocale(locale)) {
            propertyNameByLocale = propertyName + "_" + locale.getLanguage();
        }
        return propertyNameByLocale;
    }

    public static boolean hasPropertyName(Node node, String propertyName) {
        try {
            return node.hasProperty(propertyName);
        } catch (RepositoryException e) {
            LOGGER.error("Cannot check hasProperty({}) of node {}", propertyName, node, e);
            return false;
        }
    }

    public static boolean isMagnoliaProperty(final String propertyName) {
        return StringUtils.startsWith(propertyName, NodeTypes.JCR_PREFIX) || StringUtils.startsWith(propertyName, NodeTypes.MGNL_PREFIX);
    }

}
