package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1 () {
        // 이렇게 리턴하면, 엔티티의 필드가 모두 노출된다.
        // 그럼 노출을 원하지 않는 엔티티 필드 위에다가 @JsonIgnore 해주면된다.
        // 그러나 엔티티안에 @JsonIgnore만으로 모든 스펙을 녹여내기 쉽지않으며, 엔티티 안에서 화면을 뿌리기 위한 프레젠테이션 계층의 로직이 들어와버림
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse seveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
//        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse seveMemberV2 (@RequestBody @Valid CreateMemberRequest request ) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long id,@RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
        memberService.update(id, updateMemberRequest.getName());
        Member member = memberService.findMember(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;

        public CreateMemberRequest(String name) {
            this.name = name;
        }
    }
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;

        public UpdateMemberRequest(String name) {
            this.name = name;
        }
    }
    @Data
    static class UpdateMemberResponse {
        private Long id;
        private String name;

        public UpdateMemberResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
