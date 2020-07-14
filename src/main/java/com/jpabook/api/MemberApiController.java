package com.jpabook.api;

import com.jpabook.domain.Member;
import com.jpabook.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

//@Controller + @ResponseBody = @RestController
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // @RequestBody : json 객체를 Member로 바꿔줌
    // 엔티티에 종속적으로 변한다. 엔티티 따로, 컨트롤러 따로 개발할 때, 스팩이 변하면 에러를 찾기 힘들다.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }



    // 엔티티에 종속적이지 않게 DTO를 만들어서 처리해준다.
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        // 엔티티와의 매핑하는데 번거롭지만, api 스팩별 관리가 가능해지므로 좋다.
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest{

        @NotEmpty   // 무조건 값이 있어야 한다.
        // 여기서 valid 처리를 해준다.
        private String name;
    }




    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    class UpdateMemberResponse{
        private Long id;
        private String name;
    }

}
