package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        TextView text = findViewById(R.id.privacyTextView);
        text.setText("Information Collected From Users:\n" +
                "\n" +
                "The information collected when user accessing or interacting with our app is separated into mainly the following two parts: \n" +
                "I. Automatic collected information:\n" +
                "Device Information: We collect information about the operating system and version our user use to access our services to provide better user experience.\n" +
                "User facial information: We would need to collect user’s facial information in order to achieve the virtual try on effect, but we will not store this information into our server or database, all this kind of information will only be stored in user’s local device.\n" +
                "II. User Information Choice:\n" +
                "Camera access permission: our app need to access the camera of user’s device in order to enable live presentation of virtual try on effects.\n" +
                "Photo library access permission: User could screenshot preview pictures and save to local photo library. \n" +
                "All the above permission settings could be modified afterwards in user’s system setting menu.\n" +
                " \n" +
                "Information Collected From Other Sources\n" +
                "\n" +
                "In order to provide user with access to direct online shopping, and to provide user with better service in general, we will collaborate with online shopping websites to enable redirection to certain products’ sales page for the convenience of our users.\n" +
                " \n" +
                "Use of Information:\n" +
                "\n" +
                "We use information about users to provide, maintain and improve our services. We store users’ favorite list and browsing history information with user profile information into our database to facilitate user’s future potential purchasing behavior. As our app aims to utilize Augmented Reality to accomplish virtual makeup and accessories try on functionality, we would utilize user’s face information collected via camera to present live try on effects on different products to users.\n" +
                " \n" +
                "Sharing of Information:\n" +
                "\n" +
                "Although we will interact with some third-party source to provide user with shopping function, we will not share user’s information under such circumstances.\n"
        );
    }
}
