package com.nhnacademy.booklay.booklayfront.service.coupon;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CouponAdminService {
    Long saveCouponImage(MultipartFile image) throws IOException;
}
