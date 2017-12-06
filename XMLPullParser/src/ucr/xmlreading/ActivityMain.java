// demonstrates the reading of XML resource files using 
// a SAX XmlPullParser
// ---------------------------------------------------------------------
package ucr.xmlreading;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityMain extends Activity {

	private TextView txtMsg;
	Button btnGoParser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		txtMsg = (TextView) findViewById(R.id.txtMsg);
		btnGoParser = (Button) findViewById(R.id.btnReadXml);

		btnGoParser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnGoParser.setEnabled(false);
				// do slow XML reading in a separated thread
				Integer xmlResFile = R.xml.manakiki_hole1_v2;
				new backgroundAsyncTask().execute(xmlResFile);
			}
		});
	}// onCreate

	
	// ///////////////////////////////////////////////////////////////////

	public class backgroundAsyncTask extends
			AsyncTask<Integer, Void, StringBuilder> {
		
		ProgressDialog dialog = new ProgressDialog(ActivityMain.this);

		@Override
		protected void onPostExecute(StringBuilder result) {
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
		protected StringBuilder doInBackground(Integer... params) {
			int xmlResFile = params[0];
			XmlPullParser parser = getResources().getXml(xmlResFile);
					
			StringBuilder stringBuilder = new StringBuilder();
			String nodeText = "";
			String nodeName = "";
			try {
				int eventType = -1;
				while (eventType != XmlPullParser.END_DOCUMENT) {

					eventType = parser.next();

					if (eventType == XmlPullParser.START_DOCUMENT) {
						stringBuilder.append("\nSTART_DOCUMENT");

					} else if (eventType == XmlPullParser.END_DOCUMENT) {
						stringBuilder.append("\nEND_DOCUMENT");

					} else if (eventType == XmlPullParser.START_TAG) {
						nodeName = parser.getName();
						stringBuilder.append("\nSTART_TAG: " + nodeName);

						stringBuilder.append(getAttributes(parser));

					} else if (eventType == XmlPullParser.END_TAG) {
						nodeName = parser.getName();
						stringBuilder.append("\nEND_TAG:   " + nodeName );

					} else if (eventType == XmlPullParser.TEXT) {
						nodeText = parser.getText();
						stringBuilder.append("\n    TEXT: " + nodeText);

					}
				}
			} catch (Exception e) {
				Log.e("<<PARSING ERROR>>", e.getMessage());

			}

			return stringBuilder;
		}// doInBackground

		private String getAttributes(XmlPullParser parser) {
			StringBuilder stringBuilder = new StringBuilder();
			// trying to detect inner attributes nested inside a node tag
			String name = parser.getName();
			if (name != null) {
				int size = parser.getAttributeCount();

				for (int i = 0; i < size; i++) {
					String attrName = parser.getAttributeName(i);
					String attrValue = parser.getAttributeValue(i);
					stringBuilder.append("\n    Attrib <key,value>= "
							+ attrName + ", " + attrValue);
				}

			}
			return stringBuilder.toString();

		}// innerElements

	}// backroundAsyncTask

}// ActivityMain