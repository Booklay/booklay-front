package com.nhnacademy.booklay.booklayfront.controller.review;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.review.request.ReviewDeleteRequest;
import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.service.ReviewService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.io.IOException;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private static final String REDIRECT_PRODUCT_VIEW = "redirect:/product/view/";
    private static final String REVIEW_PAGE = "?reviewPage=";


    @PostMapping
    public String registerReview(@Valid @ModelAttribute ReviewCreateRequest request,
                                 MultipartFile file) throws IOException {

        HttpStatus status = reviewService.registerReview(request, file);

        if (status.equals(HttpStatus.OK)) {
            throw new BooklayClientException("리뷰를 이미 작성했거나 삭제한 이력이 있습니다.");
        }

        return ControllerUtil.buildString(REDIRECT_PRODUCT_VIEW, request.getProductId().toString());


    }

    @DeleteMapping("/{productId}")
    public String deleteReview(@PathVariable Long productId,
                               @RequestParam(defaultValue = "0") Long reviewPage,
                               @ModelAttribute ReviewDeleteRequest request,
                               MemberInfo memberInfo) {

        if(Objects.isNull(memberInfo.getMemberNo())) {
            throw new BooklayClientException("로그인이 필요합니다.");
        } else if (!request.getWriterNo().equals(memberInfo.getMemberNo())) {
            throw new BooklayClientException("작성자만 삭제 가능합니다.");
        }

        reviewService.deleteReviewById(request.getReviewId());

        return ControllerUtil.buildString(
            REDIRECT_PRODUCT_VIEW, productId.toString(), REVIEW_PAGE, reviewPage.toString());
    }



}
