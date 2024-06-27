package hello.controller.mypage;

import hello.dto.user.CustomOAuth2User;
import hello.dto.user.MyRequestDTO;
import hello.dto.user.RequestDTO;
import hello.entity.user.User;
import hello.service.basic.RequestService;
import hello.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class RequestController {

    private final UserService userService;
    private final RequestService requestService;

    @GetMapping("/request-list")
    public String requestListOrDetail(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "requestId", required = false) Long requestId) {
        Page<MyRequestDTO> requestPage = requestService.getMyRequest(page, 5);
        model.addAttribute("requestPage", requestPage);
        return "mypage/myRequest";
    }

    @PostMapping("/request")
    public String request(@AuthenticationPrincipal CustomOAuth2User user, @ModelAttribute RequestDTO requestDTO) {
        User loginUser = userService.getLoginUserDetail(user);
        requestService.writeRequest(requestDTO, loginUser);
        return "redirect:/myPage/request-list";
    }

    @PostMapping("/request-delete")
    public String deleteRequest(@RequestParam("requestId") Long requestId) {
        requestService.deleteRequest(requestId);
        return "redirect:/myPage/request-list";
    }
}