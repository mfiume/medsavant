/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ut.biolab.medsavant.view.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.ut.biolab.medsavant.view.swing.WrapLayout;
import org.ut.biolab.medsavant.view.util.PaintUtil;
import org.ut.biolab.medsavant.view.util.ViewUtil;

/**
 *
 * @author mfiume
 */
public abstract class DetailedView extends JPanel {

    private final JLabel title;
    private final JPanel contentPanel;
    protected SplitScreenView parent;
    private final JPanel bottomPanel;
    private final Component glue;

    public DetailedView() {
        this.setPreferredSize(new Dimension(9999, 350));

        this.setOpaque(false);

        this.setLayout(new BorderLayout());

        JPanel h1 = new JPanel();
        h1.setBorder(ViewUtil.getBigBorder());
        //BorderFactory.createCompoundBorder(
        //        ViewUtil.getTinyLineBorder(), ViewUtil.getBigBorder()
        //        ));

        h1.setLayout(new BoxLayout(h1, BoxLayout.X_AXIS));
        this.title = ViewUtil.getDetailTitleLabel("");
        h1.add(Box.createHorizontalGlue());
        h1.add(title);
        h1.add(Box.createHorizontalGlue());

        this.add(h1, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setBorder(ViewUtil.getMediumBorder());

        this.add(contentPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        ViewUtil.applyHorizontalBoxLayout(bottomPanel);
        bottomPanel.add(Box.createHorizontalGlue());

        glue = Box.createHorizontalGlue();
        bottomPanel.add(glue);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    public abstract void setSelectedItem(Object[] selectedRow);

    public abstract void setMultipleSelections(List<Object[]> selectedRows);

    public abstract void setRightClick(MouseEvent e);

    public void setTitle(String str) {
        this.title.setText(str);
    }

    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    public void setSplitScreenParent(SplitScreenView parent) {
        this.parent = parent;
    }

    public void addBottomComponent(Component c) {
        bottomPanel.add(glue);
        bottomPanel.add(c);
        bottomPanel.add(glue);
    }
}