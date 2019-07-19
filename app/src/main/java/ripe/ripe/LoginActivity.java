package ripe.ripe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private String name;
    private String email;
    private Button btn_fb_login;
    public static final String FB_NAME = "com.example.ripe.name";
    public static final String FB_EMAIL = "com.example.ripe.email";
    public static final String FB_IMAGE = "com.example.ripe.image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        if(isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, NavActivity.class);
            intent.putExtra(FB_NAME, name);
            intent.putExtra(FB_EMAIL, email);
            finish();
            startActivity(intent);
        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ripe.ripe.LoginActivity.this, "Error Logging In", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ripe.ripe.LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        btn_fb_login = (Button) findViewById(R.id.btn_fb_login);

        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(ripe.ripe.LoginActivity.this, Arrays.asList("public_profile", "email", "user_friends"));
            }
        });
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                // user is logged out
            }
            else {
                Intent intent = new Intent(LoginActivity.this, NavActivity.class);
                intent.putExtra(FB_NAME, name);
                intent.putExtra(FB_EMAIL, email);
                startActivity(intent);
            }
        }
    };

    private void loadProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                String firstName = "";
                String lastName = "";
                String emailA = "";
                try {
                    firstName = object.getString("first_name");
                    lastName = object.getString("last_name");
                    emailA = object.getString("email");
                }
                catch (JSONException e ) {}
                name = firstName + " " + lastName;
                email = emailA;
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /** sample code to upload to our blob storage **/
    private static final String storageURL = "BLOB_STORAGE_URL";
    private static final String storageContainer = "ribeblol";
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=ripeblob;AccountKey=ZbG1DUXzpTAJfZJM2s3TlifmUEI/gj/pw5acLv0Ht0uqniOVYYB41r0tAulZB53+NtXDCUruUFplXtfdqQE30w==;EndpointSuffix=core.windows.net";

    protected void storeImageInBlobStorage(String imgPath){
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(storageContainer);
            // Create or overwrite the blob (with the name "example.jpeg") with contents from a local file.
            CloudBlockBlob blob = container.getBlockBlobReference("example.jpg");
            File source = new File(imgPath);
            blob.upload(new FileInputStream(source), source.length());
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}
