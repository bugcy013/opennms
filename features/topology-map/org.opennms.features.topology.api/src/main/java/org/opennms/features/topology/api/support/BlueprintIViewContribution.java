/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.topology.api.support;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import org.opennms.features.topology.api.IViewContribution;
import org.opennms.features.topology.api.WidgetContext;
import org.opennms.features.topology.api.osgi.VaadinApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.container.BlueprintContainer;

public class BlueprintIViewContribution implements IViewContribution {

	private final BlueprintContainer m_container;
	private final String m_beanId;
	private String m_title;

	public BlueprintIViewContribution(BlueprintContainer container, String beanId) {
		m_container = container;
		m_beanId = beanId;
	}

    @Override
    public Component getView(VaadinApplicationContext applicationContext, WidgetContext widgetContext) {
        // Get the component by asking the blueprint container to instantiate a prototype bean
        Component component = (Component)m_container.getComponentInstance(m_beanId);
        BundleContext bundleContext = (BundleContext) m_container.getComponentInstance("blueprintBundleContext");
        applicationContext.getEventProxy(bundleContext).addPossibleEventConsumer(component);
        return component;

    }

	/**
	 * Returns null.
	 */
	@Override
	public Resource getIcon() {
		return null;
	}

	@Override
	public String getTitle() {
		return m_title;
	}

	public void setTitle(String title) {
		m_title = title;
	}
}
