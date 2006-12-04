// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.commons.ui.swt.advanced.dataeditor.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExtendedTableRemoveCommand extends Command {

    private ExtendedTableModel extendedTable;

    private Integer indexItemToRemove;

    private ArrayList beansToRemove;

    private int[] indexItemsToRemove;

    private Object beanToRemove;

    private List removedBeans;
    private List removedBeansIndices;

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableRemoveCommand(ExtendedTableModel extendedTable, Integer indexItemToRemove) {
        super();
        this.extendedTable = extendedTable;
        this.indexItemToRemove = indexItemToRemove;
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableRemoveCommand(ExtendedTableModel extendedTable, Object beanToRemove) {
        super();
        this.extendedTable = extendedTable;
        this.beanToRemove = beanToRemove;
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableRemoveCommand(ExtendedTableModel extendedTable, ArrayList beansToRemove) {
        this.extendedTable = extendedTable;
        this.indexItemToRemove = indexItemToRemove;
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableRemoveCommand(ExtendedTableModel extendedTable, int[] indexItemsToRemove) {
        this.extendedTable = extendedTable;
        this.indexItemsToRemove = indexItemsToRemove;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {

        List beansList = extendedTable.getBeansList();
        
        if (indexItemToRemove != null) {
            removedBeans = new ArrayList(1);
            Object removed = extendedTable.remove((int) indexItemToRemove);
            removedBeans.add(removedBeans);
            removedBeansIndices = new ArrayList(1);
            removedBeansIndices.add(indexItemToRemove);
        }
        if (indexItemsToRemove != null) {
            removedBeans = new ArrayList(extendedTable.remove(indexItemsToRemove));
            removedBeansIndices = Arrays.asList(ArrayUtils.toObject(indexItemsToRemove));
        }
        if (beansToRemove != null) {
            
            int lstSize = beansToRemove.size();
            removedBeansIndices = new ArrayList();
            List beansToRemove2 = new ArrayList(beansToRemove); 
            for (int i = 0; i < lstSize; i++) {
                int index = beansList.indexOf(beansToRemove2.get(i));
                if(index != -1) {
                    removedBeansIndices.add(index);
                } else {
                    beansToRemove.remove(i);
                }
            }
            if (extendedTable.removeAll((Collection) beansToRemove)) {
                removedBeans = new ArrayList(beansToRemove);
            } else {
                removedBeansIndices.clear();
            }
        }
        if (beanToRemove != null) {
            int index = beansList.indexOf(beanToRemove);
            if(extendedTable.remove(beanToRemove)) {
                removedBeans = new ArrayList(1);
                removedBeans.add(beanToRemove);
                removedBeansIndices = new ArrayList(1);
                removedBeansIndices.add(index);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#canUndo()
     */
    @Override
    public boolean canUndo() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#redo()
     */
    @Override
    public synchronized void redo() {
        execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#undo()
     */
    @Override
    public synchronized void undo() {
        
        extendedTable.addAll(removedBeansIndices, removedBeans);
        
    }

}
