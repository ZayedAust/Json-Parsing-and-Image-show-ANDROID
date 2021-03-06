package com.example.taskforinterview2;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    private ArrayList<UsersDetails>usersDetailsArrayList;
    private ListView userLV;
    private UserListAdapter userListAdapter;
    private ProgressDialog mProgress;
    private ImageView imageView;
    private final String IMAG_URL="https://randomuser.me/api/portraits/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = APIUser.getUser().create(APIInterface.class);
        usersDetailsArrayList=new ArrayList<>();
        userLV=findViewById(R.id.LV_userDetailsShow);

        //############################ showing PROGRESS BAR ######################################
        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setMessage("gettind data...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        /**
         Parsing Json
         **/
        mProgress.show();
        Call<JsonResponse> call = apiInterface.doGetUserDetails();
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                Log.d("Response TAG",response.code()+"");

                if(response.code() == 200) {
                    JsonResponse resource = response.body();
                    for (int i = 0; i < resource.getUsers().size(); i++) {
                        int id = resource.getUsers().get(i).getId();
                        String firstName = resource.getUsers().get(i).getFirstName();
                        String lastName = resource.getUsers().get(i).getLastName();
                        String mobile = resource.getUsers().get(i).getPhones().getMobile();
                        String gender = resource.getUsers().get(i).getGender();
                        int photo = resource.getUsers().get(i).getPhoto();
                        String url;
                        if(gender.equals("female"))
                        {
                            url=IMAG_URL+"women"+"/"+String.valueOf(photo)+".jpg";
                        }
                        else
                        {
                            url=IMAG_URL+"men"+"/"+String.valueOf(photo)+".jpg";
                        }




                        usersDetailsArrayList.add(new UsersDetails(id,firstName,lastName,mobile,gender,photo,url));
                    }
                    userListAdapter=new UserListAdapter(MainActivity.this,usersDetailsArrayList);
                    userLV.setAdapter(userListAdapter);
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                call.cancel();
            }
        });





    }

    void loadImageFromURL(){
        Picasso.with(this).load("https://randomuser.me/api/portraits/women/41.jpg").placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
