package com.nhnacademy.booklay.booklayfront.controller.review;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.review.request.ReviewDeleteRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private static final String REDIRECT_REVIEW_UNOWNED_ERROR = "redirect:/review/unowned/";
    private static final String REDIRECT_REVIEW_UNAUTHORIZED_ERROR = "redirect:/review/unauthenticated/";
    private static final String REDIRECT_REVIEW_ALREADY_ERROR = "redirect:/review/alreadyReview/";
    private static final String REDIRECT_PRODUCT_VIEW = "redirect:/product/view/";
    private static final String REVIEW_PAGE = "?reviewPage=";

    private static final String ERROR_MESSAGE = "reviewError";


    @PostMapping
    public String registerReview(@Valid @ModelAttribute ReviewCreateRequest request,
                                 MultipartFile file) throws IOException {

        HttpStatus status = reviewService.registerReview(request, file);

        if (status.equals(HttpStatus.OK)) {
            return ControllerUtil.buildString(
                REDIRECT_REVIEW_ALREADY_ERROR, request.getProductId().toString());
        }

        return ControllerUtil.buildString(REDIRECT_PRODUCT_VIEW, request.getProductId().toString());


    }

    @DeleteMapping("/{productId}")
    public String deleteReview(@PathVariable Long productId,
                               @RequestParam(defaultValue = "0") Long reviewPage,
                               @ModelAttribute ReviewDeleteRequest request,
                               MemberInfo memberInfo) {

        if(Objects.isNull(memberInfo.getMemberNo())) {
            return ControllerUtil.buildString(
                REDIRECT_REVIEW_UNAUTHORIZED_ERROR, productId.toString(), REVIEW_PAGE , reviewPage.toString());
        } else if (!request.getWriterNo().equals(memberInfo.getMemberNo())) {
            return ControllerUtil.buildString(
                REDIRECT_REVIEW_UNOWNED_ERROR, productId.toString(), REVIEW_PAGE , reviewPage.toString());
        }

        reviewService.deleteReviewById(request.getReviewId());

        return ControllerUtil.buildString(
            REDIRECT_PRODUCT_VIEW, productId.toString(), REVIEW_PAGE, reviewPage.toString());
    }

    @GetMapping("/unowned/{productId}")
    public String occurUnownedError(@PathVariable Long productId,
                                @RequestParam(defaultValue = "0") Long reviewPage,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "작성자만 삭제 가능합니다.");
        return ControllerUtil.buildString(
            REDIRECT_PRODUCT_VIEW, productId.toString(), REVIEW_PAGE, reviewPage.toString());
    }
    @GetMapping("/unauthenticated/{productId}")
    public String occurUnauthenticatedError(@PathVariable Long productId,
                                @RequestParam(defaultValue = "0") Long reviewPage,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "로그인이 필요합니다.");
        return ControllerUtil.buildString(
            REDIRECT_PRODUCT_VIEW, productId.toString(), REVIEW_PAGE, reviewPage.toString());
    }

    @GetMapping("/alreadyReview/{productId}")
    public String occurAlreadyError(@PathVariable Long productId,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "리뷰를 이미 작성했거나 삭제한 이력이 있습니다.");
        return ControllerUtil.buildString(
            REDIRECT_PRODUCT_VIEW, productId.toString());
    }

}
