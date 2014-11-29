/*
 * Copyright 2004-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.compass.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.Compass;
import org.compass.core.CompassException;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassConfigurationFactory;
import org.compass.core.config.CompassEnvironment;
import org.compass.core.config.InputStreamMappingResolver;
import org.compass.core.converter.Converter;
import org.compass.core.lucene.LuceneEnvironment;
import org.compass.core.lucene.engine.store.jdbc.ExternalDataSourceProvider;
import org.compass.core.spi.InternalCompass;
import org.compass.core.util.ClassUtils;
import org.compass.spring.transaction.SpringSyncTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author kimchy
 */
public class LocalCompassBean implements FactoryBean, InitializingBean, DisposableBean, BeanNameAware, ApplicationContextAware, BeanClassLoaderAware {

    protected static final Log log = LogFactory.getLog(LocalCompassBean.class);

    private Resource connection;

    private Resource configLocation;

    private String mappingScan;

    private Resource[] configLocations;

    private Resource[] resourceLocations;

    private Resource[] resourceJarLocations;

    private Resource[] resourceDirectoryLocations;

    private String[] classMappings;

    private InputStreamMappingResolver[] mappingResolvers;

    private Properties compassSettings;

    private Map<String, Object> settings;

    private DataSource dataSource;

    private PlatformTransactionManager transactionManager;

    private Map<String, Converter> convertersByName;

    private Compass compass;

    private String beanName;

    private ClassLoader classLoader;

    private ApplicationContext applicationContext;

    private CompassConfiguration config;

    private LocalCompassBeanPostProcessor postProcessor;

    /**
     * Allows to register a post processor for the Compass configuration.
     */
    public void setPostProcessor(LocalCompassBeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Sets an optional connection based on Spring <code>Resource</code>
     * abstraction. Will be used if none is set as part of other possible
     * configuration of Compass connection.
     * <p/>
     * Will use <code>Resource#getFile</code> in order to get the absolute
     * path.
     */
    public void setConnection(Resource connection) {
        this.connection = connection;
    }

    /**
     * Set the location of the Compass XML config file, for example as classpath
     * resource "classpath:compass.cfg.xml".
     * <p/>
     * Note: Can be omitted when all necessary properties and mapping resources
     * are specified locally via this bean.
     */
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * Set the location of the Compass XML config file, for example as classpath
     * resource "classpath:compass.cfg.xml".
     * <p/>
     * Note: Can be omitted when all necessary properties and mapping resources
     * are specified locally via this bean.
     */
    public void setConfigLocations(Resource[] configLocations) {
        this.configLocations = configLocations;
    }

    /**
     * @see org.compass.core.config.CompassConfiguration#addScan(String)
     */
    public void setMappingScan(String basePackage) {
        this.mappingScan = basePackage;
    }

    public void setCompassSettings(Properties compassSettings) {
        this.compassSettings = compassSettings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    /**
     * Set locations of Compass resource files (mapping and common metadata),
     * for example as classpath resource "classpath:example.cpm.xml". Supports
     * any resource location via Spring's resource abstraction, for example
     * relative paths like "WEB-INF/mappings/example.hbm.xml" when running in an
     * application context.
     * <p/>
     * Can be used to add to mappings from a Compass XML config file, or to
     * specify all mappings locally.
     */
    public void setResourceLocations(Resource[] resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

    /**
     * Set locations of jar files that contain Compass resources, like
     * "WEB-INF/lib/example.jar".
     * <p/>
     * Can be used to add to mappings from a Compass XML config file, or to
     * specify all mappings locally.
     */
    public void setResourceJarLocations(Resource[] resourceJarLocations) {
        this.resourceJarLocations = resourceJarLocations;
    }

    /**
     * Set locations of directories that contain Compass mapping resources, like
     * "WEB-INF/mappings".
     * <p/>
     * Can be used to add to mappings from a Compass XML config file, or to
     * specify all mappings locally.
     */
    public void setResourceDirectoryLocations(Resource[] resourceDirectoryLocations) {
        this.resourceDirectoryLocations = resourceDirectoryLocations;
    }

    /**
     * Sets the fully qualified class names for mappings. Useful when using annotations
     * for example. Will also try to load the matching "[Class].cpm.xml" file.
     */
    public void setClassMappings(String[] classMappings) {
        this.classMappings = classMappings;
    }

    /**
     * Sets the mapping resolvers the resolved Compass mapping definitions.
     */
    public void setMappingResolvers(InputStreamMappingResolver[] mappingResolvers) {
        this.mappingResolvers = mappingResolvers;
    }

    /**
     * Sets a <code>DataSource</code> to be used when the index is stored within a database.
     * The data source must be used with {@link org.compass.core.lucene.engine.store.jdbc.ExternalDataSourceProvider}
     * for externally configured data sources (such is the case some of the time with spring). If set, Compass data source provider
     * does not have to be set, since it will automatically default to <code>ExternalDataSourceProvider</code>. If the
     * compass data source provider is set as a compass setting, it will be used.
     * <p/>
     * Note, that it will be automatically wrapped with Spring's <literal>TransactionAwareDataSourceProxy</literal> if not
     * already wrapped by one.
     * {@link org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy}.
     * <p/>
     * Also note that setting the data source is not enough to configure Compass to store the index
     * within the database, the Compass connection string should also be set to <code>jdbc://</code>.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        if (!(dataSource instanceof TransactionAwareDataSourceProxy)) {
            this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
        }
    }

    /**
     * Sets Spring <code>PlatformTransactionManager</code> to be used with compass. If using
     * {@link org.compass.spring.transaction.SpringSyncTransactionFactory}, it must be set.
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Sets a map of global converters to be registered with compass. The map key will be
     * the name that the converter will be registered against, and the value should be the
     * Converter itself (natuarally configured using spring DI).
     */
    public void setConvertersByName(Map<String, Converter> convertersByName) {
        this.convertersByName = convertersByName;
    }

    public void setCompassConfiguration(CompassConfiguration config) {
        this.config = config;
    }

    public void afterPropertiesSet() throws Exception {
        CompassConfiguration config = this.config;
        if (config == null) {
            config = newConfiguration();
        }

        if (classLoader != null) {
            config.setClassLoader(getClassLoader());
        }

        if (this.configLocation != null) {
            config.configure(this.configLocation.getURL());
        }

        if (this.configLocations != null) {
            for (Resource configLocation1 : configLocations) {
                config.configure(configLocation1.getURL());
            }
        }

        if (this.mappingScan != null) {
            config.addScan(this.mappingScan);
        }

        if (this.compassSettings != null) {
            config.getSettings().addSettings(this.compassSettings);
        }

        if (this.settings != null) {
            config.getSettings().addSettings(this.settings);
        }

        if (resourceLocations != null) {
            for (Resource resourceLocation : resourceLocations) {
                config.addInputStream(resourceLocation.getInputStream(), resourceLocation.getFilename());
            }
        }

        if (resourceJarLocations != null) {
            for (Resource resourceJarLocation : resourceJarLocations) {
                config.addJar(resourceJarLocation.getFile());
            }
        }

        if (classMappings != null) {
            for (String classMapping : classMappings) {
                config.addClass(ClassUtils.forName(classMapping, getClassLoader()));
            }
        }

        if (resourceDirectoryLocations != null) {
            for (Resource resourceDirectoryLocation : resourceDirectoryLocations) {
                File file = resourceDirectoryLocation.getFile();
                if (!file.isDirectory()) {
                    throw new IllegalArgumentException("Resource directory location ["
                            + resourceDirectoryLocation + "] does not denote a directory");
                }
                config.addDirectory(file);
            }
        }

        if (mappingResolvers != null) {
            for (InputStreamMappingResolver mappingResolver : mappingResolvers) {
                config.addMappingResolver(mappingResolver);
            }
        }

        if (convertersByName != null) {
            for (Map.Entry<String, Converter> entry : convertersByName.entrySet()) {
                config.registerConverter(entry.getKey(), entry.getValue());
            }
        }
        if (config.getSettings().getSetting(CompassEnvironment.NAME) == null) {
            config.getSettings().setSetting(CompassEnvironment.NAME, beanName);
        }

        if (config.getSettings().getSetting(CompassEnvironment.CONNECTION) == null && connection != null) {
            config.getSettings().setSetting(CompassEnvironment.CONNECTION, connection.getFile().getAbsolutePath());
        }

        if (applicationContext != null) {
            String[] names = applicationContext.getBeanNamesForType(PropertyPlaceholderConfigurer.class);
            for (String name : names) {
                try {
                    PropertyPlaceholderConfigurer propConfigurer = (PropertyPlaceholderConfigurer) applicationContext.getBean(name);
                    Method method = findMethod(propConfigurer.getClass(), "mergeProperties");
                    method.setAccessible(true);
                    Properties props = (Properties) method.invoke(propConfigurer);
                    method = findMethod(propConfigurer.getClass(), "convertProperties", Properties.class);
                    method.setAccessible(true);
                    method.invoke(propConfigurer, props);
                    method = findMethod(propConfigurer.getClass(), "parseStringValue", String.class, Properties.class, Set.class);
                    method.setAccessible(true);
                    String nullValue = null;
                    try {
                        Field field = propConfigurer.getClass().getDeclaredField("nullValue");
                        field.setAccessible(true);
                        nullValue = (String) field.get(propConfigurer);
                    } catch (NoSuchFieldException e) {
                        // no field (old spring version)
                    }
                    for (Map.Entry entry : config.getSettings().getProperties().entrySet()) {
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        value = (String) method.invoke(propConfigurer, value, props, new HashSet());
                        config.getSettings().setSetting(key, value.equals(nullValue) ? null : value);
                    }
                } catch (Exception e) {
                    log.debug("Failed to apply property placeholder defined in bean [" + name + "]", e);
                }
            }
        }

        // we do this after the proeprties placeholder hack, so if other Compass beans are created beacuse
        // of it, we still maintain the correct thread local setting of the data source and transaction manager
        if (dataSource != null) {
            ExternalDataSourceProvider.setDataSource(dataSource);
            if (config.getSettings().getSetting(LuceneEnvironment.JdbcStore.DataSourceProvider.CLASS) == null) {
                config.getSettings().setSetting(LuceneEnvironment.JdbcStore.DataSourceProvider.CLASS,
                        ExternalDataSourceProvider.class.getName());
            }
        }

        String compassTransactionFactory = config.getSettings().getSetting(CompassEnvironment.Transaction.FACTORY);
        if (compassTransactionFactory == null && transactionManager != null) {
            // if the transaciton manager is set and a transcation factory is not set, default to the SpringSync one.
            config.getSettings().setSetting(CompassEnvironment.Transaction.FACTORY, SpringSyncTransactionFactory.class.getName());
        }
        if (compassTransactionFactory != null && compassTransactionFactory.equals(SpringSyncTransactionFactory.class.getName())) {
            if (transactionManager == null) {
                throw new IllegalArgumentException("When using SpringSyncTransactionFactory the transactionManager property must be set");
            }
        }
        SpringSyncTransactionFactory.setTransactionManager(transactionManager);

        if (postProcessor != null) {
            postProcessor.process(config);
        }
        this.compass = newCompass(config);
        this.compass = (Compass) Proxy.newProxyInstance(SpringCompassInvocationHandler.class.getClassLoader(),
                new Class[]{InternalCompass.class}, new SpringCompassInvocationHandler(this.compass));
    }

    protected CompassConfiguration newConfiguration() {
        return CompassConfigurationFactory.newConfiguration();
    }

    protected Compass newCompass(CompassConfiguration config) throws CompassException {
        return config.buildCompass();
    }

    public Object getObject() throws Exception {
        return this.compass;
    }

    public Class getObjectType() {
        return (compass != null) ? compass.getClass() : Compass.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void destroy() throws Exception {
        this.compass.close();
    }

    protected ClassLoader getClassLoader() {
        if (classLoader != null) {
            return classLoader;
        }
        return Thread.currentThread().getContextClassLoader();
    }

    private Method findMethod(Class clazz, String methodName, Class ... parameterTypes) {
        if (clazz.equals(Object.class)) {
            return null;
        }
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return findMethod(clazz.getSuperclass(), methodName, parameterTypes);
        }
    }

    /**
     * Invocation handler that handles close methods.
     */
    private class SpringCompassInvocationHandler implements InvocationHandler {

        private static final String GET_TARGET_COMPASS_METHOD_NAME = "getTargetCompass";

        private static final String CLONE_METHOD = "clone";

        private Compass targetCompass;

        public SpringCompassInvocationHandler(Compass targetCompass) {
            this.targetCompass = targetCompass;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Invocation on ConnectionProxy interface coming in...

            if (method.getName().equals(GET_TARGET_COMPASS_METHOD_NAME)) {
                return compass;
            }

            if (method.getName().equals(CLONE_METHOD) && args.length == 1) {
                if (dataSource != null) {
                    ExternalDataSourceProvider.setDataSource(dataSource);
                }

                SpringSyncTransactionFactory.setTransactionManager(transactionManager);
            }

            // Invoke method on target connection.
            try {
                return method.invoke(targetCompass, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

}
