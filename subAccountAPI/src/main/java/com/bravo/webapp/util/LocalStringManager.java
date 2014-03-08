package com.bravo.webapp.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Implementation of a local string manager.
 * Provides access to i18n messages for classes that need them.
 *
 * @author
 * @author Daniel
 */

public class LocalStringManager {

    private static final Logger logger = null;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("localString");

    public static String getLocalString(String key, Object... arguments) {
        String msg = getLocalString(key);
        if (msg != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] == null) {
                    arguments[i] = "null";
                } else if (!(arguments[i] instanceof String) &&
                    !(arguments[i] instanceof Number) &&
                    !(arguments[i] instanceof java.util.Date)) {
                    arguments[i] = arguments[i].toString();
                }
            }
            return MessageFormat.format(msg, arguments);
        } else {
            return key;
        }

    }

    public static String getLocalString(String key) {
        return resourceBundle.getString(key) == null ? key : resourceBundle.getString(key);
    }

//    private Class defaultClass;
//
//    /**
//     * Create a string manager that looks for LocalStrings.properties in
//     * the package of the defaultClass.
//     *
//     * @param defaultClass Class whose package has default localized strings
//     */
//    public LocalStringManager(Class defaultClass) {
//        this.defaultClass = defaultClass;
//    }
//
//    /**
//     * Get a localized string.
//     * Strings are stored in a single property file per package named
//     * LocalStrings[_locale].properties. Starting from the class of the
//     * caller, we walk up the class hierarchy until we find a package
//     * resource bundle that provides a value for the requested key.
//     * <p/>
//     * <p>This simplifies access to resources, at the cost of checking for
//     * the resource bundle of several classes upon each call. However, due
//     * to the caching performed by <tt>ResourceBundle</tt> this seems
//     * reasonable.
//     * <p/>
//     * <p>Due to that, sub-classes <strong>must</strong> make sure they don't
//     * have conflicting resource naming.
//     *
//     * @param callerClass   The object making the call, to allow per-package
//     *                      resource bundles
//     * @param key           The name of the resource to fetch
//     * @param defaultString The default return value if not found
//     * @return The localized value for the resource
//     */
//    private String getLocalString(
//            Class callerClass,
//            String key,
//            String defaultString
//    ) {
//        Class stopClass = defaultClass.getSuperclass();
//        Class startClass = ((callerClass != null) ? callerClass :
//                defaultClass);
//        ResourceBundle resources = null;
//        boolean globalDone = false;
//        for (Class c = startClass;
//             c != stopClass && c != null;
//             c = c.getSuperclass()) {
//            globalDone = (c == defaultClass);
//            try {
//                // Construct the bundle name as LocalStrings in the
//                // caller class's package.
//                StringBuffer resFileName = new StringBuffer(
//                        c.getName().substring(0, c.getName().lastIndexOf(".")));
//                resFileName.append(".LocalStrings");
//
//                resources = ResourceBundle.getBundle(resFileName.toString(), Locale.getDefault(), c.getClassLoader());
//                if (resources != null) {
//                    String value = resources.getString(key);
//                    if (value != null)
//                        return value;
//                }
//            } catch (Exception ex) {
//            }
//        }
//
//        // Look for a global resource (defined by defaultClass)
//        if (!globalDone) {
//            return getLocalString(null, key, defaultString);
//        } else {
//            logger.log(Level.FINE, "No local string for", key);
//            return defaultString;
//        }
//    }
//
//
//    /**
//     * Get a local string for the caller and format the arguments accordingly.
//     *
//     * @param callerClass    The caller (to walk through its class hierarchy)
//     * @param key            The key to the local format string
//     * @param defaultMessage The default format if not found in the resources
//     * @param arguments      The set of arguments to provide to the formatter
//     * @return A formatted localized string
//     */
//    private String getLocalString(
//            String key,
//            String defaultMessage,
//            Object... arguments
//    ) {
//        MessageFormat f = new MessageFormat(
//                getLocalString(callerClass, key, defaultMessage));
//        for (int i = 0; i < arguments.length; i++) {
//            if (arguments[i] == null) {
//                arguments[i] = "null";
//            } else if (!(arguments[i] instanceof String) &&
//                    !(arguments[i] instanceof Number) &&
//                    !(arguments[i] instanceof java.util.Date)) {
//                arguments[i] = arguments[i].toString();
//            }
//        }
//        return f.format(arguments);
//    }
//
//    public String getLocalString(String key) {
//        return getLocalString(defaultClass, key, key);
//    }
//
//    public String getLocalString(String key, Object... arguments) {
//        return getLocalString(defaultClass, key, key, arguments);
//    }

}

