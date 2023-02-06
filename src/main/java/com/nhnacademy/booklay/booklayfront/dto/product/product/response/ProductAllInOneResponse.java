package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ProductAllInOneResponse {

    ProductResponse info;
    ProductDetailResponse detail;
    SubscribeResponse subscribe;

    @Setter
    private List<RetrieveAuthorResponse> authors = new ArrayList<>();

    @Setter
    private List<RetrieveTagResponse> tags = new ArrayList<>();

    @Setter
    private List<CategoryResponse> categories = new ArrayList<>();


    public String[] getAuthorNameList(){
        String[] names = new String[authors.size()];

        for(int i = 0; i < authors.size(); i++){
            names[i] = authors.get(i).getName();
        }

        return names;
    }

    public String[] getTagList(){
        String[] tag = new String[tags.size()];

        for(int i = 0; i < tags.size(); i++){
            tag[i] = tags.get(i).getName();
        }

        return tag;
    }
}
