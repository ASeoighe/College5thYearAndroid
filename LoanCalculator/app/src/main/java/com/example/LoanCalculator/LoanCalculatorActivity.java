package com.example.LoanCalculator;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.donnacha.loancalculator.R;
//import com.example.LoanCalculator.R;
import org.json.*;

/** Demonstrates the use of JSON for communicating with a remote HTTP server.
 *  Note that the JSON version that is built into Android is a bit obsolete.
 *  In particular, it lacks a JSONObject constructor that lets you pass a bean.
 *  So, on the Android side we use a Map instead, but the server-side code uses the simpler
 *  and more modern constructor.
 *  <p>
 *  From <a href="http://www.coreservlets.com/android-tutorial/">
 *  the coreservlets.com Android programming tutorial series</a>.

 import android.app.Activity;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
 import android.widget.EditText;
 import android.widget.TextView;
 import org.json.JSONException;
 import org.json.JSONObject;

 import org.apache.http.client.ClientProtocolException;

 import java.io.IOException;

 /** Demonstrates the use of JSON for communicating with a remote HTTP server.
 *  Note that the JSON version that is built into Android is a bit obsolete.
 *  In particular, it lacks a JSONObject constructor that lets you pass a bean.
 *  So, on the Android side we use a Map instead, but the server-side code uses the simpler
 *  and more modern constructor.
 *  <p>
 *  From <a href="http://www.coreservlets.com/android-tutorial/">
 *  the coreservlets.com Android programming tutorial series</a>.
 */
public class LoanCalculatorActivity extends Activity {
    private EditText mBaseUrl, mLoanAmount, mInterestRate, mLoanPeriod;
    private TextView mMontlyPaymentResult, mTotalPaymentsResult;

    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_calculator);
        mBaseUrl = (EditText)findViewById(R.id.base_url);
        mLoanAmount = (EditText)findViewById(R.id.loan_amount);
        mInterestRate = (EditText)findViewById(R.id.interest_rate);
        mLoanPeriod = (EditText)findViewById(R.id.loan_period);
        mMontlyPaymentResult = (TextView)findViewById(R.id.monthly_payment_result);
        mTotalPaymentsResult = (TextView)findViewById(R.id.total_payments_result);
    }

    public void showLoanPayments(View clickedButton) {

        String baseUrl = mBaseUrl.getText().toString();
        String loanAmount = mLoanAmount.getText().toString();
        String interestRate = mInterestRate.getText().toString();
        String loanPeriod = mLoanPeriod.getText().toString();
        LoanInputs inputs = new LoanInputs(loanAmount, interestRate, loanPeriod);
        JSONObject inputsJson = new JSONObject(inputs.getInputMap());
        new HttpAsynchTask().execute(baseUrl, inputsJson.toString());
    }
    public class HttpAsynchTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String jsonString = null;
            try {

                jsonString = HttpUtils.urlContentPost(strings[0], "loanInputs", strings[1]);

            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(result);
                mMontlyPaymentResult.setText(jsonResult.getString("formattedMonthlyPayment"));
                mTotalPaymentsResult.setText(jsonResult.getString("formattedTotalPayments"));
                mLoanAmount.setText(jsonResult.getString("loanAmount"));
                mInterestRate.setText(jsonResult.getString("annualInterestRateInPercent"));
                mLoanPeriod.setText(jsonResult.getString("loanPeriodInMonths"));
            } catch (JSONException ee) {
                mMontlyPaymentResult.setText("JSON exception" + ee.getLocalizedMessage());
            }
        }
    }

}