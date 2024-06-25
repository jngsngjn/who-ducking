package hello.controller.popup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/popup")
public class PopupStoreController {

    @GetMapping()
    public String popupStore() {
        return "playground/popupStore";
    }
}
