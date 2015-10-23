// Copyright 2015 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sample.beaconservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "BeaconSampleAPP";
    private GoogleApiClient mGoogleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getFragmentManager().beginTransaction()
        .add(R.id.container, new MainActivityFragment())
        .commit();

      mGoogleApiClient = new GoogleApiClient.Builder(this)
              .addApi(Nearby.MESSAGES_API)
              .addConnectionCallbacks(this)
              .addOnConnectionFailedListener(this)
              .build();

      mGoogleApiClient.connect();

  }


    @Override
    public void onConnected(Bundle bundle) {

        Nearby.Messages.getPermissionStatus(mGoogleApiClient).setResultCallback(
                new ErrorCheckingCallback("getPermissionStatus", new Runnable() {
                    @Override
                    public void run() {

                        Nearby.Messages.subscribe(mGoogleApiClient, new MessageListener() {
                            @Override
                            public void onFound(Message message) {
                                Toast.makeText(getApplicationContext(), message.getNamespace() + " " + message.getType() + " " + message.getContent(), Toast.LENGTH_SHORT).show();
                            }
                        }, Strategy.BLE_ONLY)
                                .setResultCallback(new ErrorCheckingCallback("subscribe()"));
                    }
                }, MainActivity.this)
        );



    }

    @Override
    public void onConnectionSuspended(int i) {

        Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }


}
