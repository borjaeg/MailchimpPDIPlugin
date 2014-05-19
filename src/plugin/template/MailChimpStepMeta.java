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

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.*;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.*;
import org.pentaho.di.core.row.*;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.*;
import org.pentaho.di.trans.*;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Node;

public class MailChimpStepMeta extends BaseStepMeta implements
		StepMetaInterface {

	private static Class<?> PKG = MailChimpStepMeta.class; // for i18n purposes
	private String outputField;
	private String listId;
	private String campaignId;
	private int operation;

	public MailChimpStepMeta() {
		super();
	}

	public String getOutputField() {
		return outputField;
	}

	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getIdCampaign() {
		return campaignId;
	}

	public void setIdCampaign(String campaignId) {
		this.campaignId = campaignId;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperaton(int operation) {
		this.operation = operation;
	}

	public String getXML() throws KettleValueException {
		String retval = "";
		retval += "		<outputfield>" + getOutputField() + "</outputfield>"
				+ Const.CR;
		retval += "		<listid>" + getListId() + "</listid>" + Const.CR;
		retval += "		<campaignid>" + getIdCampaign() + "</campaignid>" + Const.CR;
		retval += "		<operation>" + getOperation() + "</operation>" + Const.CR;
		return retval;
	}

	public void getFields(RowMetaInterface r, String origin,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {

		// append the outputField to the output
		ValueMetaInterface v = new ValueMeta();
		v.setName("api_key");
		v.setType(ValueMeta.TYPE_STRING);
		v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
		v.setOrigin(origin);

		r.addValueMeta(v);

		v = new ValueMeta();
		v.setName("email");
		v.setType(ValueMeta.TYPE_STRING);
		v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
		v.setOrigin(origin);

		v = new ValueMeta();
		v.setName("operation");
		v.setType(ValueMeta.TYPE_STRING);
		v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
		v.setOrigin(origin);

		r.addValueMeta(v);

	}

	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {

		try {
			setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(
					stepnode, "outputfield")));
			setListId(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode,
					"listid")));
			setIdCampaign(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode,
					"campaignid")));
			setOperaton(Integer.parseInt(XMLHandler.getNodeValue(XMLHandler
					.getSubNode(stepnode, "operation"))));
		} catch (Exception e) {
			throw new KettleXMLException(
					"Template Plugin Unable to read step info from XML node", e);
		}

	}

	public void setDefault() {
		outputField = "MailChimp API Key ";
		listId = "MailChimp List Id";
		campaignId = "Campaign Id";
		operation = Integer.parseInt("0");
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transmeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		// See if we have input streams leading to this step!
		log.logBasic("CHECKING!!!!!!!!!!");
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					"Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
					"No input received from other steps!", stepMeta);
			remarks.add(cr);
		}

	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta,
			TransMeta transMeta, String name) {
		return new MailChimpStepDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new MailChimpStep(stepMeta, stepDataInterface, cnr, transMeta,
				disp);
	}

	public StepDataInterface getStepData() {
		return new MailChimpStepData();
	}

	public void readRep(Repository rep, ObjectId id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			outputField = rep.getStepAttributeString(id_step, "outputfield"); //$NON-NLS-1$
			listId = rep.getStepAttributeString(id_step, "listid"); //$NON-NLS-1$
			campaignId = rep.getStepAttributeString(id_step, "campaignid"); //$NON-NLS-1$
			operation = Integer.parseInt(rep.getStepAttributeString(id_step,
					"listid")); //$NON-NLS-1$
		} catch (Exception e) {
			throw new KettleException(BaseMessages.getString(PKG,
					"TemplateStep.Exception.UnexpectedErrorInReadingStepInfo"),
					e);
		}
	}

	public void saveRep(Repository rep, ObjectId id_transformation,
			ObjectId id_step) throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step,
					"outputfield", outputField); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "listid", listId); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "campaignid", campaignId); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step,
					"operation", operation); //$NON-NLS-1$
		} catch (Exception e) {
			throw new KettleException(BaseMessages.getString(PKG,
					"TemplateStep.Exception.UnableToSaveStepInfoToRepository")
					+ id_step, e);
		}
	}
}
