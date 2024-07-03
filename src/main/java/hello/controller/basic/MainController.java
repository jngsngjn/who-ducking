package hello.controller.basic;

import hello.dto.animation.AnimationMainDTO;
import hello.dto.board.BoardListMainDTO;
import hello.dto.main.MainDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.board.Board;
import hello.entity.user.ProfileImage;
import hello.entity.user.User;
import hello.service.animations.AnimationService;
import hello.service.basic.UserService;
import hello.service.board.BoardService;
import hello.service.board.CommentService;
import hello.service.main.MainService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final MainService mainService;
    private final AnimationService animationService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal CustomOAuth2User user, HttpSession session, Model model) {
        if (user != null) {

            User loginUser = userService.getLoginUserDetail(user);

            if (loginUser != null) {
                String nickname = loginUser.getNickname();
                String levelImageName = loginUser.getLevel().getImageName();
                ProfileImage profileImage = loginUser.getProfileImage();
                String profileImageName = null;
                if (profileImage != null) {
                    profileImageName = profileImage.getStoreImageName();
                }

                session.setAttribute("nickname", nickname);
                session.setAttribute("levelImageName", levelImageName);
                session.setAttribute("profileImageName", profileImageName);
                session.setAttribute("level", loginUser.getLevel().getId());
                session.setAttribute("point", loginUser.getPoint());
                session.setAttribute("currentExp", loginUser.getCurrentExp());
                session.setAttribute("maxExp", loginUser.getLevel().getMaxExp());

                String role = loginUser.getRole();
                if (role.equals("ROLE_ADMIN")) {
                    session.setAttribute("isAdmin", true);
                }
            }
        }

        // 최신 게시글 5개 가져오기
        List<Board> boardList = boardService.getBoardsSortedByWriteDateToMain();

        // 각 게시글의 댓글 개수 가져오기
        List<BoardListMainDTO> boardDtoList = boardList.stream().map(board -> {
            int commentCount = commentService.countCommentsByBoardId(board.getId());
            return new BoardListMainDTO(board, commentCount);
        }).collect(Collectors.toList());

        model.addAttribute("boardListToMain", boardDtoList);


        List<AnimationMainDTO> animationMain = animationService.getAnimationMain();
        for (AnimationMainDTO animationMainDTO : animationMain) {
            System.out.println("animationMainDTO = " + animationMainDTO);
        }

        return "index";
    }

    @ResponseBody
    @GetMapping("/api/animations")
    public List<MainDTO> getAnimations() {
        return mainService.getMainAnimations();
    }
}