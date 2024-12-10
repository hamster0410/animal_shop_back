package animal_shop.shop.pet.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.pet.dto.PetDTO;
import animal_shop.shop.pet.dto.PetProfile;
import animal_shop.shop.pet.dto.PetProfileList;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        pet.setRegistrationCode(petDTO.getRegistrationCode());
        pet.setDescription(petDTO.getDescription()); // 자기소개 설정
        pet.setProfileImageUrl(petDTO.getProfileImageUrl()); // 프로필 이미지 URL 설정
        if(member.getPets().isEmpty()) {
            pet.setLeader(true);
        }else{
            pet.setLeader(false);
        }

        //4.저장
        pet = petRepository.save(pet);

    }
    @Transactional
    public void updateLeader(Long petId) {
        // 1. 해당 petId로 동물 조회
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 동물이 존재하지 않습니다."));

        // 2. 해당 동물의 주인(회원) 정보 가져오기
        Member member = pet.getMember();

        // 3. 해당 회원의 모든 동물 조회
        petRepository.findByMember(member, Pageable.unpaged()).forEach(existingPet -> {
            // 4. 모든 동물들의 leader 값을 false로 설정
            existingPet.setLeader(false);
            petRepository.save(existingPet);  // 동물 저장
        });

        // 5. 지정된 pet의 leader 값을 true로 설정
        pet.setLeader(true);
        petRepository.save(pet);  // 변경된 pet 저장
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

    @Transactional
    public void updatePet(String token, String petId, PetDTO petDTO) {
        // 1. 사용자 인증
        String userId = tokenProvider.extractIdByAccessToken(token);

        // 2. 기존 Pet 조회
        Pet pet = petRepository.findById(Long.valueOf(petId))
                .orElseThrow(() -> new IllegalArgumentException("해당 동물이 존재하지 않습니다."));

        petRepository.findById(Long.valueOf(petId))
                .orElseThrow(() -> new IllegalArgumentException("해당 동물이 존재하지 않습니다."));

        // 3. 소유자 확인
        if (!pet.getMember().getId().equals(Long.valueOf(userId))) {
            throw new IllegalArgumentException("해당 사용자는 수정할 권한이 없습니다.");
        }

        // 필드 업데이트
        Optional.ofNullable(petDTO.getName()).ifPresent(pet::setName);
        Optional.ofNullable(petDTO.getGender()).ifPresent(pet::setGender);
        Optional.ofNullable(petDTO.getSpecies()).ifPresent(pet::setSpecies);
        Optional.ofNullable(petDTO.getBreed()).ifPresent(pet::setBreed);
        Optional.ofNullable(petDTO.getIsNeutered()).ifPresent(pet::setIsNeutered);
        Optional.ofNullable(petDTO.getAge()).ifPresent(pet::setAge);
        Optional.ofNullable(petDTO.getWeight()).ifPresent(pet::setWeight);
        Optional.ofNullable(petDTO.getDescription()).ifPresent(pet::setDescription);
        Optional.ofNullable(petDTO.getProfileImageUrl()).ifPresent(pet::setProfileImageUrl);
        Optional.ofNullable(petDTO.getRegistrationCode()).ifPresent(pet::setRegistrationCode);
        Optional.ofNullable(petDTO.isLeader()).ifPresent(pet::setLeader);

        petRepository.save(pet);
    }
    @Transactional(readOnly = true)
    public PetProfileList selectAll(String token, int page) {
        // 1. 사용자 인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        // 2. 페이징 설정
        Pageable pageable = PageRequest.of(page, 5); // 한 페이지당 5개 조회

        // 3. 전체 동물 조회 (회원과 연관된 동물만 조회)
        Page<Pet> pets = petRepository.findByMember(member, pageable);

        List<PetProfile> petList = pets.stream().map(PetProfile::new).toList();

        PetProfileList petProfileList = PetProfileList.builder()
                .petProfileList(petList)
                .total_count(pets.getTotalElements())
                .build();        // 4. DTO 변환 및 반환

        return petProfileList;
    }


}
