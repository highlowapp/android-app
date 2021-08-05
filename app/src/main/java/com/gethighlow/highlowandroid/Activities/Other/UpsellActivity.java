package com.gethighlow.highlowandroid.Activities.Other;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.util.PremiumStatusListener;
import com.revenuecat.purchases.EntitlementInfo;
import com.revenuecat.purchases.Offering;
import com.revenuecat.purchases.Offerings;
import com.revenuecat.purchases.Package;
import com.revenuecat.purchases.PurchaserInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.PurchasesError;
import com.revenuecat.purchases.interfaces.MakePurchaseListener;
import com.revenuecat.purchases.interfaces.ReceiveOfferingsListener;
import com.revenuecat.purchases.interfaces.UpdatedPurchaserInfoListener;

public class UpsellActivity extends AppCompatActivity {

    LinearLayout monthlyPurchaseView;
    LinearLayout annualPurchaseView;

    TextView monthlyTitle;
    TextView monthlyPrice;
    TextView annualTitle;
    TextView annualPrice;

    TextView complianceNotice;
    TextView freeTrialNotice;

    Button continueWithPurchase;

    Offering currentOffering;

    String selected = "monthly";

    private Consumer<PurchaserInfo> premiumStatusListener = new Consumer<PurchaserInfo>() {
        @Override
        public void accept(PurchaserInfo purchaserInfo) {

            //Check to see if the user has premium
            checkForProEntitlement(purchaserInfo);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsell);

        monthlyPurchaseView = findViewById(R.id.monthly_purchase);
        annualPurchaseView = findViewById(R.id.annual_purchase);
        complianceNotice = findViewById(R.id.complianceNotice);
        freeTrialNotice = findViewById(R.id.freeTrialNotice);
        monthlyTitle = findViewById(R.id.monthlyTitle);
        monthlyPrice = findViewById(R.id.monthlyPrice);
        annualTitle = findViewById(R.id.annualTitle);
        annualPrice = findViewById(R.id.annualPrice);
        continueWithPurchase = findViewById(R.id.continueWithPurchase);

        //Make links work
        complianceNotice.setMovementMethod(LinkMovementMethod.getInstance());

        //Start by showing nothing
        showScreen(null);

        //If the user's premium status changes, notify us
        PremiumStatusListener.shared().addSubscriber(premiumStatusListener);

        //Set the continue button onClick listener
        continueWithPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Make the purchase
                makePurchase();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get the offerings
        Purchases.getSharedInstance().getOfferings(new ReceiveOfferingsListener() {
            @Override
            public void onReceived(@NonNull Offerings offerings) {

                //Show the offerings on the screen
                showScreen(offerings);

            }

            @Override
            public void onError(@NonNull PurchasesError error) {

                //Log the error
                Log.e("Purchases Sample", error.getMessage());

            }
        });
    }

    private void showScreen(@Nullable Offerings offerings) {

        //If we don't have any offerings...
        if (offerings == null) {

            //Don't show the options
            monthlyPurchaseView.setVisibility(View.INVISIBLE);
            annualPurchaseView.setVisibility(View.INVISIBLE);
            freeTrialNotice.setVisibility(View.INVISIBLE);

        } else {

            //Otherwise, get the current offering
            currentOffering = offerings.getCurrent();

            //As long as the current offering exists...
            if (currentOffering != null) {

                //Make the options visible
                monthlyPurchaseView.setVisibility(View.VISIBLE);
                annualPurchaseView.setVisibility(View.VISIBLE);

                //Set up the buttons
                setupPackageButton(currentOffering.getMonthly(), monthlyPurchaseView);
                setupPackageButton(currentOffering.getAnnual(), annualPurchaseView);

                //Select the monthly plan
                selectPlan(monthlyPurchaseView);

            } else {

                //Log the error
                Log.e("Purchases Sample", "Error loading current offering");

            }
        }
    }

    private void setupPackageButton(@Nullable final Package aPackage, final LinearLayout button) {

        //If the package exists
        if (aPackage != null) {

            //Get the product
            SkuDetails product = aPackage.getProduct();

            //Create the string for the text
            String priceString = "";

            //Get the right string for the plan
            if (button == monthlyPurchaseView) {

                //Set the price string
                priceString = product.getPriceCurrencyCode() + " " + product.getPrice() + " / mo";

                //Put the string in the text
                monthlyPrice.setText(priceString);

            }

            else if (button == annualPurchaseView) {

                //Set the price string
                priceString = product.getPriceCurrencyCode() + " " + product.getPrice() + " / yr";

                //Put the string in the text
                annualPrice.setText(priceString);

            }


            //Set the button onClick listeners
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Select the appropriate plan
                    selectPlan(v);

                }
            });


        } else {

            //Log an error
            Log.e("Purchases Sample", "Error loading package");

        }
    }

    private void selectPlan(View view) {

        //If they selected the monthly plan...
        if (view == monthlyPurchaseView) {

            //Color the annual items gray
            annualTitle.setTextColor( getResources().getColor(R.color.gray) );
            annualPrice.setTextColor( getResources().getColor(R.color.gray) );
            annualPurchaseView.setBackgroundColor( getResources().getColor(R.color.very_light_gray) );

            //Color the monthly items primary
            monthlyTitle.setTextColor( getResources().getColor(R.color.white) );
            monthlyPrice.setTextColor( getResources().getColor(R.color.white) );
            monthlyPurchaseView.setBackgroundColor( getResources().getColor(R.color.colorPrimary) );

            //Set the selected value to monthly
            selected = "monthly";

        }

        //If they selected the annual plan...
        else if (view == annualPurchaseView) {

            //Color the annual items primary
            annualTitle.setTextColor( getResources().getColor(R.color.white) );
            annualPrice.setTextColor( getResources().getColor(R.color.white) );
            annualPurchaseView.setBackgroundColor( getResources().getColor(R.color.colorPrimary) );

            //Color the monthly items gray
            monthlyTitle.setTextColor( getResources().getColor(R.color.gray) );
            monthlyPrice.setTextColor( getResources().getColor(R.color.gray) );
            monthlyPurchaseView.setBackgroundColor( getResources().getColor(R.color.very_light_gray) );

            //Set the selected value to annual
            selected = "annual";

        }

        //If the offering doesn't exist, return
        if (currentOffering == null) return;

        //Placeholder for the current plan
        Package currentPlan = null;

        //Get the current plan
        if ( selected.equals("monthly") ) currentPlan = currentOffering.getMonthly();
        else if ( selected.equals("annual") ) currentPlan = currentOffering.getAnnual();

        //If the plan couldn't be retrieved, return
        if (currentPlan == null) return;

        //Otherwise, get the potential free trial period
        String freeTrialPeriod = currentPlan.getProduct().getFreeTrialPeriod();

        //If the free trial period is not null...
        if (freeTrialPeriod != null) {

            //Split the free trial period
            String[] pieces = freeTrialPeriod.split("");

            //Get the unit
            String timeUnit = "day";
            if (pieces[2].equals("W")) timeUnit = "week";
            else if (pieces[2].equals("M")) timeUnit = "month";
            else if (pieces[2].equals("Y")) timeUnit = "year";

            //Set the free trial text
            freeTrialNotice.setText("Includes " + pieces[1] + " " + timeUnit + " free trial");

            //Make the free trial notice visible
            freeTrialNotice.setVisibility(View.VISIBLE);

        }

        //Otherwise, make the notice invisible
        else {

            freeTrialNotice.setVisibility(View.INVISIBLE);

        }

    }

    private void makePurchase() {

        //If the offering doesn't exist, return
        if (currentOffering == null) return;

        //Placeholder for the package
        Package packageToPurchase = null;

        //Get the package to purchase
        if ( selected.equals("monthly") ) packageToPurchase = currentOffering.getMonthly();
        else if ( selected.equals("annual") ) packageToPurchase = currentOffering.getAnnual();

        //If the package was not able to be retrieved, return
        if (packageToPurchase == null) return;

        //Make the purchase
        Purchases.getSharedInstance().purchasePackage(this, packageToPurchase, new MakePurchaseListener() {
            @Override
            public void onCompleted(@NonNull Purchase purchase, @NonNull PurchaserInfo purchaserInfo) {

                //Check to see if the user has premium
                checkForProEntitlement(purchaserInfo);
            }

            @Override
            public void onError(@NonNull PurchasesError error, boolean userCancelled) {

                //Log an error
                if (!userCancelled) {
                    Log.e("Purchases Sample", error.getMessage());
                }

            }
        });
    }

    private void checkForProEntitlement(PurchaserInfo purchaserInfo) {
        EntitlementInfo proEntitlement = purchaserInfo.getEntitlements().get("Premium");
        if (proEntitlement != null && proEntitlement.isActive()) {

            //Finish the activity
            finish();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Deregister our Premium status listener
        PremiumStatusListener.shared().removeSubscriber(premiumStatusListener);

    }
}
