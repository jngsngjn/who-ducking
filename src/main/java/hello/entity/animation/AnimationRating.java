package hello.entity.animation;

import lombok.Getter;

@Getter
public enum AnimationRating {
    ALL("전체 관람가"),
    TWELVE("12세 관람가"),
    FIFTEEN("15세 관람가"),
    ADULT("성인 관람가");

    private final String description;

    AnimationRating(String description) {
        this.description = description;
    }
}