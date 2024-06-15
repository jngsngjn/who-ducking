package hello.controller;

import hello.service.AnimationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// @ 커뮤니티 페이지
@Controller
//@RequestMapping("/community")
public class CommunityController {

    private final AnimationService animationService;

    public CommunityController(AnimationService animationService) {
        this.animationService = animationService;
    }

    //전체 애니메이션 데이터 조회
    @GetMapping("/community")
    public String animations(Model model) {
//        List<GetAniListDTO> aniLists = animationService.getAniLists();
//        model.addAttribute("aniLists", aniLists);
        System.out.println("화연이가 시킴");
        return "ReviewBoard";
    }

    // 애니메이션 상세페이지(리뷰작성페이지)
    @GetMapping("/community/1")
    public String getAnimation() {
//    @GetMapping("/reviewBoard/{id}")
//    public String getAnimation(@PathVariable("id") Long id, Model model) {
//        GetOneAniDTO oneAnimation = animationService.findById(id);
//        model.addAttribute("animation", oneAnimation);
        return "ReviewWrite";
    }


}
