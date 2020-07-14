package com.jpabook.api;

import com.jpabook.domain.Member;
import com.jpabook.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

//@Controller + @ResponseBody = @RestController
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 단순 조회
    @GetMapping("/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    // 버전 업
    // 엔티티 스팩이 변해도 유지보수하기 편하다.
    @GetMapping("/v2/members")
    public Result memberV2(){
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m->new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(),collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    // @RequestBody : json 객체를 Member로 바꿔줌
    // 엔티티에 종속적으로 변한다. 엔티티 따로, 컨트롤러 따로 개발할 때, 스팩이 변하면 에러를 찾기 힘들다.
    @PostMapping("/v1/members")
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
    @PostMapping("/v2/members")
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
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName()); // 업데이트
        Member findMember = memberService.findOne(id);  // 조회를 분리한다. 커멘드와, 쿼리를 분리하여 관리하는 습관 기르자.
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
    }

    // 안쪽에서 만드는 경우 inner로 사용하면 된다.
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
