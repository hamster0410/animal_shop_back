package animal_shop.tools.Like.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.security.TokenProvider;
import animal_shop.tools.Like.entity.Like;
import animal_shop.tools.Like.repository.LikeRepository;
import animal_shop.tools.map_service.entity.MapEntity;
import animal_shop.tools.map_service.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MapRepository mapRepository;

    @Transactional
    public void addLike(String token, Long mapId) {
        // Member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        // MapEntity 찾기
        MapEntity mapEntity = mapRepository.findById(mapId)
                .orElseThrow(() -> new RuntimeException("Map not found"));

        // 중복 체크: 이미 좋아요를 눌렀는지 확인
        Like existingLike = likeRepository.findByMemberIdAndMapId(member.getId(), mapId);
        if (existingLike != null) {
            throw new RuntimeException("Already liked");
        }

        // Like 객체 생성 후 저장
        Like like = Like.builder()
                .member(member)
                .map(mapEntity)
                .build();

        likeRepository.save(like);
    }

    @Transactional
    public void deleteLike(String token, Long mapId) {
        // Member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        // MapEntity 찾기
        MapEntity mapEntity = mapRepository.findById(mapId)
                .orElseThrow(() -> new RuntimeException("Map not found"));

        // Like 찾기
        Like like = likeRepository.findByMemberIdAndMapId(member.getId(), mapId);
        if (like == null) {
            throw new RuntimeException("Like not found");
        }

        // Like 삭제
        likeRepository.delete(like);
    }
}
