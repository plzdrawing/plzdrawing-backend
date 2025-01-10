package org.example.plzdrawing.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.member.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
}
