package com.nhnacademy.booklay.booklayfront.service.category;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final RestService restService;

    private final String gatewayIp;

    public List<CategorySteps> categorySteps() {
        String redirectGatewayPrefix = gatewayIp + "/shop/v1" + "/admin/categories";
        ApiEntity<CategorySteps> categoryStepsApiEntity =
            restService.get(redirectGatewayPrefix + "/steps/1", null,
                CategorySteps.class);

        return categoryStepsApiEntity.getBody().getCategories();
    }

    public CategorySteps getCurrentCategory(List<CategorySteps> categories, Long categoryId){

        CategorySteps categorySteps = null;
        for (CategorySteps category: categories) {
            categorySteps = category.getCategories()
                .stream()
                .filter(x -> x.getId().equals(categoryId))
                .findFirst()
                .orElse(null);
            if (Objects.nonNull(categorySteps)) {
                break;
            }
        }
        return categorySteps;
    }

    public Long getDefaultCategoryId(List<CategorySteps> categories){
        return  categories.get(0).getCategories().get(0).getId();
    }
}
