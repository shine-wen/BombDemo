package com.example.yls.bombdemo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity implements  onDelListener{
    private RecyclerView mRecyclerView;
    private List<Person> personList = new ArrayList<>(); //假设这块内存地址是0xABCD
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //第一：默认初始化
//        Bmob.initialize(this, "7e0d2c0bb90bd831fc7556c9b5638da5");
        //第一：默认初始化
        Bmob.initialize(this, "6d953b9f003bd3e13d403305b8c740ff");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new MyAdapter(personList, MainActivity.this);

        LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

    }

    private void deleteOne() {

        Person p = new Person();
        p.delete("13fd2f557b", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    ((TextView)findViewById(R.id.txt)).setText("success");
                }else{
                    ((TextView)findViewById(R.id.txt)).setText(e.toString());
                }
            }
        });

    }

    private void updateOne() {
        Person p = new Person();
        p.setAge(30);
        p.setAddress("体育西路");
        p.update("13fd2f557b", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    ((TextView)findViewById(R.id.txt)).setText("success");
                }else{
                    ((TextView)findViewById(R.id.txt)).setText(e.toString());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        queryAll();
    }

    private void queryAll() {


        BmobQuery<Person> query = new BmobQuery<>();
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if(e == null){
//                    personList.clear();;
//                    personList.addAll(list);
                    personList = list;  // 改变指向0XABEF
                    mAdapter.changData(personList);
                    ((TextView)findViewById(R.id.txt)).setText("数据返回 " + personList.size());
//                    Toast.makeText(MainActivity.this, "数据返回 ", Toast.LENGTH_LONG).show();
//                    Log.i("Bmob", "数据返回 " + personList.size());
                }else{
                    Log.e("queryAll", e.toString());
                }
            }
        });
    }

    private void queryOne() {
        BmobQuery<Person> query = new BmobQuery<>();
        query.getObject("13fd2f557b", new QueryListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if(e == null){
                    ((TextView)findViewById(R.id.txt)).setText(person.getName() + "  " + person.getAddress());
                }else{
                    ((TextView)findViewById(R.id.txt)).setText(e.toString());
                }
            }
        });
    }

    private void add() {
        Person p1 = new Person();
        p1.setName("桃平");
        p1.setAge(19);
        p1.setAddress("天源路789");
        p1.setScore(98);
        p1.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                ((TextView)findViewById(R.id.txt)).setText(s);
            }
        });
    }

    @Override
    public void del(String name) {
        // 操作数据库实现删除
    }

    @Override
    public void refresh() {
        queryAll();
    }
}
