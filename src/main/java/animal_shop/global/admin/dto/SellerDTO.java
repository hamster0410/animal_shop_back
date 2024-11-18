package animal_shop.global.admin.dto;

import animal_shop.community.member.entity.SellerCandidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {

    private String member; // 댓글을 단 사용자

    private String username;

    private String category;

    private String contents;

    private String phone_number;

    //사업자 등록 번호
    private String bln;


    public SellerDTO(SellerCandidate sellerCandidate) {
        this.phone_number = sellerCandidate.getPhone_number();
        this.category = sellerCandidate.getCategory();
        this.contents = sellerCandidate.getContents();
        this.bln = sellerCandidate.getBln();
        this.member = sellerCandidate.getMember().getNickname();
        this.username = sellerCandidate.getMember().getUsername();
    }
}
