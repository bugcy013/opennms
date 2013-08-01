package org.opennms.features.topology.plugins.ncs.internal;

import org.opennms.features.topology.api.osgi.OnmsServiceManager;
import org.opennms.features.topology.api.osgi.VaadinApplicationContext;
import org.opennms.features.topology.api.osgi.locator.OnmsServiceManagerLocator;
import org.opennms.features.topology.api.topo.Criteria;
import org.osgi.framework.BundleContext;

import java.util.List;
import java.util.Properties;


public class NCSCriteriaServiceManager {
    
    private BundleContext m_bundleContext;

    public void registerCriteria(VaadinApplicationContext applicationContext, Criteria ncsCriteria) {
        //This is to get around an issue with the NCSPathProvider when registering a service with different namespaces
        removeServicesForSessionWithNamespace(applicationContext, ncsCriteria.getNamespace());
        
        Properties additionalProperties = new Properties();
        additionalProperties.put("namespace", ncsCriteria.getNamespace());
        getServiceManager().registerAsService(ncsCriteria, applicationContext, additionalProperties);
    }
    
    private void removeServicesForSessionWithNamespace(VaadinApplicationContext applicationContext, String namespace) {
        unregisterCriteria(applicationContext, namespace);
    }

    public void setBundleContext(BundleContext bundleContext) {
        m_bundleContext = bundleContext;
    }

    public boolean isCriteriaRegistered(VaadinApplicationContext applicationContext, String namespace) {
        Properties additionalProperties = new Properties();
        additionalProperties.put("namespace", namespace);
        List<Criteria> criterias = getServiceManager().getServices(Criteria.class, applicationContext, additionalProperties);
        return !criterias.isEmpty();
    }

    public void unregisterCriteria(VaadinApplicationContext applicationContext, String namespace) {
        Properties additionalProperties = new Properties();
        additionalProperties.put("namespace", namespace);
        getServiceManager().unregister(Criteria.class, applicationContext, additionalProperties);
    }

    private OnmsServiceManager getServiceManager() {
        return new OnmsServiceManagerLocator().lookup(m_bundleContext);
    }
}
