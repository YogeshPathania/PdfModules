package com.example.smartindiahackathon.ui.epubReader;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import com.example.smartindiahackathon.R;
import com.example.smartindiahackathon.ui.epubReader.adapter.BookAdapter;
import com.example.smartindiahackathon.ui.epubReader.bottomsheet.ToolsBottomSheet;
import com.example.smartindiahackathon.ui.epubReader.view.MyRecycler;
import java.util.ArrayList;
import java.util.List;
import io.hamed.htepubreadr.component.EpubReaderComponent;
import io.hamed.htepubreadr.entity.BookEntity;
import io.hamed.htepubreadr.entity.FontEntity;

public class EpubReaderMainActivity extends AppCompatActivity {

    private MyRecycler rvBook;
    private ImageView btnBack;
    private ImageView btn_setting;
    private List<FontEntity> listFont = new ArrayList<>();
    private EpubReaderComponent epubReader;
    private BookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub_main);
        bindView();

        setTitle(getString(R.string.app_name));
        String epub_location = this.getIntent().getExtras().getString("epub_location");
        onBookReady(epub_location);
       listFont.add(new FontEntity("https://hamedtaherpour.github.io/sample-assets/font/Acme.css", "Acme"));
        listFont.add(new FontEntity("https://hamedtaherpour.github.io/sample-assets/font/IndieFlower.css", "IndieFlower"));
        listFont.add(new FontEntity("https://hamedtaherpour.github.io/sample-assets/font/SansitaSwashed.css", "SansitaSwashed"));
    }
    private void bindView() {
        rvBook = findViewById(R.id.rv_book);
        btnBack = findViewById(R.id.btnBack);
        btn_setting = findViewById(R.id.btn_setting);
        btnBack.setOnClickListener(view -> finish());
        btn_setting.setOnClickListener(view -> openToolsMenu());
    }
    private void openToolsMenu() {
        ToolsBottomSheet sheet = new ToolsBottomSheet();
        sheet.setFontSize(adapter.getFontSize());
        sheet.setAllFontFamily(listFont);
        sheet.setOnChangeFontFamily(position -> {
            adapter.setFontEntity(listFont.get(position));
            adapter.notifyDataSetChanged();
        });
        sheet.setOnChangeFontSize(size -> {
            adapter.setFontSize(size);
            adapter.notifyDataSetChanged();
        });
        sheet.show(getSupportFragmentManager(), sheet.getTag());
    }

    private void onBookReady(String filePath) {
        try {
            epubReader = new EpubReaderComponent(filePath);
            BookEntity bookEntity = epubReader.make(this);
            setUpBookAdapter(bookEntity.getPagePathList());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void setUpBookAdapter(List<String> list) {
        adapter = new BookAdapter(list, epubReader.getAbsolutePath());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvBook.setLayoutManager(layoutManager);
        rvBook.setAdapter(adapter);
        adapter.setFontSize(30);
        adapter.setOnHrefClickListener(href -> gotoPageByHref(href));
        new LinearSnapHelper().attachToRecyclerView(rvBook);
    }

    public void gotoPageByHref(String href) {
        int position = epubReader.getPagePositionByHref(href);
        if (position != EpubReaderComponent.PAGE_NOT_FOUND)
            rvBook.scrollToPosition(position);
    }
}