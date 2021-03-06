/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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
package org.opennms.features.vaadin.datacollection;

import org.opennms.netmgt.config.datacollection.Parameter;
import org.opennms.netmgt.config.datacollection.StorageStrategy;
import org.opennms.netmgt.dao.support.IndexStorageStrategy;
import org.opennms.netmgt.dao.support.SiblingColumnStorageStrategy;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractSelect.NewItemHandler;

/**
 * The Storage Strategy Field.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a> 
 */
@SuppressWarnings("serial")
public class StorageStrategyField extends CustomField<StorageStrategy> implements Button.ClickListener {

    /** The Combo Box. */
    private final ComboBox combo = new ComboBox("Class Name");

    /** The Container. */
    private final BeanContainer<String,Parameter> container = new BeanContainer<String,Parameter>(Parameter.class);

    /** The Table. */
    private final Table table = new Table("Parameters", container);

    /** The Toolbar. */
    private final HorizontalLayout toolbar = new HorizontalLayout();

    /** The add button. */
    private final Button add = new Button("Add", this);

    /** The delete button. */
    private final Button delete = new Button("Delete", this);

    /**
     * Instantiates a new storage strategy field.
     */
    public StorageStrategyField() {
        combo.addItem(IndexStorageStrategy.class.getName());
        combo.addItem(SiblingColumnStorageStrategy.class.getName());
        combo.setNullSelectionAllowed(false);
        combo.setRequired(true);
        combo.setNewItemsAllowed(true);
        combo.setNewItemHandler(new NewItemHandler() {
            @Override
            public void addNewItem(String newItemCaption) {
                if (!combo.containsId(newItemCaption)) {
                    combo.addItem(newItemCaption);
                    combo.setValue(newItemCaption);
                }
            }
        });

        container.setBeanIdProperty("key");
        table.addStyleName("light");
        table.setVisibleColumns(new Object[]{"key", "value"});
        table.setColumnHeader("key", "Parameter Name");
        table.setColumnHeader("value", "Parameter Value");
        table.setColumnExpandRatio("value", 1);
        table.setEditable(!isReadOnly());
        table.setSelectable(true);
        table.setHeight("125px");
        table.setWidth("100%");

        toolbar.addComponent(add);
        toolbar.addComponent(delete);
        toolbar.setVisible(table.isEditable());
        setBuffered(true);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.CustomField#initContent()
     */
    @Override
    public Component initContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(combo);
        layout.addComponent(table);
        layout.addComponent(toolbar);
        layout.setComponentAlignment(toolbar, Alignment.MIDDLE_RIGHT);
        return layout;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#getType()
     */
    @Override
    public Class<StorageStrategy> getType() {
        return StorageStrategy.class;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#setPropertyDataSource(com.vaadin.data.Property)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof StorageStrategy) {
            StorageStrategy dto = (StorageStrategy) value;
            combo.setValue(dto.getClazz());
            container.removeAllItems();
            container.addAll(dto.getParameterCollection());
            table.setPageLength(dto.getParameterCollection() == null ? 0 : dto.getParameterCollection().size());
        } else {
            throw new ConversionException("Invalid type");
        }
        super.setPropertyDataSource(newDataSource);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#getValue()
     */
    @Override
    public StorageStrategy getValue() {
        StorageStrategy dto = new StorageStrategy();
        dto.setClazz((String) combo.getValue());
        for (Object itemId: container.getItemIds()) {
            dto.getParameterCollection().add(container.getItem(itemId).getBean());
        }
        return dto;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractComponent#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        combo.setReadOnly(readOnly);
        table.setEditable(!readOnly);
        toolbar.setVisible(!readOnly);
        super.setReadOnly(readOnly);
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button btn = event.getButton();
        if (btn == add) {
            addHandler();
        }
        if (btn == delete) {
            deleteHandler();
        }
    }

    /**
     * Adds the handler.
     */
    private void addHandler() {
        Parameter p = new Parameter();
        p.setKey("New Parameter");
        container.addBean(p);
    }

    /**
     * Delete handler.
     */
    private void deleteHandler() {
        final Object itemId = table.getValue();
        if (itemId == null) {
            Notification.show("Please select a Parameter from the table.");
        } else {
            ConfirmDialog.show(getUI(),
                               "Are you sure?",
                               "Do you really want to remove the selected parameter from the Storage Strategy?\nThis action cannot be undone.",
                               "Yes",
                               "No",
                               new ConfirmDialog.Listener() {
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                        table.removeItem(itemId);
                    }
                }
            });
        }
    }

}
