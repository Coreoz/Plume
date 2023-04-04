package com.coreoz.plume.conf;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigIncludeContext;
import com.typesafe.config.ConfigIncluder;
import com.typesafe.config.ConfigIncluderClasspath;
import com.typesafe.config.ConfigIncluderFile;
import com.typesafe.config.ConfigIncluderURL;
import com.typesafe.config.ConfigObject;

/**
 * Config includer that forces all includes to resolve or else fail with a ConfigException.
 * 
 * See https://github.com/lightbend/config/issues/587
 */
public class RequiredIncluder implements ConfigIncluder, ConfigIncluderFile, ConfigIncluderURL,
    ConfigIncluderClasspath {
    private ConfigIncluder delegate;

    @Override
    public ConfigIncluder withFallback(ConfigIncluder fallback) {
        Objects.requireNonNull(fallback);
        if (delegate != fallback) {
            if (delegate == null)
                delegate = fallback;
            else
                delegate = delegate.withFallback(fallback);
        }
        return this;
    }

    @Override
    public ConfigObject include(ConfigIncludeContext context, String what) {
        context = verifyDelegateAndStrictContext(context);
        return delegate.include(context, what);
    }

    @Override
    public ConfigObject includeResources(ConfigIncludeContext context, String what) {
        context = verifyDelegateAndStrictContext(context);
        if (delegate instanceof ConfigIncluderClasspath) {
            return ((ConfigIncluderClasspath) delegate).includeResources(context, what);
        }
        return ConfigFactory.parseResources(what, context.parseOptions()).root();
    }

    @Override
    public ConfigObject includeFile(ConfigIncludeContext context, File what) {
        context = verifyDelegateAndStrictContext(context);
        if (delegate instanceof ConfigIncluderFile) {
            return ((ConfigIncluderFile) delegate).includeFile(context, what);
        }
        return ConfigFactory.parseFile(what, context.parseOptions()).root();
    }

    @Override
    public ConfigObject includeURL(ConfigIncludeContext context, URL what) {
        context = verifyDelegateAndStrictContext(context);
        if (delegate instanceof ConfigIncluderURL) {
            return ((ConfigIncluderURL) delegate).includeURL(context, what);
        }
        return ConfigFactory.parseURL(what, context.parseOptions()).root();
    }

    private ConfigIncludeContext verifyDelegateAndStrictContext(ConfigIncludeContext context) {
        Objects.requireNonNull(delegate, "ConfigIncluder not accessible");
        if (context.parseOptions().getAllowMissing()) {
            return context.setParseOptions(context.parseOptions().setAllowMissing(false));
        }
        return context;
    }
}
