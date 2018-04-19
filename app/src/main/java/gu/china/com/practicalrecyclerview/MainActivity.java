package gu.china.com.practicalrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.hz.recyvlerview.MyRecyclerView;
import com.hz.recyvlerview.PracticalRecyclerView;
import com.hz.recyvlerview.mannger.PRGridLayoutManager;
import com.hz.recyvlerview.mannger.PRLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PracticalRecyclerView practicalRecyclerView;
    List<String> list = new ArrayList<>();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        practicalRecyclerView = (PracticalRecyclerView) findViewById(R.id.prv);
        list.add("我来了");
        list.add("我走了");
        list.add("我又来了");
        list.add("我不想走了");
        list.add("我终于走了");
        button = (Button) findViewById(R.id.btn_clear);
        practicalRecyclerView.myRecyclerView.setLayoutManager(new PRLinearLayoutManager(this));
        practicalRecyclerView.myRecyclerView.setAdapter(new RecycleCommonAdapter<String>(this,list,R.layout.item_string) {
            @Override
            public void convert(RecycleCommonViewHolder helper, String item, int position) {
                helper.setText(R.id.tv_name,item);
            }
        });
        practicalRecyclerView.myRecyclerView.loadMoreComplete();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                practicalRecyclerView.myRecyclerView.loadMoreComplete();
            }
        });
    }
}
