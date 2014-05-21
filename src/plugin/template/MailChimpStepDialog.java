/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar HASSAN.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

package plugin.template;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class MailChimpStepDialog extends BaseStepDialog implements
		StepDialogInterface {

	private static Class<?> PKG = MailChimpStepMeta.class; // for i18n purposes

	private MailChimpStepMeta input;

	// output field name
	private Label wlValName;
	private Text wValName;
	private Label lListId;
	private Text tListId;
	private Label lCampaignId;
	private Text tCampaignId;
	private Label lListOperations;
	private List listOperations;
	private Label lCampaignIdPreviousStep;
	private Button idCampaignPreviousStep;
	private FormData fdlValName, fdValName, ldValName, ldlValName, ddlValname,
			ddValname, ldValCamapign, ddValCampaign, fdIdCampaignPreviousStep,
			ldIdCampaignPreviousStep;

	public MailChimpStepDialog(Shell parent, Object in, TransMeta transMeta,
			String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		input = (MailChimpStepMeta) in;
	}

	public String open() {
		Shell parent = getParent();
		Display display = parent.getDisplay();

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN
				| SWT.MAX);
		props.setLook(shell);
		setShellImage(shell, input);

		ModifyListener lsMod = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				input.setChanged();
			}
		};

		changed = input.hasChanged();

		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(BaseMessages.getString(PKG, "Template.Shell.Title"));

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		wlStepname = new Label(shell, SWT.RIGHT);
		wlStepname
				.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right = new FormAttachment(middle, -margin);
		fdlStepname.top = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);

		wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(0, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);

		// Api Key
		wlValName = new Label(shell, SWT.RIGHT);
		wlValName.setText(BaseMessages.getString(PKG,
				"Template.FieldName.Label"));
		props.setLook(wlValName);
		fdlValName = new FormData();
		fdlValName.left = new FormAttachment(0, 0);
		fdlValName.right = new FormAttachment(middle, -margin);
		fdlValName.top = new FormAttachment(wStepname, margin);
		wlValName.setLayoutData(fdlValName);

		wValName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wValName);
		wValName.addModifyListener(lsMod);
		fdValName = new FormData();
		fdValName.left = new FormAttachment(middle, 0);
		fdValName.right = new FormAttachment(100, 0);
		fdValName.top = new FormAttachment(wStepname, margin);
		wValName.setLayoutData(fdValName);

		// List Id
		lListId = new Label(shell, SWT.RIGHT);
		lListId.setText(BaseMessages.getString(PKG, "Template.FieldList.Label"));
		props.setLook(lListId);
		ldlValName = new FormData();
		ldlValName.left = new FormAttachment(0, 0);
		ldlValName.right = new FormAttachment(middle, -margin);
		ldlValName.top = new FormAttachment(wValName, margin);
		lListId.setLayoutData(ldlValName);

		tListId = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(tListId);
		tListId.addModifyListener(lsMod);
		ldValName = new FormData();
		ldValName.left = new FormAttachment(middle, 0);
		ldValName.right = new FormAttachment(100, 0);
		ldValName.top = new FormAttachment(wValName, margin);
		tListId.setLayoutData(ldValName);

		// Form Campaign Id from previous step
		lCampaignIdPreviousStep = new Label(shell, SWT.RIGHT);
		lCampaignIdPreviousStep.setText(BaseMessages.getString(PKG,
				"Template.FormCampaign.Label"));
		props.setLook(lCampaignIdPreviousStep);
		ldIdCampaignPreviousStep = new FormData();
		ldIdCampaignPreviousStep.left = new FormAttachment(0, 0);
		ldIdCampaignPreviousStep.right = new FormAttachment(middle, -margin);
		ldIdCampaignPreviousStep.top = new FormAttachment(tListId, margin);
		lCampaignIdPreviousStep.setLayoutData(ldIdCampaignPreviousStep);

		idCampaignPreviousStep = new Button(shell, SWT.CHECK);
		props.setLook(idCampaignPreviousStep);
		// idCampaignPreviousStep.addModifyListener(lsMod);
		fdIdCampaignPreviousStep = new FormData();
		fdIdCampaignPreviousStep.left = new FormAttachment(middle, 0);
		fdIdCampaignPreviousStep.right = new FormAttachment(100, 0);
		fdIdCampaignPreviousStep.top = new FormAttachment(tListId, margin);
		idCampaignPreviousStep.setLayoutData(fdIdCampaignPreviousStep);

		// Campaign Id
		lCampaignId = new Label(shell, SWT.RIGHT);
		lCampaignId.setText(BaseMessages.getString(PKG,
				"Template.FieldCampaign.Label"));
		props.setLook(lCampaignId);
		ldValCamapign = new FormData();
		ldValCamapign.left = new FormAttachment(0, 0);
		ldValCamapign.right = new FormAttachment(middle, -margin);
		ldValCamapign.top = new FormAttachment(idCampaignPreviousStep, margin);
		lCampaignId.setLayoutData(ldValCamapign);

		tCampaignId = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(tCampaignId);
		tCampaignId.addModifyListener(lsMod);
		ddValCampaign = new FormData();
		ddValCampaign.left = new FormAttachment(middle, 0);
		ddValCampaign.right = new FormAttachment(100, 0);
		ddValCampaign.top = new FormAttachment(idCampaignPreviousStep, margin);
		tCampaignId.setLayoutData(ddValCampaign);

		// Operations
		lListOperations = new Label(shell, SWT.RIGHT);
		lListOperations.setText(BaseMessages.getString(PKG,
				"Template.FieldOperations.Label"));
		props.setLook(lListOperations);
		ddlValname = new FormData();
		ddlValname.left = new FormAttachment(0, 0);
		ddlValname.right = new FormAttachment(middle, -margin);
		ddlValname.top = new FormAttachment(tCampaignId, margin);
		lListOperations.setLayoutData(ddlValname);

		listOperations = new List(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(listOperations);
		// listOperations.addModifyListener(lsMod);
		listOperations.add(BaseMessages.getString(PKG,
				"Template.FieldOperations.Field_1"));
		listOperations.add(BaseMessages.getString(PKG,
				"Template.FieldOperations.Field_2"));
		listOperations.add(BaseMessages.getString(PKG,
				"Template.FieldOperations.Field_3"));
		listOperations.add(BaseMessages.getString(PKG,
				"Template.FieldOperations.Field_4"));
		ddValname = new FormData();
		ddValname.left = new FormAttachment(middle, 0);
		ddValname.right = new FormAttachment(100, 0);
		ddValname.top = new FormAttachment(tCampaignId, margin);
		listOperations.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				// if(listOperations.getSelectionCount() > 0)
				// System.out.println(Arrays.toString(listOperations.getSelection()));
				System.out.println("Index: "
						+ listOperations.getSelectionIndex());
				input.setChanged();
			}
		});
		listOperations.setLayoutData(ddValname);

		// OK and cancel buttons
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

		BaseStepDialog.positionBottomButtons(shell,
				new Button[] { wOK, wCancel }, margin, listOperations);

		// Add listeners
		lsCancel = new Listener() {
			public void handleEvent(Event e) {
				cancel();
			}
		};
		lsOK = new Listener() {
			public void handleEvent(Event e) {
				ok();
			}
		};

		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);

		lsDef = new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				ok();
			}
		};

		wStepname.addSelectionListener(lsDef);
		wValName.addSelectionListener(lsDef);

		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});

		// Set the shell size, based upon previous time...
		setSize();

		getData();
		input.setChanged(changed);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return stepname;
	}

	// Read data and place it in the dialog
	public void getData() {
		wStepname.selectAll();
		wValName.setText(input.getOutputField());
		tListId.setText(input.getListId());
		if (input.getIdCampaign() == null)
			tCampaignId.setText("Id Campaign");
		else
			tCampaignId.setText(input.getIdCampaign());
		listOperations.select(input.getOperation());
	}

	private void cancel() {
		stepname = null;
		input.setChanged(changed);
		dispose();
	}

	// let the plugin know about the entered data
	private void ok() {
		stepname = wStepname.getText(); // return value
		input.setOutputField(wValName.getText());
		input.setListId(tListId.getText());
		input.setOperaton(listOperations.getSelectionIndex());
		if (tCampaignId.equals(""))
			input.setIdCampaign("Id Campaign");
		else
			input.setIdCampaign(tCampaignId.getText());
		log.logBasic("Key:" + input.getOutputField() + " ; ListId: "
				+ input.getListId());
		dispose();
	}
}
