import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/loan-calculator")
public class LoanCalculator extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inputString = request.getParameter("loanInputs");
        // Example input: { "amount": 100000, "rate": 5.5, "months": 360 };
        // Example String: String inputString = "{ \"amount\": 100000, \"rate\": 5.5, \"months\": 360 }";

        double loanAmount = 200000;
        double annualInterestRateInPercent = 5.5;
        long loanPeriodInMonths = 360;
        try {

            JSONObject inputValues = new JSONObject(inputString);
            loanAmount = inputValues.getDouble("amount");
            annualInterestRateInPercent = inputValues.getDouble("rate");
            loanPeriodInMonths = inputValues.getLong("months");
            System.out.print("Excecuted!");

        } catch (Exception e) {  // NullPointerException and JSONException
            // Use default values assigned before the try block
        }
        PaymentInfo info = new PaymentInfo(loanAmount, annualInterestRateInPercent, loanPeriodInMonths);
        PrintWriter out = response.getWriter();

        out.println(new JSONObject(info));


    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
