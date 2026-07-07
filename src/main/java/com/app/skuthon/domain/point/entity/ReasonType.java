package com.app.skuthon.domain.point.entity;

public enum ReasonType {
  SEED,           // 가입 시드 300P (data.sql로 대체됐지만 enum엔 유지)
  PROOF_REWARD,   // 인증 성공 보상 +30P
  BET_PLACE,      // 베팅 차감 (음수)
  BET_CANCEL,     // 베팅 취소 환불 (양수)
  BET_WIN         // 베팅 적중 배당 지급
}
