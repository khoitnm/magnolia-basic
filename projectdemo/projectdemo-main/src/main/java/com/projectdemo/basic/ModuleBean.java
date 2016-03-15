package com.projectdemo.basic;

/**
 * ${link https://documentation.magnolia-cms.com/display/DOCS/Module+configuration}.
 * Your module may have a module class.
 * This class is the "root" bean of the configuration of your module.
 * This means you can add bean properties to the class. When the module is loaded the Node2Bean mechanism will read property values from the configuration and set them in the instance of the module class.
 *
 * This class is optional and represents the configuration for the this module.
 * By exposing simple getter/setter/adder methods, this bean can be configured via content2bean
 * using the properties and node from <tt>config:/modules/projectdemo-main</tt>.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 *
 * @author dat.dang
 */
public class ModuleBean {
    /* you can optionally implement info.magnolia.module.ModuleLifecycle */

}
