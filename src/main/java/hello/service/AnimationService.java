package hello.service;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.GetOneAniDTO;
import hello.entity.animation.Animation;
import hello.repository.AnimationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimationService {

    private final AnimationRepository animationRepository;

    // 전체 애니메이션 리스트 이미지 별점 추천수 조회
    public AnimationService(AnimationRepository animationRepository) {
        this.animationRepository = animationRepository;
    }

    // 전체 애니메이션 리스트 이미지 별점 추천수 조회
//    public List <GetAniListDTO> getAniLists(){
//        return animationRepository.getAniLists();
//    }

    // 리뷰 작성시 보여질 애니메이션 상세 데이터 조회
//    public GetOneAniDTO findById(Long id) {
//        return animationRepository.getAniInfo(id);
//    }
}
