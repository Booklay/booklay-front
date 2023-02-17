package com.nhnacademy.booklay.booklayfront.service.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final RestService restService;
    private final ObjectMapper objectMapper;

    private static final String CATEGORY_ADMIN_API_URI = "/shop/v1/admin/categories";

    private final String gatewayIp;

    @Cacheable(value = "categoryStep")
    public List<CategorySteps> categorySteps() {
        String redirectGatewayPrefix = gatewayIp + CATEGORY_ADMIN_API_URI;
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

    @CacheEvict(value = "categoryStep", allEntries = true)
    public Optional<CategoryResponse> createCategory(CategoryCreateRequest createRequest) {
        URI uri = URI.create(gatewayIp + CATEGORY_ADMIN_API_URI);

        ApiEntity<CategoryResponse> response =
            restService.post(uri.toString(), objectMapper.convertValue(createRequest, Map.class),
                CategoryResponse.class);

        if (response.isSuccess()){
            return Optional.of(response.getBody());
        }

        return Optional.empty();
    }

    @CacheEvict(value = "categoryStep", allEntries = true)
    public void deleteCategory(Long id) {

        URI uri = URI.create(gatewayIp + CATEGORY_ADMIN_API_URI + "/" + id);

        restService.delete(uri.toString());

    }

    @CacheEvict(value = "categoryStep", allEntries = true)
    public Optional<CategoryResponse> updateCategory(CategoryUpdateRequest request,
                                                    Long id) {

        URI uri = URI.create(gatewayIp + CATEGORY_ADMIN_API_URI + "/" + id);

        ApiEntity<CategoryResponse> categoryResponse =
            restService.put(uri.toString(), objectMapper.convertValue(request, Map.class),
                CategoryResponse.class);

        if (categoryResponse.isSuccess()){
            return  Optional.of(categoryResponse.getBody());
        }
        return Optional.empty();
    }
}
