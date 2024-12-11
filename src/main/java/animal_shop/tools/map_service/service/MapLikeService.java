package animal_shop.tools.map_service.service;

import animal_shop.community.heart_post.entity.Heart;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.security.TokenProvider;
import animal_shop.tools.map_service.entity.MapEntity;
import animal_shop.tools.map_service.entity.MapLike;
import animal_shop.tools.map_service.repository.MapLikeRepository;
import animal_shop.tools.map_service.repository.MapRepository;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MapLikeService {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MapService mapService;

    @Autowired
    private MapLikeRepository mapLikeRepository;

    @Transactional
    public void addHeart(String token, Long mapId) {
        // member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        // MapEntity 찾기
        MapEntity map = mapRepository.findById(mapId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found"));
        // 이미 해당 회원이 이 지도에 좋아요를 눌렀는지 확인
        MapLike existingLike = mapLikeRepository.findByMemberIdAndMapId(mapId, Long.valueOf(userId));
        if (existingLike != null) {
            throw new IllegalArgumentException("You have already liked this map");
        }
        // Map의 좋아요 수 증가
        map.setCountLike(map.getCountLike() + 1);
        mapRepository.save(map);

        // MapLike 객체 생성 후 저장
        MapLike mapLike = MapLike.builder()
                .member(member)
                .map(map) // MapEntity를 설정
                .build();

        mapLikeRepository.save(mapLike);
    }


    @Transactional
    public void deleteHeart(String token, Long mapId) {
        // member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        // MapEntity 찾기
        MapEntity map = mapRepository.findById(mapId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found"));

        // MapLike 찾기
        MapLike mapLike = mapLikeRepository.findByMemberIdAndMapId(mapId, Long.valueOf(userId));
        if (mapLike == null) {
            throw new IllegalArgumentException("Like not found for this map and member");
        }

        // 좋아요 수 감소
        map.setCountLike(map.getCountLike() - 1);
        mapRepository.save(map);

        // MapLike 삭제
        mapLikeRepository.delete(mapLike);
    }
}
