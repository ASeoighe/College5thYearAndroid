package com.example.xmlreading_w3c_documentbuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView txtMsg;
	Button btnGoParser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtMsg = (TextView) findViewById(R.id.txtMsg);
		btnGoParser = (Button) findViewById(R.id.btnReadXml);

		btnGoParser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnGoParser.setEnabled(false);

				// this xml file includes elements: <course>,
				// <name>, <coordinates> for justa few holes
				String xmlFile = "manakiki_holes1and2.kml";
				// do slow XML reading in a separated thread
				new backgroundAsyncTask().execute(xmlFile);

			}
		});
	}// onCreate
	
	// /////////////////////////////////////////////////////////////////////////
	private class backgroundAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog dialog = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			txtMsg.setText(result.toString());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			String xmlFileName = params[0];
			return useW3CParser(xmlFileName);
		}// doInBackground

	}// backgroundAsyncTask

	// ///////////////////////////////////////////////////////////////////
	private String useW3CParser(String xmlFileName) {
		StringBuilder str = new StringBuilder();
		try {
			String kmlFile = Environment.getExternalStorageDirectory()
					.getPath() + "/" + xmlFileName;

			InputStream is = new FileInputStream(kmlFile);

			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document document = docBuilder.parse(is);

			if (document == null) {
				Log.v("REALLY BAD!!!!", "document was NOT made by parser");
				return "BAD-ERROR";
			}
			// put all data into NodeLists
			NodeList listNameTag = document.getElementsByTagName("name");
			NodeList listCoordinatesTag = document
					.getElementsByTagName("coordinates");
			NodeList listCourseTag = document.getElementsByTagName("course");

			// traverse NodeLists for elements: <name>, <coordinates>, <course>.
			str.append(getAllDataFromNodeList(listNameTag, "name"));
			str.append(getAllDataFromNodeList(listCoordinatesTag, "coordinates"));
			str.append(getAllDataFromNodeList(listCourseTag, "course"));

		} catch (FileNotFoundException e) {
			Log.e("W3C Error", e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e("W3C Error", e.getMessage());
		} catch (SAXException e) {
			Log.e("W3C Error", e.getMessage());
		} catch (IOException e) {
			Log.e("W3C Error", e.getMessage());
		}
		return str.toString();
	}// useW3cOrgDocumentBuilder

	private Object getAllDataFromNodeList(NodeList list, String strElementName) {
		StringBuilder str = new StringBuilder();
		// dealing with the <strElementName> tag
		str.append("\n\nNodeList for:  <" + strElementName + "> Tag");
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			int size = node.getAttributes().getLength();
			String text = node.getTextContent();
			str.append("\n " + i + ": " + text);

			// get all attributes of the current element (i-th hole)
			for (int j = 0; j < size; j++) {
				String attrName = node.getAttributes().item(j).getNodeName();
				String attrValue = node.getAttributes().item(j).getNodeValue();

				str.append("\n attr. info-" + i + "-" + j + ": " + attrName
						+ " " + attrValue);
			}
		}

		return str;
	}//getAllDataFromNodeList


}// ActivityMain
