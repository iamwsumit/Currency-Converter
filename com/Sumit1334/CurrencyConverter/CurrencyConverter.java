package com.Sumit1334.CurrencyConverter;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@DesignerComponent(description = "An extension for converting any currency to any currency by Sumit Kumar", version = 1, iconName = "https://community.kodular.io/user_avatar/community.kodular.io/sumit1334/120/82654_2.png",nonVisible = true,category = ComponentCategory.EXTENSION)
@SimpleObject(external = true)
public class CurrencyConverter extends AndroidNonvisibleComponent {

    private static String from="USD";
    private static String to="INR";
    private final Activity activity;
    private static String url;
    private static int amount;

    public CurrencyConverter(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();
    }



    @SimpleEvent(description = "This event raises when got the price")
    public void Got(double amount){
        EventDispatcher.dispatchEvent(this, "Got", amount);
    }
    @SimpleEvent(description = "This event raises when any error occur")
    public void ErrorOccured(String error){
        EventDispatcher.dispatchEvent(this, "ErrorOccured", error);
    }
    @SimpleFunction(description = "convert any amount to given currency")
    public void Get(int amount){
        final StringBuilder sb=new StringBuilder();
        sb.append("https://www.mataf.net/en/currency/converter-");
        sb.append(this.from);
        sb.append("-");
        sb.append(this.to);
        sb.append("?m1=");
        sb.append(amount);
        this.url=sb.toString();
        this.amount=amount;
        new getprize().start();
    }

    @SimpleProperty(description = "return the current Currency From")
    public String From(){
        return this.from;
    }
    @SimpleProperty(description = "return the current Currency to")
    public String To(){
        return this.to;
    }
    @DesignerProperty(editorType = "String",defaultValue = "INR")
    @SimpleProperty(description = "Set the currency type of that you want to get the prize")
    public void To(String to){
        this.to=to;
    }
    @DesignerProperty(editorType = "String",defaultValue = "USD")
    @SimpleProperty(description = "Set the currency type from that you want to get the prize")
    public void From(String to){
        this.from=to;
    }


    public class getprize extends Thread {
        int am=amount;
        private void get() {
            String status="Getting data";
            
        }

        public void run() {
            try {
                URL url=new URL(CurrencyConverter.url);
                HttpURLConnection h= (HttpURLConnection) url.openConnection();
                InputStream im=h.getInputStream();
                BufferedReader bf=new BufferedReader(new InputStreamReader(im));
                final String data=bf.readLine();
                CurrencyConverter.this.activity.runOnUiThread((Runnable) new Runnable(){
                    String dat=data;
                    @Override
                    public void run() {
                        try {
                            int l="description\" content=\"ll➤ 【".length();
                            int st=dat.indexOf("description\" content=\"ll➤ 【");
                            String segment1 = dat.substring((st+l),st+l+20);
                            String segment2 = segment1.substring(segment1.indexOf("= ") + 2, segment1.indexOf("】"));
                            double price=Double.parseDouble(segment2.replaceAll(segment2.substring(0,1),""))*CurrencyConverter.this.amount;
                            CurrencyConverter.this.Got(price);
                        }catch (Exception e){
                            CurrencyConverter.this.ErrorOccured(e.toString());
                        }
                    }
                });
            } catch (MalformedURLException e) {
                CurrencyConverter.this.ErrorOccured(e.toString());
            } catch (IOException e) {
                CurrencyConverter.this.ErrorOccured(e.toString());
            }
        }
    }


}
