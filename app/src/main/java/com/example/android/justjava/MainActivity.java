package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * 测试代码，学习专用（下单定咖啡应用）
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    int basePrice = 5;
    int toppingPrice = 0;

    String defaultCustomerName = null;
    Boolean hasWhippedCream = false;
    Boolean hasChocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(quantity);
        displayPrice(totalPrice());
        defaultCustomerName = getString(R.string.defaultCustomerName);
    }

    public void increment(View view) {
        if (quantity < 100) {
            quantity += 1;
            display(quantity);
            displayPrice(totalPrice());
        } else {
            Toast.makeText(this, R.string.tipForMaxNumber, Toast.LENGTH_SHORT).show();
        }
    }

    public void decrement(View view) {
        if (quantity > 1) {
            quantity -= 1;
            display(quantity);
            displayPrice(totalPrice());
        } else {
            Toast.makeText(this, R.string.tipForMinNumber, Toast.LENGTH_SHORT).show();
        }
    }

    public void addToppings(View view) {
        int perCream = 1;
        int perChocolate = 2;
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.checkBox_addWhippedCream);
        hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.checkBox_addChocolate);
        hasChocolate = chocolateCheckBox.isChecked();
        switch (view.getId()) {
            case R.id.checkBox_addWhippedCream:
                if (hasWhippedCream) {
                    toppingPrice += perCream;
                } else {
                    toppingPrice -= perCream;
                }
                break;
            case R.id.checkBox_addChocolate:
                if (hasChocolate) {
                    toppingPrice += perChocolate;
                } else {
                    toppingPrice -= perChocolate;
                }
                break;
        }
        displayPrice(totalPrice());
    }

    private String textForAddToppings() {
        String messages = "";
        if (hasWhippedCream) {
            messages += addNewLine(getString(R.string.textRemindForAddWhippedCream));
        }
        if (hasChocolate) {
            messages += addNewLine(getString(R.string.textRemindForAddChocolate));
        }
        if (messages.isEmpty()) {
            return addNewLine(getString(R.string.textRemindForNoToppings));
        } else {
            return messages;
        }
    }

    private String addNewLine(String message) {
        message = getString(R.string.newLineSymbol) + message;
        return message;
    }

    private int totalPrice() {
        return (basePrice + toppingPrice) * quantity;
    }

    private String getCustomerName() {
        EditText CustomerNameTextBox = (EditText) findViewById(R.id.input_CustomerName);
        String CustomerName = CustomerNameTextBox.getText().toString();
        if (CustomerName.isEmpty()) {
            return defaultCustomerName;
        } else {
            return CustomerName;
        }
    }

    public void submitOrder(View view) {
        String priceMessage = getString(R.string.labelTextForCustomerName) + getString(R.string.symbolColon) + getCustomerName();
        priceMessage += textForAddToppings();
        priceMessage += addNewLine(getString(R.string.labelTextForQuantity) + getString(R.string.symbolColon) + quantity);
        priceMessage += addNewLine(getString(R.string.labelTextForTotalPrize) + getString(R.string.symbolColon) + NumberFormat.getCurrencyInstance().format(totalPrice()));
        priceMessage += addNewLine(getString(R.string.textRemindThankYou));
        displayMessage(priceMessage);
        Toast.makeText(this, R.string.textRemindThankYou, Toast.LENGTH_SHORT).show();
        submitOrder(priceMessage);
    }

    private void submitOrder(String messages) {
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse(getString(R.string.emailAddressForReceiveOrder)))
                .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailTitle, getCustomerName()))
                .putExtra(Intent.EXTRA_TEXT, messages);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.textRemindFailedToSendOrder, Toast.LENGTH_SHORT).show();
        }
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantityTextView);
        quantityTextView.setText("" + number);
    }

    /**
     * @param number show number
     */
    private void displayPrice(int number) {
        TextView summaryLabel = (TextView) findViewById(R.id.label_Price);
        summaryLabel.setText(R.string.labelTextForPrice);
        TextView priceTextView = (TextView) findViewById(R.id.summaryTextView);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    private void displayMessage(String message) {
        TextView summaryLabel = (TextView) findViewById(R.id.label_Price);
        summaryLabel.setText(R.string.labelTextForOrderSummary);
        TextView priceTextView = (TextView) findViewById(R.id.summaryTextView);
        priceTextView.setText(message);
    }

}
