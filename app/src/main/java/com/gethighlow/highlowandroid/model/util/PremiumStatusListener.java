package com.gethighlow.highlowandroid.model.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.revenuecat.purchases.PurchaserInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.PurchasesError;
import com.revenuecat.purchases.interfaces.ReceivePurchaserInfoListener;
import com.revenuecat.purchases.interfaces.UpdatedPurchaserInfoListener;

import java.util.ArrayList;
import java.util.List;

public class PremiumStatusListener implements UpdatedPurchaserInfoListener {

    private static final PremiumStatusListener ourInstance = new PremiumStatusListener();

    public static PremiumStatusListener shared() {
        return ourInstance;
    }

    private List<Consumer<PurchaserInfo>> subscribers = new ArrayList<>();

    public void addSubscriber(Consumer<PurchaserInfo> subscriber) {

        //Add the subscriber to the list
        subscribers.add(subscriber);

        //Get the current purchaser info once
        Purchases.getSharedInstance().getPurchaserInfo(new ReceivePurchaserInfoListener() {
            @Override
            public void onReceived(@NonNull PurchaserInfo purchaserInfo) {

                //Give it to the subscriber
                subscriber.accept(purchaserInfo);

            }

            @Override
            public void onError(@NonNull PurchasesError error) {

                //Log the error
                Log.e("Error", error.getMessage());

            }
        });

    }

    public void removeSubscriber(Consumer<PurchaserInfo> subscriber) {

        //Remove the subscriber from the list
        subscribers.remove(subscriber);

    }

    @Override
    public void onReceived(@NonNull PurchaserInfo purchaserInfo) {

        //For each subscriber...
        for (Consumer<PurchaserInfo> subscriber: subscribers) {

            //Run the function
            subscriber.accept(purchaserInfo);

        }

    }
}
