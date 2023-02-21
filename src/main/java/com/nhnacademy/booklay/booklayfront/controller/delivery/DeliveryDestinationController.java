package com.nhnacademy.booklay.booklayfront.controller.delivery;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.booklayfront.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequestMapping("/member/profile/address")
public class DeliveryDestinationController {
    private final RestTemplate restTemplate;
    private final String redirectGatewayPrefix;

    public DeliveryDestinationController(RestTemplate restTemplate, String gateway) {
        this.restTemplate = restTemplate;
        redirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + "/delivery/destination";
    }

    @PostMapping("/register")
    public String create(@Valid @ModelAttribute DeliveryDestinationCURequest requestDto,
                         BindingResult bindingResult,
                         MemberInfo memberInfo,
                         Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = URI.create(redirectGatewayPrefix + "/" + memberInfo.getMemberNo());

        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(requestDto), headers,
                HttpMethod.POST, uri);

        try {
            restTemplate.exchange(requestEntity, Void.class);
        } catch (BooklayClientException e) {
            throw new BooklayClientException("배송지는 10개를 넘을 수 없습니다.");
        }
        return "complete";
    }

    @GetMapping("/register")
    public String retrieveCreateForm(MemberInfo memberInfo,
                                     Model model) {
        model.addAttribute("memberNo", memberInfo.getMemberNo());
        return "mypage/member/memberAddressRegisterForm";
    }

    @GetMapping
    public String retrieveAddress(Model model, MemberInfo memberInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/list/" + memberInfo.getMemberNo());

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<List<DeliveryDestinationRetrieveResponse>> response =
            restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<>() {
                });

        model.addAttribute("addresses", response.getBody());
        model.addAttribute("memberNo", memberInfo.getMemberNo());

        return "mypage/member/memberAddressList";
    }

    @GetMapping("/update/{addressNo}")
    public String retrieveUpdateAddressForm(MemberInfo memberInfo,
                                            @PathVariable Long addressNo,
                                            Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/" + addressNo);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<DeliveryDestinationRetrieveResponse> response =
            restTemplate.exchange(requestEntity, DeliveryDestinationRetrieveResponse.class);

        model.addAttribute("address", response.getBody());
        model.addAttribute("memberNo", memberInfo.getMemberNo());

        return "mypage/member/memberAddressUpdateForm";
    }

    @PostMapping("/update/{addressNo}")
    public String updateAddress(@Valid @ModelAttribute DeliveryDestinationCURequest requestDto,
                                BindingResult bindingResult,
                                MemberInfo memberInfo,
                                @PathVariable Long addressNo,
                                Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = URI.create(
            redirectGatewayPrefix + "/update/" + memberInfo.getMemberNo() + "/" + addressNo);


        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(requestDto), headers,
                HttpMethod.POST, uri);

        restTemplate.exchange(requestEntity, Void.class);

        return "complete";
    }

    @GetMapping("/delete/{addressNo}")
    public String deleteAddress(MemberInfo memberInfo,
                                @PathVariable Long addressNo,
                                Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(
            redirectGatewayPrefix + "/" + memberInfo.getMemberNo() + "/" + addressNo);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.DELETE, uri);

        ResponseEntity<Void> response =
            restTemplate.exchange(requestEntity, Void.class);

        model.addAttribute("address", response.getBody());

        return "redirect:/member/profile/address";
    }
}
