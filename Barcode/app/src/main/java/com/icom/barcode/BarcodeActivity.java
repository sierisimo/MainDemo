package com.icom.barcode;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarcodeActivity extends AppCompatActivity {

    private static final String TAG = BarcodeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText etInputTelephone = (EditText) findViewById(R.id.et_input_telephone);
        final EditText etInputAmount = (EditText) findViewById(R.id.et_input_amount);
        final TextView etBarcode = (TextView) findViewById(R.id.tv_barcode);
        final LinearLayout llBarcode = (LinearLayout) findViewById(R.id.ll_barcode);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telephone = etInputTelephone.getText().toString();
                String amount = etInputAmount.getText().toString();

                if (TextUtils.isEmpty(telephone)) {
                    etInputTelephone.setError("El campo de teléfono no puede ser nulo");
                } else {
                    if (TextUtils.isEmpty(amount)) {
                        etInputAmount.setError("La cantidad a pagar no puede ser nula");
                    } else {
                        if (!amount.contains(".")) {
                            etInputAmount.setError("Ingresa un número decimal");
                        } else {
                            printBarcode(llBarcode, etBarcode, generateBarcode(telephone, amount));
                        }
                    }
                }
            }
        });
    }

    /**
     * Used to parse a digit to its barcode codification,
     * the n -> narrow & w -> wide indicates the width of
     * the bar/space to be printed
     *
     * @param number the number to parse
     * @return the string codification of the number
     */
    private String parseNumber(char number) {
        switch (number) {
            case '0':
                return "nnwwn";

            case '1':
                return "wnnnw";

            case '2':
                return "nwnnw";

            case '3':
                return "wwnnn";

            case '4':
                return "nnwnw";

            case '5':
                return "wnwnn";

            case '6':
                return "nwwnn";

            case '7':
                return "nnnww";

            case '8':
                return "wnnwn";

            case '9':
                return "nwnwn";

            default:
                return "";
        }
    }

    /**
     * Used to generate a telmex barcode base on a telephone and amount,
     * we assumed that the telephone and amount params have the correct format.
     *
     * @param telephone the telephone number
     * @param amount    the amount to pay
     * @return the barcode
     */
    public String generateBarcode(String telephone, String amount) {
        StringBuilder sbBarcode = new StringBuilder();

        sbBarcode.append(telephone);
        int indexOfDot = amount.indexOf('.');

        for (int j = 0; j != (7 - indexOfDot); j++) {
            sbBarcode.append('0');
        }
        sbBarcode.append(amount.substring(0, indexOfDot));
        sbBarcode.append(amount.substring((indexOfDot + 1), amount.length()));
        sbBarcode.append(generateChecksum(sbBarcode.toString()));

        return sbBarcode.toString();
    }

    /**
     * Used to generate a checksum digit based on the UPC-A system
     *
     * @param barcode the barcode used to generate the checksum digit
     * @return the checksum digit
     */
    private String generateChecksum(String barcode) {
        int oddSum, evenSum, checkSum;

        oddSum = evenSum = 0;

        for (int i = 0; i != barcode.length(); i++) {
            if (i % 2 == 0) {
                oddSum += Character.getNumericValue(barcode.charAt(i)); // add the odd-numbered digits
            } else {
                evenSum += Character.getNumericValue(barcode.charAt(i)); // add the even-numbered digits
            }
        }

        checkSum = oddSum * 3 + evenSum; // multiply the odd-numbered sum by 3 & add the even-numbered sum
        if ((checkSum %= 10) != 0) { //calculate modulo ten of the checksum
            checkSum = 10 - checkSum; // if the modulo is not zero, subtract the result from ten
        } else {
            checkSum = 0;
        }

        return Integer.toString(checkSum);
    }

    /**
     * Used to print a barcode
     *
     * @param lllBarcode View used to print the barcode
     * @param tvBarcode  Text of the barcode
     * @param barcode    The barcode to print
     */
    public void printBarcode(LinearLayout lllBarcode, TextView tvBarcode, String barcode) {
        StringBuilder sbBarcode, sbBars, sbSpaces;
        int size;

        tvBarcode.setText(barcode);
        lllBarcode.removeAllViews();

        sbBarcode = new StringBuilder();
        sbBars = new StringBuilder();
        sbSpaces = new StringBuilder();

//        Head of the barcode
        sbBars.append("nn");
        sbSpaces.append("nn");

//        Numbers parsing
        for (int position = 0; position != barcode.length(); position++) {
            if (position % 2 == 0) {
                sbBars.append(parseNumber(barcode.charAt(position)));
            } else {
                sbSpaces.append(parseNumber(barcode.charAt(position)));
            }
        }

//        Rear of the barcode
        sbBars.append("wn");
        sbSpaces.append("n");

        size = sbBars.length();

        sbBarcode.append(sbBars).append(sbSpaces);

        for (int i = 0; i != size; i++) {
            try {
                View bar = new View(this);
                View space = new View(this);

                bar.setBackgroundResource(R.color.black);
                bar.setLayoutParams(getLayoutParams(sbBarcode.charAt(i)));
                lllBarcode.addView(bar);

                space.setBackgroundResource(R.color.white);
                space.setLayoutParams(getLayoutParams(sbBarcode.charAt(i + size)));
                lllBarcode.addView(space);

            } catch (IndexOutOfBoundsException ioobe) {
                ioobe.printStackTrace();
            }
        }
    }

    /**
     * Used to generate a {@link android.view.ViewGroup.LayoutParams} to be used
     * in the barcode view.  The width of the the view is specified by the width param
     *
     * @param width character that indicates the width of the LayoutParams
     * @return the LayoutParams to be used in the view
     */
    private ViewGroup.LayoutParams getLayoutParams(char width) {
        switch (width) {
            case 'n':
                return new ViewGroup.LayoutParams(3, ViewGroup.LayoutParams.MATCH_PARENT);

            case 'w':
                return new ViewGroup.LayoutParams(9, ViewGroup.LayoutParams.MATCH_PARENT);

            default:
                return null;
        }
    }
}
