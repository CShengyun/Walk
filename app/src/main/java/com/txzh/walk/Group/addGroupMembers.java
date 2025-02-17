package com.txzh.walk.Group;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.txzh.walk.Adapter.GroupAddMembersInfoAdapter;
import com.txzh.walk.Bean.GroupAddInfoBean;
import com.txzh.walk.NetWork.NetWorkIP;
import com.txzh.walk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.txzh.walk.HomePage.WalkHome.context;

public class addGroupMembers extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ib_searchUserReturn;     //返回按钮
    private EditText et_userName;           //输入框
    private ImageButton ibtn_searchUser;    //搜索按钮
    private ListView lv_searchUser;         //列表
    private GroupAddMembersInfoAdapter groupAddInfoAdapter;  //查询群组列表
    private String searchKey;              //搜索框内容
    private Handler handler;
    private List<GroupAddInfoBean> groupAddInfoBeanList = new ArrayList<GroupAddInfoBean>();
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_members);

        init();
    }

    private void init(){
        handler = new Handler();
        ib_searchUserReturn = findViewById(R.id.ib_searchUserReturn);
        ib_searchUserReturn.setOnClickListener(this);
        et_userName = findViewById(R.id.et_userName);
        ibtn_searchUser = findViewById(R.id.ibtn_searchUser);
        ibtn_searchUser.setOnClickListener(this);
        lv_searchUser = findViewById(R.id.lv_searchUser);

        bundle=getIntent().getBundleExtra("groupIdInfo");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_searchUserReturn:
                finish();
                break;
            case R.id.ibtn_searchUser:
                searchKey = et_userName.getText().toString().trim();
                searchUsers(searchKey);
                break;
        }

    }

    //查询用户
    private void searchUsers(final String keyword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("key", keyword)
                        .build();

                Request request = new Request.Builder()
                        .url(NetWorkIP.URL_addMembers)
                        .post(formBody)
                        .build();
                Response response;
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            return;
                        }

                        groupAddInfoBeanList.clear();

                        JSONObject jsonObject = null;
                        String success = null;
                        String message = null;
                        String[] data = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            success = jsonObject.getString("success");
                            message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                GroupAddInfoBean groupAddInfoBean = new GroupAddInfoBean();

                                groupAddInfoBean.setUserID(object.getString("userID"));
                                groupAddInfoBean.setUserHead(object.getString("headPath"));
                                groupAddInfoBean.setUserNickName(object.getString("nickName"));
                                groupAddInfoBean.setUserAccounts(object.getString("userName"));

                                groupAddInfoBeanList.add(groupAddInfoBean);

                                Log.i("aaaa",object.getString("headPath"));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        final String finalSuccess = success;
                        final String finalMessage = message;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(RetrievePassword.this, ""+b, Toast.LENGTH_SHORT).show();
                                groupAddInfoAdapter = new GroupAddMembersInfoAdapter(groupAddInfoBeanList, context, bundle.getString("groupID"));
                                lv_searchUser.setAdapter(groupAddInfoAdapter);
                            }
                        });
                    }
                });
            }
        }).start();
    }
}
