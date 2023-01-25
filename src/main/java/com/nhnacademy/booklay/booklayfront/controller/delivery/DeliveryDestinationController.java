package com.nhnacademy.booklay.booklayfront.controller.delivery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.booklayfront.dto.delivery.response.DeliveryDestinationRetrieveResponse;
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
@RequestMapping("/address")
public class DeliveryDestinationController {
    private final RestTemplate restTemplate;

    private final String redirectGatewayPrefix;

    private final static String MYPAGE = "/mypage/myPage";

    public DeliveryDestinationController(RestTemplate restTemplate, String gateway) {
        this.restTemplate = restTemplate;
        redirectGatewayPrefix = gateway + "/shop/v1" + "/delivery/destination";
    }

    @PostMapping("/register/{memberNo}")
    public String create(@Valid @ModelAttribute DeliveryDestinationCURequest requestDto,
                         BindingResult bindingResult,
                         @PathVariable Long memberNo,
                         Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = URI.create(redirectGatewayPrefix + "/create/" + memberNo);

        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(requestDto), headers,
                HttpMethod.POST, uri);

        ResponseEntity<Void> response =
            restTemplate.exchange(requestEntity, Void.class);

        return "redirect:/address/" + memberNo;
    }

    @GetMapping("/register/{memberNo}")
    public String retrieveCreateForm(@PathVariable Long memberNo,
                                     Model model) {
        model.addAttribute("memberNo", memberNo);
        return "mypage/member/memberAddressRegisterForm";
    }


    @GetMapping("/{memberNo}")
    public String retrieveAddress(@PathVariable Long memberNo, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/list/" + memberNo);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<List<DeliveryDestinationRetrieveResponse>> response =
            restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<>() {
                });

        model.addAttribute("addresses", response.getBody());
        model.addAttribute("memberNo", memberNo);
        model.addAttribute("targetUrl", "member/memberAddressList");

        return MYPAGE;
    }

    @GetMapping("/update/{memberNo}/{addressNo}")
    public String retrieveUpdateAddressForm(@PathVariable Long memberNo,
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
        model.addAttribute("memberNo", memberNo);

        return "mypage/member/memberAddressUpdateForm";
    }

    @PostMapping("/update/{memberNo}/{addressNo}")
    public String updateAddress(@Valid @ModelAttribute DeliveryDestinationCURequest requestDto,
                                BindingResult bindingResult,
                                @PathVariable Long memberNo,
                                @PathVariable Long addressNo,
                                Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = URI.create(redirectGatewayPrefix + "/update/" + memberNo + "/" + addressNo);


        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(requestDto), headers,
                HttpMethod.POST, uri);

        ResponseEntity<Void> response =
            restTemplate.exchange(requestEntity, Void.class);

        return "redirect:/address/" + memberNo;
    }

    @GetMapping("/delete/{memberNo}/{addressNo}")
    public String deleteAddress(@PathVariable Long memberNo,
                                @PathVariable Long addressNo,
                                Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/delete/" + memberNo + "/" + addressNo);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.DELETE, uri);

        ResponseEntity<Void> response =
            restTemplate.exchange(requestEntity, Void.class);

        model.addAttribute("address", response.getBody());

        return "redirect:/address/" + memberNo;
    }

}
