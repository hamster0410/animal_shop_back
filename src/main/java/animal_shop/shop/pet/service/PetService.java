package animal_shop.shop.pet.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.pet.dto.PetDTO;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Transactional
    public void registerPet(String token, PetDTO petDTO) {
        //1. 사용자 인증(token,member)
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        //2.입력
        Pet pet = new Pet();

        // 3. DTO에서 정보를 받아서 PetEntity로 설정
        pet.setMember(member); // 동물 주인 설정
        pet.setName(petDTO.getName()); // 동물 이름 설정
        pet.setSpecies(petDTO.getSpecies()); // 동물 종 설정
        pet.setBreed(petDTO.getBreed()); // 품종 설정
        pet.setIsNeutered(petDTO.getIsNeutered()); // 중성화 여부 설정
        pet.setAge(petDTO.getAge()); // 나이 설정
        pet.setGender(petDTO.getGender()); // 성별 설정
        pet.setWeight(petDTO.getWeight()); // 체중 설정
        pet.setHasRegistrationCertificate(petDTO.getHasRegistrationCertificate()); // 등록증 유무 설정
        pet.setDescription(petDTO.getDescription()); // 자기소개 설정
        pet.setProfileImageUrl(petDTO.getProfileImageUrl()); // 프로필 이미지 URL 설정

        //4.저장
        pet = petRepository.save(pet);

    }

    public void deletePet(String token,String pet_id) {
        //1. 사용자 인증
        String userId = tokenProvider.extractIdByAccessToken(token);

        // 2. petId로 해당 동물 조회
        Pet pet = petRepository.findById(Long.valueOf(pet_id))
                .orElseThrow(() -> new IllegalArgumentException("해당 동물이 존재하지 않습니다."));

        // 3. 찐 주인인지 확인 (동물의 주인과 비교)
        if (!pet.getMember().getId().equals(Long.valueOf(userId))) {
            throw new IllegalArgumentException("해당 사용자는 삭제할 권한이 없습니다.");
        }

        //4. 해당 강아지 삭제
        petRepository.delete(pet);


    }
}
