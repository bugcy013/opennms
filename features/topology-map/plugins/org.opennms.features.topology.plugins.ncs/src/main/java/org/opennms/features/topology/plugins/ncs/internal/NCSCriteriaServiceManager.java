package org.opennms.features.topology.plugins.ncs.internal;

import org.opennms.features.topology.api.osgi.OnmsServiceManager;
import org.opennms.features.topology.api.osgi.VaadinApplicationContext;
import org.opennms.features.topology.api.topo.Criteria;

import java.util.List;
import java.util.Properties;


public class NCSCriteriaServiceManager {
    
    private OnmsServiceManager serviceManager;

    public void registerCriteria(VaadinApplicationContext applicationContext, Criteria ncsCriteria) {
        //This is to get around an issue with the NCSPathProvider when registering a service with different namespaces
        removeServicesForSessionWithNamespace(applicationContext, ncsCriteria.getNamespace());
        
        Properties additionalProperties = new Properties();
        additionalProperties.put("namespace", ncsCriteria.getNamespace());
        serviceManager.registerAsService(ncsCriteria, applicationContext, additionalProperties);
    }
    
    private void removeServicesForSessionWithNamespace(VaadinApplicationContext applicationContext, String namespace) {
        unregisterCriteria(applicationContext, namespace);
    }

    public void setServiceManager(OnmsServiceManager serviceManager) {
        this.serviceManager = serviceManager;

        // TODO was machen wir hiermit?
//        m_bundleContext = context;
//        m_bundleContext.addBundleListener(new BundleListener() {
//
//            @Override
//            public void bundleChanged(BundleEvent event) {
//                switch(event.getType()) {
//                    case BundleEvent.STOPPING:
//                        removeAllServices();
//                }
//            }
//        });
    }

    public boolean isCriteriaRegistered(VaadinApplicationContext applicationContext, String namespace) {
        Properties additionalProperties = new Properties();
        additionalProperties.put("namespace", namespace);
        List<Criteria> criterias = serviceManager.getServices(Criteria.class, applicationContext, additionalProperties);
        return !criterias.isEmpty();
    }

    public void unregisterCriteria(VaadinApplicationContext applicationContext, String namespace) {
        Properties additionalProperties = new Properties();
        additionalProperties.put("namespace", namespace);
        serviceManager.unregister(Criteria.class, applicationContext, additionalProperties);
    }
}
