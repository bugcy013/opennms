package org.opennms.features.topology.api;

import org.opennms.features.topology.api.osgi.VaadinApplicationContext;

public interface VaadinUiInitializedListener {
    void vaadinUiInitialized(VaadinApplicationContext applicationContext);
}
