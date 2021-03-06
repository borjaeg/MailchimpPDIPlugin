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

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.nwire.mailchimp.MailChimp;

public class MailChimpStep extends BaseStep implements StepInterface {

	private MailChimpStepData data;
	private MailChimpStepMeta meta;
	private int i = 0;

	public MailChimpStep(StepMeta s, StepDataInterface stepDataInterface,
			int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {
		meta = (MailChimpStepMeta) smi;
		data = (MailChimpStepData) sdi;
		List<String> emails = null;

		Object[] r = getRow(); // get row, blocks when needed!

		// System.out.println("R_1: " + r[0]);

		if (r == null) // no more input to be expected...
		{
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;
			System.out.println("FIRST ROWWWWW");
			// Check if there are any rows from the previous step

			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			logBasic("template step initialized successfully");

		}
		MailChimp mailchimp = new MailChimp();
		log.logBasic("Key:" + meta.getOutputField() + " ; ListId: "
				+ meta.getListId() + "; Campaign Id: " + meta.getIdCampaign());
		mailchimp.initialize(meta.getOutputField(), meta.getListId());

		log.logBasic("Operation selected: " + meta.getOperation());
		log.logBasic("Campaign Id selected: " + meta.getIdCampaign() + " ;");

		log.logBasic(getInputRowMeta().toString());

		String campaign;
		switch (meta.getOperation()) {
		case 0:
			log.logBasic("List Members: " + meta.getOperation());
			emails = mailchimp.listMembers();
			break;
		case 1:
			log.logBasic("Campaign Lists: " + meta.getOperation());
			emails = mailchimp.getCampaigns();
			break;
		case 2:
			log.logBasic("Emails Opened: " + meta.getOperation());
			campaign = meta.getIdCampaign();
			// r[1] must be generalized taking some metadata from previous step
			if (campaign == null || campaign.equals("null")) {
				emails = mailchimp.getEmailsYES((String) r[1]);
				//System.out.println("Previous: " + (String) r[0]);
			} else {
				emails = mailchimp.getEmailsYES(campaign);
			}
			break;
		case 3:
			// r[1] must be generalized taking some metadata from previous step
			log.logBasic("Emails Not Opened: " + meta.getOperation());
			campaign = meta.getIdCampaign();
			if (campaign == null || campaign.equals("")
					|| campaign.equals("null")) {
				emails = mailchimp.getEmailsNO((String) r[1]);
				System.out.println("Previous: " + (String) r[1]);
			} else {
				emails = mailchimp.getEmailsYES(campaign);
			}
			break;
		default:
			log.logError("Incorrect Option selected in operations list");
		}

		System.out.println("Emails: " + emails);
		Object[] outputRow;
		String email;

		for (int i = 0; i < emails.size(); i++) {
			email = emails.get(i);
			// System.out.println("Email: " + i + " " + email);
			// outputRow = new Object[1000];
			r = getRow();
			outputRow = RowDataUtil.addValueData(r,
					data.outputRowMeta.size() - 1, email);
			putRow(data.outputRowMeta, outputRow); // copy row to possible
													// alternate
													// rowset(s)
			outputRow = new Object[1];
		}

		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (MailChimpStepMeta) smi;
		data = (MailChimpStepData) sdi;

		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (MailChimpStepMeta) smi;
		data = (MailChimpStepData) sdi;

		super.dispose(smi, sdi);
	}

	//
	// Run is were the action happens!
	public void run() {
		System.out.println("And finally..." + data.outputRowMeta);
		/*
		 * logDebug("Starting to run..."); try { while (processRow(meta, data)
		 * && !isStopped()) ; } catch (Exception e) {
		 * logError("Unexpected error : " + e.toString());
		 * logError(Const.getStackTracker(e)); setErrors(1); stopAll(); }
		 * finally { System.out.println("And finally..." + data.outputRowMeta);
		 * dispose(meta, data); logBasic("Finished, processing " +
		 * getLinesRead() + " rows"); markStop(); }
		 */
	}

}
