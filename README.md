[![](https://jitpack.io/v/aileelucky/PracticalRecyclerView.svg)](https://jitpack.io/#aileelucky/PracticalRecyclerView)

# PracticalRecyclerView



## xml里面设置
```xml
<com.hz.recyvlerview.PracticalRecyclerView
            android:id="@+id/vst"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            />
```

## 代码中实现（简单示例）
```java
 practicalRecyclerView = (PracticalRecyclerView) findViewById(R.id.prv);

 practicalRecyclerView.myRecyclerView.setLayoutManager(new PRLinearLayoutManager(this));
        practicalRecyclerView.myRecyclerView.setAdapter(new RecycleCommonAdapter<String>(this,list,R.layout.item_string) {
            @Override
            public void convert(RecycleCommonViewHolder helper, String item, int position) {
                
            }
        });
        practicalRecyclerView.myRecyclerView.loadMoreComplete();
```


## 描述
自定义一个包含上拉加载更多、空白页面的Recycler
```java
compile 'com.github.aileelucky:PracticalRecyclerView:V1.0.0'
````

## 更新点 
>v1.0.0

