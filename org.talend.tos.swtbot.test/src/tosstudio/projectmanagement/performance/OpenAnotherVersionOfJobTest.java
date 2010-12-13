// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package tosstudio.projectmanagement.performance;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.matchers.WidgetOfType;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.talend.swtbot.TalendSwtBotForTos;

/**
 * DOC Administrator class global comment. Detailled comment
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class OpenAnotherVersionOfJobTest extends TalendSwtBotForTos {

    private SWTBotTree tree;

    private SWTBotShell shell;

    private SWTBotView view;

    private static String JOBNAME = "test01"; //$NON-NLS-1$

    @Before
    public void createAJob() {
        view = gefBot.viewByTitle("Repository");
        view.setFocus();
        tree = new SWTBotTree((Tree) gefBot.widget(WidgetOfType.widgetOfType(Tree.class), view.getWidget()));
        tree.setFocus();
        tree.select("Job Designs").contextMenu("Create job").click();

        gefBot.waitUntil(Conditions.shellIsActive("New job"));
        shell = gefBot.shell("New job");
        shell.activate();

        gefBot.textWithLabel("Name").setText(JOBNAME);

        gefBot.button("Finish").click();
        gefBot.waitUntil(Conditions.shellCloses(shell));

        gefBot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
    }

    @Test
    public void openAnotherVersionOfJob() {
        gefBot.cTabItem("Job " + JOBNAME + " 0.1").close();
        tree.expandNode("Job Designs").getNode(JOBNAME + " 0.1").contextMenu("Open an other version").click();

        gefBot.shell("New job").activate();
        gefBot.checkBox("Create new version and open it?").click();
        gefBot.button("M").click();
        gefBot.button("m").click();
        gefBot.button("Finish").click();

        SWTBotCTabItem newJobTabItem1 = gefBot.cTabItem("Job " + JOBNAME + " 1.1");
        Assert.assertNotNull(newJobTabItem1);

        tree.expandNode("Job Designs").getNode(JOBNAME + " 1.1").contextMenu("Open an other version").click();
        gefBot.shell("New job").activate();
        gefBot.table().select(0);
        gefBot.button("Finish").click();

        SWTBotCTabItem newJobTabItem2 = gefBot.cTabItem("Job " + JOBNAME + " 0.1");
        Assert.assertNotNull(newJobTabItem2);
    }

    @After
    public void removePreviouslyCreateItems() {
        gefBot.cTabItem("Job " + JOBNAME + " 0.1").close();
        gefBot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
        gefBot.cTabItem("Job " + JOBNAME + " 1.1").close();
        tree.expandNode("Job Designs").getNode(JOBNAME + " 1.1").contextMenu("Delete").click();

        tree.select("Recycle bin").contextMenu("Empty recycle bin").click();
        gefBot.waitUntil(Conditions.shellIsActive("Empty recycle bin"));
        gefBot.button("Yes").click();
    }
}
